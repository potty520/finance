package com.finance.module.stock.service;

import com.finance.module.stock.entity.StockGoods;
import com.finance.module.stock.entity.StockIo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IStockService {
    boolean saveGoods(StockGoods goods);
    boolean updateGoods(StockGoods goods);

    /**
     * 出入库（带加权平均成本计算）
     */
    boolean stockIn(StockIo io);
    boolean stockOut(StockIo io);

    BigDecimal calculateAvgPrice(Long goodsId, Long warehouseId);

    List<Map<String, Object>> stockSummary();
}
