package com.finance.module.expense.service.impl;

import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import com.finance.common.service.CurrentUserResolver;
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
    @Resource private CurrentUserResolver currentUser;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExpenseApply apply(ExpenseApply apply) {
        if (apply.getAmount() == null || apply.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("报销金额必须大于 0");
        }
        if (apply.getBillNo() == null || apply.getBillNo().trim().isEmpty()) {
            // 时间戳 + 随机后缀，避免同毫秒并发撞号
            apply.setBillNo("EX-" + System.currentTimeMillis() + "-"
                    + java.util.concurrent.ThreadLocalRandom.current().nextInt(1000, 9999));
        }
        if (apply.getApplicant() == null) {
            // 申请人以登录用户为准，避免前端伪造他人报销
            apply.setApplicant(currentUser.currentId());
            apply.setApplicantName(currentUser.currentName());
        }
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
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("还款金额必须大于 0");
        }
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
    @Transactional(rollbackFor = Exception.class)
    public boolean markPaid(Long applyId) {
        ExpenseApply a = applyMapper.selectById(applyId);
        if (a == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"1".equals(a.getStatus())) {
            throw new BusinessException("仅审批通过的报销单可标记为已付款");
        }
        a.setStatus("3");
        a.setUpdateTime(LocalDateTime.now());
        return applyMapper.updateById(a) > 0;
    }

    @Override
    public List<Map<String, Object>> analysis(String year, Integer period) {
        return applyMapper.sumBySubject(year, period);
    }
}
