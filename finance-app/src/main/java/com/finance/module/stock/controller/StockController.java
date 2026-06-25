package com.finance.module.stock.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.stock.entity.StockBalance;
import com.finance.module.stock.entity.StockGoods;
import com.finance.module.stock.entity.StockIo;
import com.finance.module.stock.entity.StockWarehouse;
import com.finance.module.stock.mapper.StockBalanceMapper;
import com.finance.module.stock.mapper.StockGoodsMapper;
import com.finance.module.stock.mapper.StockIoMapper;
import com.finance.module.stock.mapper.StockWarehouseMapper;
import com.finance.module.stock.service.IStockService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Resource private IStockService stockService;
    @Resource private StockGoodsMapper goodsMapper;
    @Resource private StockIoMapper ioMapper;
    @Resource private StockBalanceMapper balanceMapper;
    @Resource private StockWarehouseMapper warehouseMapper;

    // 物料
    @GetMapping("/goods/page")
    public Result<PageResult<StockGoods>> goodsPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryCode) {
        LambdaQueryWrapper<StockGoods> qw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            qw.and(w -> w.like(StockGoods::getItemCode, keyword).or().like(StockGoods::getItemName, keyword));
        }
        if (categoryCode != null && !categoryCode.isEmpty()) qw.eq(StockGoods::getCategoryId, categoryCode);
        qw.orderByAsc(StockGoods::getItemCode);
        Page<StockGoods> p = goodsMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/goods/list")
    public Result<List<StockGoods>> goodsList() {
        return Result.success(goodsMapper.selectList(
                new LambdaQueryWrapper<StockGoods>().eq(StockGoods::getStatus, 1)
                        .orderByAsc(StockGoods::getItemCode)));
    }

    @GetMapping("/goods/{id}")
    public Result<StockGoods> getGoods(@PathVariable Long id) {
        return Result.success(goodsMapper.selectById(id));
    }

    @PostMapping("/goods")
    public Result<Boolean> addGoods(@RequestBody StockGoods goods) {
        return Result.success(stockService.saveGoods(goods));
    }

    @PutMapping("/goods")
    public Result<Boolean> editGoods(@RequestBody StockGoods goods) {
        return Result.success(stockService.updateGoods(goods));
    }

    @DeleteMapping("/goods/{id}")
    public Result<Boolean> delGoods(@PathVariable Long id) {
        return Result.success(goodsMapper.deleteById(id) > 0);
    }

    // 出入库
    @PostMapping("/in")
    public Result<Boolean> stockIn(@RequestBody StockIo io) {
        return Result.success(stockService.stockIn(io));
    }

    @PostMapping("/out")
    public Result<Boolean> stockOut(@RequestBody StockIo io) {
        return Result.success(stockService.stockOut(io));
    }

    @GetMapping("/io/page")
    public Result<PageResult<StockIo>> ioPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String ioType,
            @RequestParam(required = false) Long goodsId) {
        LambdaQueryWrapper<StockIo> qw = new LambdaQueryWrapper<>();
        if (ioType != null && !ioType.isEmpty()) qw.eq(StockIo::getIoType, ioType);
        if (goodsId != null) qw.eq(StockIo::getGoodsId, goodsId);
        qw.orderByDesc(StockIo::getIoDate);
        Page<StockIo> p = ioMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    // 库存余额
    @GetMapping("/balance/list")
    public Result<List<StockBalance>> balanceList(@RequestParam(required = false) Long goodsId) {
        LambdaQueryWrapper<StockBalance> qw = new LambdaQueryWrapper<>();
        if (goodsId != null) qw.eq(StockBalance::getGoodsId, goodsId);
        return Result.success(balanceMapper.selectList(qw));
    }

    @GetMapping("/summary")
    public Result<List<Map<String, Object>>> summary() {
        return Result.success(stockService.stockSummary());
    }

    // 仓库
    @GetMapping("/warehouse/list")
    public Result<List<StockWarehouse>> warehouseList() {
        return Result.success(warehouseMapper.selectList(
                new LambdaQueryWrapper<StockWarehouse>().eq(StockWarehouse::getStatus, 1)));
    }

    @PostMapping("/warehouse")
    public Result<Boolean> addWarehouse(@RequestBody StockWarehouse w) {
        if (w.getStatus() == null) w.setStatus(1);
        return Result.success(warehouseMapper.insert(w) > 0);
    }

    @PutMapping("/warehouse")
    public Result<Boolean> editWarehouse(@RequestBody StockWarehouse w) {
        return Result.success(warehouseMapper.updateById(w) > 0);
    }

    @DeleteMapping("/warehouse/{id}")
    public Result<Boolean> delWarehouse(@PathVariable Long id) {
        return Result.success(warehouseMapper.deleteById(id) > 0);
    }
}
