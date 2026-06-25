package com.finance.module.consol.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.consol.entity.ConsolOffset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface ConsolOffsetMapper extends BaseMapper<ConsolOffset> {
    BigDecimal sumByType(@Param("year") String year, @Param("period") Integer period);
    List<Map<String, Object>> analysis(@Param("year") String year, @Param("period") Integer period);
}
