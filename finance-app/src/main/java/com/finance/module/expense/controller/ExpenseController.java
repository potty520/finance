package com.finance.module.expense.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.expense.entity.ExpenseApply;
import com.finance.module.expense.entity.ExpenseLoan;
import com.finance.module.expense.mapper.ExpenseApplyMapper;
import com.finance.module.expense.mapper.ExpenseLoanMapper;
import com.finance.module.expense.service.IExpenseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Resource private IExpenseService expenseService;
    @Resource private ExpenseApplyMapper applyMapper;
    @Resource private ExpenseLoanMapper loanMapper;

    @PostMapping("/apply")
    public Result<ExpenseApply> apply(@RequestBody ExpenseApply a) {
        return Result.success(expenseService.apply(a));
    }

    @GetMapping("/apply/page")
    public Result<PageResult<ExpenseApply>> applyPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String status) {
        LambdaQueryWrapper<ExpenseApply> qw = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) qw.eq(ExpenseApply::getStatus, status);
        qw.orderByDesc(ExpenseApply::getCreateTime);
        Page<ExpenseApply> p = applyMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/apply/analysis")
    public Result<List<Map<String, Object>>> analysis(
            @RequestParam String fiscalYear,
            @RequestParam Integer fiscalPeriod) {
        return Result.success(expenseService.analysis(fiscalYear, fiscalPeriod));
    }

    @PostMapping("/loan")
    public Result<Boolean> addLoan(@RequestBody ExpenseLoan loan) {
        if (loan.getLoanNo() == null) loan.setLoanNo("LN-" + System.currentTimeMillis());
        loan.setStatus("DRAFT");
        return Result.success(loanMapper.insert(loan) > 0);
    }

    @GetMapping("/loan/list")
    public Result<List<ExpenseLoan>> loanList(@RequestParam(required = false) Long applicant) {
        return Result.success(loanMapper.selectByApplicant(applicant));
    }

    @PostMapping("/loan/repay")
    public Result<Boolean> repay(@RequestBody Map<String, Object> body) {
        Long loanId = Long.valueOf(body.get("loanId").toString());
        java.math.BigDecimal amount = new java.math.BigDecimal(body.get("amount").toString());
        return Result.success(expenseService.repayLoan(loanId, amount));
    }
}
