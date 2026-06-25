package com.finance.module.budget.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.budget.entity.BudgetExecute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BudgetExecuteMapper extends BaseMapper<BudgetExecute> {
    List<BudgetExecute> selectByBudget(@Param("budgetId") Long budgetId);
}
