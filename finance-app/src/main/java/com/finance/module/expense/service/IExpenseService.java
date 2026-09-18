package com.finance.module.expense.service;

import com.finance.module.expense.entity.ExpenseApply;
import com.finance.module.expense.entity.ExpenseLoan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IExpenseService {
    ExpenseApply apply(ExpenseApply apply);
    boolean repayLoan(Long loanId, BigDecimal amount);
    /** 审批通过后标记为已付款 */
    boolean markPaid(Long applyId);
    List<Map<String, Object>> analysis(String year, Integer period);
}
