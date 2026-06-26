package com.finance.module.cashier.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.cashier.entity.CashDaily;
import com.finance.module.cashier.mapper.CashDailyMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/cashier/daily")
public class CashDailyController {
    @Resource
    private CashDailyMapper mapper;

    @GetMapping("/page")
    public Result<PageResult<CashDaily>> page(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String billNo,
            @RequestParam(required = false) String billType,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        LambdaQueryWrapper<CashDaily> qw = new LambdaQueryWrapper<>();
        if (billNo != null && !billNo.isEmpty()) qw.like(CashDaily::getSummary, billNo);
        if (billType != null && !billType.isEmpty()) qw.eq(CashDaily::getTransType, billType);
        if (accountId != null) qw.eq(CashDaily::getAccountId, accountId);
        if (startDate != null && !startDate.isEmpty()) qw.ge(CashDaily::getTransDate, startDate);
        if (endDate != null && !endDate.isEmpty()) qw.le(CashDaily::getTransDate, endDate);
        qw.orderByDesc(CashDaily::getTransDate);
        Page<CashDaily> p = mapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        // 动态生成单号（DB 无此列）
        for (CashDaily d : p.getRecords()) {
            if (d.getBillNo() == null || d.getBillNo().isEmpty()) {
                d.setBillNo("CD-" + d.getTransDate().toString().replace("-","") + "-" + String.format("%04d", d.getId()));
            }
        }
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/{id}")
    public Result<CashDaily> get(@PathVariable Long id) {
        return Result.success(mapper.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody CashDaily daily) {
        if (daily.getBillNo() == null || daily.getBillNo().isEmpty()) {
            daily.setBillNo("CD-" + System.currentTimeMillis());
        }
        if (daily.getStatus() == null) daily.setStatus("POSTED");
        return Result.success(mapper.insert(daily) > 0);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody CashDaily daily) {
        return Result.success(mapper.updateById(daily) > 0);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(mapper.deleteById(id) > 0);
    }

    @PostMapping("/post/{id}")
    public Result<Boolean> post(@PathVariable Long id) {
        CashDaily d = mapper.selectById(id);
        if (d == null) return Result.error("数据不存在");
        d.setStatus("POSTED");
        return Result.success(mapper.updateById(d) > 0);
    }
}
