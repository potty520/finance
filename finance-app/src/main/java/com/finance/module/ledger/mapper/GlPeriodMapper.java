package com.finance.module.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.ledger.entity.GlPeriod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GlPeriodMapper extends BaseMapper<GlPeriod> {
    GlPeriod selectByYearPeriod(@Param("year") String year, @Param("period") Integer period);
    GlPeriod selectCurrent();
}
