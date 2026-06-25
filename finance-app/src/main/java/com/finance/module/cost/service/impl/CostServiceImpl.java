package com.finance.module.cost.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.module.cost.entity.CostProduct;
import com.finance.module.cost.mapper.CostAllocationMapper;
import com.finance.module.cost.mapper.CostProductMapper;
import com.finance.module.cost.service.ICostService;
import com.finance.module.stock.entity.StockGoods;
import com.finance.module.stock.mapper.StockGoodsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CostServiceImpl implements ICostService {

    @Resource private CostAllocationMapper allocationMapper;
    @Resource private CostProductMapper productMapper;
    @Resource private StockGoodsMapper goodsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean allocate(com.finance.module.cost.entity.CostAllocation allocation) {
        if (allocation.getBillNo() == null) {
            allocation.setBillNo("AL-" + System.currentTimeMillis());
        }
        if (allocation.getStatus() == null) allocation.setStatus("A");
        allocation.setCreateTime(LocalDateTime.now());
        return allocationMapper.insert(allocation) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> calculateProductCost(String year, Integer period) {
        String periodCode = year + String.format("%02d", period);
        List<CostProduct> products = productMapper.selectList(
                new LambdaQueryWrapper<CostProduct>()
                        .eq(CostProduct::getPeriodCode, periodCode)
                        .eq(CostProduct::getDeleted, 0));
        BigDecimal grand = BigDecimal.ZERO;
        BigDecimal totalQty = BigDecimal.ZERO;
        for (CostProduct p : products) {
            BigDecimal total = (p.getDirectMaterial() == null ? BigDecimal.ZERO : p.getDirectMaterial())
                    .add(p.getDirectLabor() == null ? BigDecimal.ZERO : p.getDirectLabor())
                    .add(p.getManufactureOverhead() == null ? BigDecimal.ZERO : p.getManufactureOverhead());
            p.setTotalCost(total);
            if (p.getOutputQuantity() != null && p.getOutputQuantity().compareTo(BigDecimal.ZERO) > 0) {
                p.setUnitCost(total.divide(p.getOutputQuantity(), 4, RoundingMode.HALF_UP));
            }
            p.setStatus("POSTED");
            productMapper.updateById(p);
            grand = grand.add(total);
            totalQty = totalQty.add(p.getOutputQuantity() == null ? BigDecimal.ZERO : p.getOutputQuantity());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("totalCost", grand);
        result.put("totalQuantity", totalQty);
        result.put("count", products.size());
        return result;
    }

    @Override
    public List<Map<String, Object>> productSummary(String year, Integer period) {
        String periodCode = year + String.format("%02d", period);
        List<CostProduct> products = productMapper.selectList(
                new LambdaQueryWrapper<CostProduct>()
                        .eq(CostProduct::getPeriodCode, periodCode)
                        .eq(CostProduct::getDeleted, 0));
        List<Map<String, Object>> rows = new ArrayList<>();
        for (CostProduct p : products) {
            StockGoods goods = p.getProductItemId() == null ? null : goodsMapper.selectById(p.getProductItemId());
            Map<String, Object> row = new HashMap<>();
            row.put("productCode", goods != null ? goods.getItemCode() : String.valueOf(p.getProductItemId()));
            row.put("productName", goods != null ? goods.getItemName() : "-");
            row.put("outputQuantity", p.getOutputQuantity());
            row.put("directMaterial", p.getDirectMaterial());
            row.put("directLabor", p.getDirectLabor());
            row.put("totalCost", p.getTotalCost());
            row.put("unitCost", p.getUnitCost());
            rows.add(row);
        }
        return rows;
    }
}
