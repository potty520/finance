package com.finance.module.cashier.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.cashier.entity.*;
import com.finance.module.cashier.mapper.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/cashier/account")
public class CashAccountController {
    @Resource
    private CashAccountMapper mapper;

    @GetMapping("/list")
    public Result<List<CashAccount>> list() {
        return Result.success(mapper.selectActive());
    }

    @GetMapping("/page")
    public Result<PageResult<CashAccount>> page(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<CashAccount> qw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            qw.and(w -> w.like(CashAccount::getAccountCode, keyword)
                    .or().like(CashAccount::getAccountName, keyword));
        }
        qw.orderByAsc(CashAccount::getAccountCode);
        Page<CashAccount> p = mapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/{id}")
    public Result<CashAccount> get(@PathVariable Long id) {
        return Result.success(mapper.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody CashAccount account) {
        if (account.getStatus() == null) account.setStatus(1);
        if (account.getCurrentBalance() == null) {
            account.setCurrentBalance(account.getOpeningBalance() != null
                    ? account.getOpeningBalance() : account.getBeginBalance());
        }
        return Result.success(mapper.insert(account) > 0);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody CashAccount account) {
        return Result.success(mapper.updateById(account) > 0);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(mapper.deleteById(id) > 0);
    }
}
