package com.finance.module.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    List<Map<String, Object>> selectIncomeStatementItems(@Param("year") String year, @Param("period") Integer period);

    List<Map<String, Object>> selectCashFlowItems(@Param("year") String year, @Param("period") Integer period);
}
