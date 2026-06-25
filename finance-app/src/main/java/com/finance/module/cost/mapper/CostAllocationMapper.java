package com.finance.module.cost.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.cost.entity.CostAllocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CostAllocationMapper extends BaseMapper<CostAllocation> {
    List<Map<String, Object>> sumByCenter(@Param("year") String year, @Param("period") Integer period);
}
