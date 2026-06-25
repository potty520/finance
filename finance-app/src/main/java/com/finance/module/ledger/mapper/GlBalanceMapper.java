package com.finance.module.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.ledger.entity.GlBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GlBalanceMapper extends BaseMapper<GlBalance> {
    List<GlBalance> selectByPeriod(@Param("year") String year, @Param("period") Integer period);
    List<Map<String, Object>> sumBySubjectType(@Param("year") String year, @Param("period") Integer period);
    List<Map<String, Object>> selectBalanceSheetItems(@Param("year") String year, @Param("period") Integer period);
}
