package com.finance.module.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.stock.entity.StockBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface StockBalanceMapper extends BaseMapper<StockBalance> {
    List<StockBalance> selectByGoods(@Param("goodsId") Long goodsId);
    BigDecimal getEndQuantity(@Param("goodsId") Long goodsId, @Param("warehouseId") Long warehouseId);
    List<Map<String, Object>> sumByCategory();
}
