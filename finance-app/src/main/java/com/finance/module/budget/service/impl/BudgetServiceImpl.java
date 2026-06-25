package com.finance.module.budget.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import com.finance.module.budget.entity.Budget;
import com.finance.module.budget.entity.BudgetExecute;
import com.finance.module.budget.mapper.BudgetExecuteMapper;
import com.finance.module.budget.mapper.BudgetMapper;
import com.finance.module.budget.service.IBudgetService;
import com.finance.module.ledger.entity.GlSubject;
import com.finance.module.ledger.mapper.GlSubjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BudgetServiceImpl implements IBudgetService {

    @Resource private BudgetMapper budgetMapper;
    @Resource private BudgetExecuteMapper executeMapper;
    @Resource private GlSubjectMapper glSubjectMapper;

    @Override
    public boolean saveBudget(Budget b) {
        if (b.getBudgetNo() == null) b.setBudgetNo("BD-" + System.currentTimeMillis());
        if (b.getStatus() == null) b.setStatus(0);
        if (b.getUsedAmount() == null) b.setUsedAmount(BigDecimal.ZERO);
        if (b.getAvailableAmount() == null && b.getAmount() != null) {
            b.setAvailableAmount(b.getAmount().subtract(b.getUsedAmount()));
        }
        if (b.getDeleted() == null) b.setDeleted(0);
        if (b.getControlType() == null) b.setControlType("WARN");
        return budgetMapper.insert(b) > 0;
    }

    @Override
    public boolean updateBudget(Budget b) {
        Budget exist = budgetMapper.selectById(b.getId());
        if (exist == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (exist.getStatus() != null && exist.getStatus() >= 2) {
            throw new BusinessException("已下达/已关闭的预算不可修改");
        }
        if (b.getAmount() != null) {
            BigDecimal used = exist.getUsedAmount() == null ? BigDecimal.ZERO : exist.getUsedAmount();
            b.setAvailableAmount(b.getAmount().subtract(used));
        }
        return budgetMapper.updateById(b) > 0;
    }

    @Override
    public boolean auditBudget(Long id, boolean pass) {
        Budget b = budgetMapper.selectById(id);
        if (b == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (b.getStatus() == null || b.getStatus() != 0) throw new BusinessException("仅未提交状态可审核");
        b.setStatus(pass ? 1 : 9);
        return budgetMapper.updateById(b) > 0;
    }

    @Override
    public boolean issueBudget(Long id) {
        Budget b = budgetMapper.selectById(id);
        if (b == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (b.getStatus() == null || b.getStatus() != 1) throw new BusinessException("仅已审核状态可下达");
        b.setStatus(2);
        return budgetMapper.updateById(b) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkAndDeduct(String year, Integer period, String subjectCode, Long deptId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return true;
        Budget b = findBudget(year, period, subjectCode, deptId);
        if (b == null) return true;
        if (b.getStatus() == null || b.getStatus() != 2) return true;
        BigDecimal available = availableOf(b);
        if (available.compareTo(amount) < 0) {
            throw new BusinessException(ResultCode.INSUFFICIENT_BUDGET);
        }
        b.setUsedAmount((b.getUsedAmount() == null ? BigDecimal.ZERO : b.getUsedAmount()).add(amount));
        budgetMapper.updateById(b);
        BudgetExecute e = new BudgetExecute();
        e.setBudgetId(b.getId());
        e.setFiscalYear(year);
        e.setFiscalPeriod(period);
        e.setAmount(amount);
        e.setOperator(1L);
        e.setOperatorName("系统用户");
        e.setCreateTime(LocalDateTime.now());
        executeMapper.insert(e);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean release(String year, Integer period, String subjectCode, Long deptId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return true;
        Budget b = findBudget(year, period, subjectCode, deptId);
        if (b == null) return true;
        b.setUsedAmount((b.getUsedAmount() == null ? BigDecimal.ZERO : b.getUsedAmount()).subtract(amount));
        return budgetMapper.updateById(b) > 0;
    }

    @Override
    public List<Map<String, Object>> analysis(String year, Integer period) {
        LambdaQueryWrapper<Budget> qw = new LambdaQueryWrapper<Budget>()
                .eq(Budget::getFiscalYear, Integer.valueOf(year))
                .eq(Budget::getFiscalPeriod, period)
                .eq(Budget::getDeleted, 0);
        List<Budget> budgets = budgetMapper.selectList(qw);
        // 加载所有科目名称
        List<GlSubject> subjects = glSubjectMapper.selectList(null);
        Map<String, String> nameMap = new HashMap<>();
        for (GlSubject s : subjects) {
            nameMap.put(s.getSubjectCode(), s.getSubjectName());
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Budget b : budgets) {
            Map<String, Object> row = new HashMap<>();
            String subjectName = nameMap.getOrDefault(b.getSubjectCode(), b.getSubjectCode());
            row.put("subjectName", subjectName);
            row.put("subjectCode", b.getSubjectCode());
            row.put("amount", b.getAmount() == null ? BigDecimal.ZERO : b.getAmount());
            row.put("usedAmount", b.getUsedAmount() == null ? BigDecimal.ZERO : b.getUsedAmount());
            row.put("availableAmount", availableOf(b));
            row.put("status", b.getStatus());
            rows.add(row);
        }
        return rows;
    }

    private Budget findBudget(String year, Integer period, String subjectCode, Long deptId) {
        LambdaQueryWrapper<Budget> qw = new LambdaQueryWrapper<Budget>()
                .eq(Budget::getFiscalYear, Integer.valueOf(year))
                .eq(Budget::getFiscalPeriod, period)
                .eq(Budget::getSubjectCode, subjectCode)
                .eq(Budget::getDeleted, 0);
        if (deptId != null) qw.eq(Budget::getDeptId, deptId);
        qw.last("LIMIT 1");
        return budgetMapper.selectOne(qw);
    }

    private BigDecimal availableOf(Budget b) {
        BigDecimal amount = b.getAmount() == null ? BigDecimal.ZERO : b.getAmount();
        BigDecimal used = b.getUsedAmount() == null ? BigDecimal.ZERO : b.getUsedAmount();
        return amount.subtract(used);
    }
}
