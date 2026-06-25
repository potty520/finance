package com.finance.module.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import com.finance.module.stock.entity.StockBalance;
import com.finance.module.stock.entity.StockGoods;
import com.finance.module.stock.entity.StockIo;
import com.finance.module.stock.mapper.StockBalanceMapper;
import com.finance.module.stock.mapper.StockGoodsMapper;
import com.finance.module.stock.mapper.StockIoMapper;
import com.finance.module.stock.service.IStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockServiceImpl implements IStockService {

    @Resource private StockGoodsMapper goodsMapper;
    @Resource private StockIoMapper ioMapper;
    @Resource private StockBalanceMapper balanceMapper;

    @Override
    public boolean saveGoods(StockGoods goods) {
        if (goods.getItemCode() == null || goods.getItemCode().isEmpty()) {
            goods.setItemCode("GD" + System.currentTimeMillis());
        }
        if (goods.getStatus() == null) goods.setStatus(1);
        if (goods.getCostMethod() == null) goods.setCostMethod("WAM");
        return goodsMapper.insert(goods) > 0;
    }

    @Override
    public boolean updateGoods(StockGoods goods) {
        return goodsMapper.updateById(goods) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean stockIn(StockIo io) {
        StockGoods g = goodsMapper.selectById(io.getGoodsId());
        if (g == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (io.getStatus() == null) io.setStatus("A");
        if (io.getBillNo() == null) io.setBillNo("IN-" + System.currentTimeMillis());

        BigDecimal oldQty = getEndQuantity(io.getGoodsId(), io.getWarehouseId());
        BigDecimal oldAmount = getEndAmount(io.getGoodsId(), io.getWarehouseId());
        BigDecimal inQty = io.getQuantity() == null ? BigDecimal.ZERO : io.getQuantity();
        BigDecimal inAmount = io.getAmount() == null ? BigDecimal.ZERO : io.getAmount();
        BigDecimal newQty = oldQty.add(inQty);
        BigDecimal newAmount = oldAmount.add(inAmount);
        BigDecimal avgPrice = newQty.compareTo(BigDecimal.ZERO) > 0
                ? newAmount.divide(newQty, 4, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        // 更新库存余额
        io.setTotalCost(inAmount);
        boolean ok = ioMapper.insert(io) > 0;
        if (ok) updateBalance(io.getGoodsId(), io.getWarehouseId(), inQty, inAmount, true);
        return ok;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean stockOut(StockIo io) {
        StockGoods g = goodsMapper.selectById(io.getGoodsId());
        if (g == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (io.getStatus() == null) io.setStatus("A");
        if (io.getBillNo() == null) io.setBillNo("OUT-" + System.currentTimeMillis());

        BigDecimal outQty = io.getQuantity() == null ? BigDecimal.ZERO : io.getQuantity();
        BigDecimal curQty = getEndQuantity(io.getGoodsId(), io.getWarehouseId());
        if (curQty.compareTo(outQty) < 0) {
            throw new BusinessException(ResultCode.INSUFFICIENT_STOCK);
        }
        // 加权平均成本
        BigDecimal avgPrice = g.getCostMethod() != null ? BigDecimal.ZERO : BigDecimal.ZERO;
        StockBalance balance = findBalance(io.getGoodsId(), io.getWarehouseId());
        if (balance != null && balance.getAvgPrice() != null) avgPrice = balance.getAvgPrice();
        BigDecimal totalCost = outQty.multiply(avgPrice).setScale(2, RoundingMode.HALF_UP);
        io.setPrice(avgPrice);
        io.setTotalCost(totalCost);
        io.setAmount(totalCost);
        boolean ok = ioMapper.insert(io) > 0;
        if (ok) updateBalance(io.getGoodsId(), io.getWarehouseId(), outQty, totalCost, false);
        return ok;
    }

    @Override
    public BigDecimal calculateAvgPrice(Long goodsId, Long warehouseId) {
        return getEndAmount(goodsId, warehouseId)
                .divide(getEndQuantity(goodsId, warehouseId), 4, RoundingMode.HALF_UP);
    }

    @Override
    public List<Map<String, Object>> stockSummary() {
        String periodCode = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        List<StockGoods> goodsList = goodsMapper.selectList(
                new LambdaQueryWrapper<StockGoods>()
                        .eq(StockGoods::getStatus, 1)
                        .eq(StockGoods::getDeleted, 0));
        List<Map<String, Object>> rows = new ArrayList<>();
        for (StockGoods g : goodsList) {
            StockBalance b = balanceMapper.selectOne(new LambdaQueryWrapper<StockBalance>()
                    .eq(StockBalance::getGoodsId, g.getId())
                    .eq(StockBalance::getPeriodCode, periodCode)
                    .last("LIMIT 1"));
            Map<String, Object> row = new HashMap<>();
            row.put("goodsCode", g.getItemCode());
            row.put("goodsName", g.getItemName());
            row.put("categoryName", "");
            row.put("beginQuantity", b != null && b.getBeginQuantity() != null ? b.getBeginQuantity() : BigDecimal.ZERO);
            row.put("inQuantity", b != null && b.getInQuantity() != null ? b.getInQuantity() : BigDecimal.ZERO);
            row.put("outQuantity", b != null && b.getOutQuantity() != null ? b.getOutQuantity() : BigDecimal.ZERO);
            row.put("endQuantity", b != null && b.getEndQuantity() != null ? b.getEndQuantity() : BigDecimal.ZERO);
            row.put("endAmount", b != null && b.getEndAmount() != null ? b.getEndAmount() : BigDecimal.ZERO);
            row.put("avgPrice", b != null && b.getAvgPrice() != null ? b.getAvgPrice() : BigDecimal.ZERO);
            rows.add(row);
        }
        return rows;
    }

    private StockBalance findBalance(Long goodsId, Long warehouseId) {
        String periodCode = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        LambdaQueryWrapper<StockBalance> qw = new LambdaQueryWrapper<StockBalance>()
                .eq(StockBalance::getGoodsId, goodsId)
                .eq(StockBalance::getPeriodCode, periodCode);
        if (warehouseId != null) {
            qw.eq(StockBalance::getWarehouseId, warehouseId);
        }
        qw.last("LIMIT 1");
        return balanceMapper.selectOne(qw);
    }

    private BigDecimal getEndQuantity(Long goodsId, Long warehouseId) {
        StockBalance b = findBalance(goodsId, warehouseId);
        return b == null || b.getEndQuantity() == null ? BigDecimal.ZERO : b.getEndQuantity();
    }

    private BigDecimal getEndAmount(Long goodsId, Long warehouseId) {
        StockBalance b = findBalance(goodsId, warehouseId);
        return b == null || b.getEndAmount() == null ? BigDecimal.ZERO : b.getEndAmount();
    }

    private void updateBalance(Long goodsId, Long warehouseId, BigDecimal qty, BigDecimal amount, boolean isIn) {
        String periodCode = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        StockBalance b = findBalance(goodsId, warehouseId);
        if (b == null) {
            b = new StockBalance();
            b.setGoodsId(goodsId);
            b.setWarehouseId(warehouseId);
            b.setPeriodCode(periodCode);
            b.setBeginQuantity(BigDecimal.ZERO);
            b.setBeginAmount(BigDecimal.ZERO);
            b.setInQuantity(BigDecimal.ZERO);
            b.setInAmount(BigDecimal.ZERO);
            b.setOutQuantity(BigDecimal.ZERO);
            b.setOutAmount(BigDecimal.ZERO);
            b.setEndQuantity(BigDecimal.ZERO);
            b.setEndAmount(BigDecimal.ZERO);
        }
        if (isIn) {
            b.setInQuantity((b.getInQuantity() == null ? BigDecimal.ZERO : b.getInQuantity()).add(qty));
            b.setInAmount((b.getInAmount() == null ? BigDecimal.ZERO : b.getInAmount()).add(amount));
        } else {
            b.setOutQuantity((b.getOutQuantity() == null ? BigDecimal.ZERO : b.getOutQuantity()).add(qty));
            b.setOutAmount((b.getOutAmount() == null ? BigDecimal.ZERO : b.getOutAmount()).add(amount));
        }
        b.setEndQuantity(b.getBeginQuantity().add(b.getInQuantity()).subtract(b.getOutQuantity()));
        b.setEndAmount(b.getBeginAmount().add(b.getInAmount()).subtract(b.getOutAmount()));
        if (b.getEndQuantity().compareTo(BigDecimal.ZERO) > 0) {
            b.setAvgPrice(b.getEndAmount().divide(b.getEndQuantity(), 4, RoundingMode.HALF_UP));
        }
        if (b.getId() == null) {
            balanceMapper.insert(b);
        } else {
            balanceMapper.updateById(b);
        }
    }
}
