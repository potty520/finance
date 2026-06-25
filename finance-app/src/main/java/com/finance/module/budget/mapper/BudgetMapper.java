package com.finance.module.budget.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.budget.entity.Budget;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface BudgetMapper extends BaseMapper<Budget> {
    BigDecimal sumAmount(@Param("year") String year, @Param("period") Integer period);
    List<Map<String, Object>> analysis(@Param("year") String year, @Param("period") Integer period);
    Budget findBudget(@Param("year") String year, @Param("period") Integer period,
                      @Param("subjectCode") String subjectCode, @Param("deptId") Long deptId);
}
