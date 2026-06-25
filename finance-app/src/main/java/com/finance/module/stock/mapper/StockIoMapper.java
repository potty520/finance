package com.finance.module.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.stock.entity.StockIo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockIoMapper extends BaseMapper<StockIo> {
    List<Map<String, Object>> selectStockSummary();
    List<Map<String, Object>> selectIoByGoods(@Param("goodsId") Long goodsId);
}
