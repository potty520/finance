package com.finance.module.ledger.controller;

import com.finance.common.exception.BusinessException;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.response.ResultCode;
import com.finance.common.util.CommonUtil;
import com.finance.common.service.CurrentUserResolver;
import com.finance.module.system.entity.SysUser;
import com.finance.module.ledger.entity.GlPeriod;
import com.finance.module.ledger.entity.GlVoucher;
import com.finance.module.ledger.mapper.GlPeriodMapper;
import com.finance.module.ledger.mapper.GlVoucherMapper;
import com.finance.module.ledger.service.GlBalanceService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会计期间
 */
@RestController
@RequestMapping("/ledger/period")
public class GlPeriodController {

    @Resource
    private GlPeriodMapper periodMapper;

    @Resource
    private GlVoucherMapper voucherMapper;

    @Resource
    private GlBalanceService balanceService;

    @Resource
    private CurrentUserResolver currentUserResolver;

    @GetMapping("/list")
    public Result<List<GlPeriod>> list() {
        return Result.success(periodMapper.selectList(
                new LambdaQueryWrapper<GlPeriod>().orderByDesc(GlPeriod::getFiscalYear, GlPeriod::getPeriodIndex)));
    }

    @GetMapping("/page")
    public Result<PageResult<GlPeriod>> page(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize) {
        Page<GlPeriod> p = periodMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)),
                new LambdaQueryWrapper<GlPeriod>().orderByDesc(GlPeriod::getFiscalYear, GlPeriod::getPeriodIndex));
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/current")
    public Result<GlPeriod> current() {
        return Result.success(periodMapper.selectCurrent());
    }

    @GetMapping("/{id}")
    public Result<GlPeriod> get(@PathVariable Long id) {
        return Result.success(periodMapper.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody GlPeriod period) {
        if (period.getStatus() == null) period.setStatus("OPEN");
        return Result.success(periodMapper.insert(period) > 0);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody GlPeriod period) {
        return Result.success(periodMapper.updateById(period) > 0);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(periodMapper.deleteById(id) > 0);
    }

    @PostMapping("/open/{id}")
    public Result<Boolean> open(@PathVariable Long id) {
        GlPeriod p = periodMapper.selectById(id);
        if (p == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        p.setStatus("OPEN");
        return Result.success(periodMapper.updateById(p) > 0);
    }

    @PostMapping("/close/{id}")
    public Result<Boolean> close(@PathVariable Long id) {
        GlPeriod p = periodMapper.selectById(id);
        if (p == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if ("CLOSED".equals(p.getStatus())) throw new BusinessException("该期间已结账");
        String periodCode = p.getPeriodCode();
        if (periodCode == null && p.getFiscalYear() != null && p.getPeriodIndex() != null) {
            periodCode = CommonUtil.buildPeriodCode(String.valueOf(p.getFiscalYear()), p.getPeriodIndex());
        }
        if (periodCode == null) throw new BusinessException("期间编码缺失，无法结账");
        // 存在未过账（草稿/待审/已审未过账）凭证时不允许结账
        long unfinished = voucherMapper.selectCount(new LambdaQueryWrapper<GlVoucher>()
                .eq(GlVoucher::getPeriodCode, periodCode)
                .in(GlVoucher::getStatus, "DRAFT", "D", "APPROVING", "A", "SUBMITTED", "APPROVED"));
        if (unfinished > 0) {
            throw new BusinessException("本期还有 " + unfinished + " 张凭证未过账，请先处理后再结账");
        }
        SysUser cu = currentUserResolver.require();
        p.setStatus("CLOSED");
        p.setCloseTime(LocalDateTime.now());
        p.setCloser(cu.getId());
        p.setCloserName(cu.getRealName() != null ? cu.getRealName() : cu.getUsername());
        boolean ok = periodMapper.updateById(p) > 0;
        if (ok) {
            // 期末结转：本期期末余额结转为下期期初，并确保下期期间存在
            balanceService.carryForward(periodCode);
            ensureNextPeriod(p, periodCode);
        }
        return Result.success(ok);
    }

    /** 结账后自动创建下一会计期间（若不存在） */
    private void ensureNextPeriod(GlPeriod p, String periodCode) {
        int year = Integer.parseInt(periodCode.substring(0, 4));
        int idx = Integer.parseInt(periodCode.substring(4, 6));
        int ny = idx >= 12 ? year + 1 : year;
        int ni = idx >= 12 ? 1 : idx + 1;
        String nextCode = ny + String.format("%02d", ni);
        GlPeriod exist = periodMapper.selectOne(new LambdaQueryWrapper<GlPeriod>()
                .eq(GlPeriod::getPeriodCode, nextCode).last("LIMIT 1"));
        if (exist != null) return;
        GlPeriod np = new GlPeriod();
        np.setPeriodCode(nextCode);
        np.setPeriodName(ny + "年" + ni + "月");
        np.setFiscalYear(ny);
        np.setPeriodIndex(ni);
        np.setStartDate(LocalDate.of(ny, ni, 1));
        np.setEndDate(LocalDate.of(ny, ni, 1).plusMonths(1).minusDays(1));
        np.setStatus("OPEN");
        np.setCreateTime(LocalDateTime.now());
        periodMapper.insert(np);
    }

    @PostMapping("/unclose/{id}")
    public Result<Boolean> unclose(@PathVariable Long id) {
        GlPeriod p = periodMapper.selectById(id);
        if (p == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        p.setStatus("OPEN");
        p.setCloseTime(null);
        p.setCloser(null);
        p.setCloserName(null);
        return Result.success(periodMapper.updateById(p) > 0);
    }

    @GetMapping("/status/{year}/{period}")
    public Result<Map<String, Object>> status(@PathVariable String year, @PathVariable Integer period) {
        GlPeriod p = periodMapper.selectByYearPeriod(year, period);
        Map<String, Object> r = new HashMap<>();
        r.put("year", year);
        r.put("period", period);
        r.put("status", p == null ? -1 : p.getStatus());
        return Result.success(r);
    }
}
