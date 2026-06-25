package com.finance.module.asset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.asset.entity.FaDepreciation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FaDepreciationMapper extends BaseMapper<FaDepreciation> {
    List<FaDepreciation> selectByPeriod(@Param("year") String year, @Param("period") Integer period);
}
