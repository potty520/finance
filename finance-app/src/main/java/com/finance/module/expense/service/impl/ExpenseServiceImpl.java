package com.finance.module.expense.service.impl;

import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import com.finance.module.expense.entity.ExpenseApply;
import com.finance.module.expense.entity.ExpenseLoan;
import com.finance.module.expense.mapper.ExpenseApplyMapper;
import com.finance.module.expense.mapper.ExpenseLoanMapper;
import com.finance.module.expense.service.IExpenseService;
import com.finance.module.workflow.service.IWorkflowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseServiceImpl implements IExpenseService {

    @Resource private ExpenseApplyMapper applyMapper;
    @Resource private ExpenseLoanMapper loanMapper;
    @Resource private IWorkflowService wfService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExpenseApply apply(ExpenseApply apply) {
        if (apply.getBillNo() == null) apply.setBillNo("EX-" + System.currentTimeMillis());
        apply.setStatus("0");
        apply.setCreateTime(LocalDateTime.now());
        applyMapper.insert(apply);
        // 启动审批流
        wfService.start("expense", apply.getId(), apply.getBillNo(),
                apply.getApplicant(), apply.getApplicantName(), apply.getAmount());
        return apply;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean repayLoan(Long loanId, BigDecimal amount) {
        ExpenseLoan loan = loanMapper.selectById(loanId);
        if (loan == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        BigDecimal repaid = loan.getRepaidAmount() == null ? BigDecimal.ZERO : loan.getRepaidAmount();
        BigDecimal pending = loan.getPendingAmount() == null ? loan.getAmount() : loan.getPendingAmount();
        if (amount.compareTo(pending) > 0) throw new BusinessException("还款金额超限");
        loan.setRepaidAmount(repaid.add(amount));
        loan.setPendingAmount(pending.subtract(amount));
        if (loan.getPendingAmount().compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus("3");
        }
        return loanMapper.updateById(loan) > 0;
    }

    @Override
    public List<Map<String, Object>> analysis(String year, Integer period) {
        return applyMapper.sumBySubject(year, period);
    }
}
