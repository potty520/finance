package com.finance.module.cost.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.cost.entity.CostProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CostProductMapper extends BaseMapper<CostProduct> {
    List<Map<String, Object>> summary(@Param("year") String year, @Param("period") Integer period);
}
