package com.finance.module.cost.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.cost.entity.*;
import com.finance.module.cost.mapper.*;
import com.finance.module.cost.service.ICostService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cost")
public class CostController {

    @Resource private ICostService costService;
    @Resource private CostCenterMapper centerMapper;
    @Resource private CostItemMapper itemMapper;
    @Resource private CostAllocationMapper allocationMapper;
    @Resource private CostProductMapper productMapper;

    @GetMapping("/center/list")
    public Result<List<CostCenter>> centerList() {
        return Result.success(centerMapper.selectList(
                new LambdaQueryWrapper<CostCenter>().orderByAsc(CostCenter::getCenterCode)));
    }

    @GetMapping("/center/page")
    public Result<PageResult<CostCenter>> centerPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize) {
        Page<CostCenter> p = centerMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)),
                new LambdaQueryWrapper<CostCenter>().orderByAsc(CostCenter::getCenterCode));
        return Result.success(CommonUtil.toPageResult(p));
    }

    @PostMapping("/center")
    public Result<Boolean> addCenter(@RequestBody CostCenter c) {
        if (c.getStatus() == null) c.setStatus(1);
        return Result.success(centerMapper.insert(c) > 0);
    }

    @PutMapping("/center")
    public Result<Boolean> editCenter(@RequestBody CostCenter c) {
        return Result.success(centerMapper.updateById(c) > 0);
    }

    @DeleteMapping("/center/{id}")
    public Result<Boolean> delCenter(@PathVariable Long id) {
        return Result.success(centerMapper.deleteById(id) > 0);
    }

    @GetMapping("/item/list")
    public Result<List<CostItem>> itemList() {
        return Result.success(itemMapper.selectList(
                new LambdaQueryWrapper<CostItem>().eq(CostItem::getStatus, 1)));
    }

    @PostMapping("/item")
    public Result<Boolean> addItem(@RequestBody CostItem i) {
        if (i.getStatus() == null) i.setStatus(1);
        return Result.success(itemMapper.insert(i) > 0);
    }

    @PutMapping("/item")
    public Result<Boolean> editItem(@RequestBody CostItem i) {
        return Result.success(itemMapper.updateById(i) > 0);
    }

    @DeleteMapping("/item/{id}")
    public Result<Boolean> delItem(@PathVariable Long id) {
        return Result.success(itemMapper.deleteById(id) > 0);
    }

    @PostMapping("/allocation")
    public Result<Boolean> allocate(@RequestBody CostAllocation allocation) {
        return Result.success(costService.allocate(allocation));
    }

    @GetMapping("/allocation/page")
    public Result<PageResult<CostAllocation>> allocationPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize) {
        Page<CostAllocation> p = allocationMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)),
                new LambdaQueryWrapper<CostAllocation>().orderByDesc(CostAllocation::getCreateTime));
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/allocation/summary")
    public Result<List<Map<String, Object>>> allocationSummary(
            @RequestParam String fiscalYear,
            @RequestParam Integer fiscalPeriod) {
        return Result.success(allocationMapper.sumByCenter(fiscalYear, fiscalPeriod));
    }

    @PostMapping("/product/calculate")
    public Result<Map<String, Object>> calculateProduct(@RequestBody Map<String, Object> body) {
        String year = (String) body.get("fiscalYear");
        Integer period = Integer.valueOf(body.get("fiscalPeriod").toString());
        return Result.success(costService.calculateProductCost(year, period));
    }

    @GetMapping("/product/summary")
    public Result<List<Map<String, Object>>> productSummary(
            @RequestParam String fiscalYear,
            @RequestParam Integer fiscalPeriod) {
        return Result.success(costService.productSummary(fiscalYear, fiscalPeriod));
    }
}
