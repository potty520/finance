package com.finance.module.cashier.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.cashier.entity.CashBill;
import com.finance.module.cashier.mapper.CashBillMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/cashier/bill")
public class CashBillController {
    @Resource
    private CashBillMapper mapper;

    @GetMapping("/page")
    public Result<PageResult<CashBill>> page(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String billType,
            @RequestParam(required = false) String status) {
        LambdaQueryWrapper<CashBill> qw = new LambdaQueryWrapper<>();
        if (billType != null && !billType.isEmpty()) qw.eq(CashBill::getBillType, billType);
        if (status != null && !status.isEmpty()) qw.eq(CashBill::getStatus, status);
        qw.orderByDesc(CashBill::getIssueDate);
        Page<CashBill> p = mapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/list")
    public Result<List<CashBill>> list() {
        return Result.success(mapper.selectList(null));
    }

    @GetMapping("/{id}")
    public Result<CashBill> get(@PathVariable Long id) {
        return Result.success(mapper.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody CashBill bill) {
        if (bill.getStatus() == null) bill.setStatus("1");
        return Result.success(mapper.insert(bill) > 0);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody CashBill bill) {
        return Result.success(mapper.updateById(bill) > 0);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(mapper.deleteById(id) > 0);
    }

    @PostMapping("/use/{id}")
    public Result<Boolean> use(@PathVariable Long id) {
        CashBill b = mapper.selectById(id);
        if (b == null) return Result.error("数据不存在");
        b.setStatus("2");
        return Result.success(mapper.updateById(b) > 0);
    }

    @PostMapping("/cancel/{id}")
    public Result<Boolean> cancel(@PathVariable Long id) {
        CashBill b = mapper.selectById(id);
        if (b == null) return Result.error("数据不存在");
        b.setStatus("5");
        return Result.success(mapper.updateById(b) > 0);
    }
}
