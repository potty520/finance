package com.finance.module.budget.service;

import com.finance.module.budget.entity.Budget;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IBudgetService {
    boolean saveBudget(Budget budget);
    boolean updateBudget(Budget budget);
    boolean auditBudget(Long id, boolean pass);
    boolean issueBudget(Long id);

    /**
     * 预算占用检查与扣减
     */
    boolean checkAndDeduct(String year, Integer period, String subjectCode, Long deptId, BigDecimal amount);

    /**
     * 预算释放
     */
    boolean release(String year, Integer period, String subjectCode, Long deptId, BigDecimal amount);

    List<Map<String, Object>> analysis(String year, Integer period);
}
