package com.finance.module.cost.service;

import com.finance.module.cost.entity.CostAllocation;
import com.finance.module.cost.entity.CostProduct;

import java.util.List;
import java.util.Map;

public interface ICostService {
    boolean allocate(CostAllocation allocation);
    /**
     * 品种法计算产品成本
     */
    Map<String, Object> calculateProductCost(String year, Integer period);
    List<Map<String, Object>> productSummary(String year, Integer period);
}
