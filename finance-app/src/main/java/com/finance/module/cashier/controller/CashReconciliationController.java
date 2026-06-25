package com.finance.module.cashier.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.cashier.entity.CashReconciliation;
import com.finance.module.cashier.mapper.CashReconciliationMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/cashier/reconciliation")
public class CashReconciliationController {
    @Resource
    private CashReconciliationMapper mapper;

    @GetMapping("/page")
    public Result<PageResult<CashReconciliation>> page(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize) {
        Page<CashReconciliation> p = mapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)),
                new LambdaQueryWrapper<CashReconciliation>().orderByDesc(CashReconciliation::getCreateTime));
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/{id}")
    public Result<CashReconciliation> get(@PathVariable Long id) {
        return Result.success(mapper.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody CashReconciliation rec) {
        if (rec.getStatus() == null) rec.setStatus("0");
        rec.setDiffAmount(rec.getBankBalance().subtract(rec.getBookBalance()));
        return Result.success(mapper.insert(rec) > 0);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody CashReconciliation rec) {
        rec.setDiffAmount(rec.getBankBalance().subtract(rec.getBookBalance()));
        return Result.success(mapper.updateById(rec) > 0);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(mapper.deleteById(id) > 0);
    }

    @PostMapping("/confirm/{id}")
    public Result<Boolean> confirm(@PathVariable Long id) {
        CashReconciliation r = mapper.selectById(id);
        if (r == null) return Result.error("数据不存在");
        r.setStatus("1");
        r.setReconTime(LocalDateTime.now());
        return Result.success(mapper.updateById(r) > 0);
    }
}
