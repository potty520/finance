package com.finance.module.expense.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.expense.entity.ExpenseLoan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExpenseLoanMapper extends BaseMapper<ExpenseLoan> {
    List<ExpenseLoan> selectByApplicant(@Param("applicant") Long applicant);
}
