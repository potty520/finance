package com.finance.module.ledger.controller;

import com.finance.common.exception.BusinessException;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.response.ResultCode;
import com.finance.common.util.CommonUtil;
import com.finance.module.ledger.entity.GlPeriod;
import com.finance.module.ledger.mapper.GlPeriodMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
        p.setStatus("CLOSED");
        p.setCloseTime(LocalDateTime.now());
        p.setCloser(1L);
        p.setCloserName("系统用户");
        return Result.success(periodMapper.updateById(p) > 0);
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
