package com.finance.module.expense.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.expense.entity.ExpenseApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExpenseApplyMapper extends BaseMapper<ExpenseApply> {
    List<Map<String, Object>> sumBySubject(@Param("year") String year, @Param("period") Integer period);
}
