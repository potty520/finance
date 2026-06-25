package com.finance.module.ledger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.response.Result;
import com.finance.module.ledger.entity.GlCashFlowItem;
import com.finance.module.ledger.mapper.GlCashFlowItemMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 现金流量项
 */
@RestController
@RequestMapping("/ledger/cashFlow")
public class GlCashFlowController {

    @Resource
    private GlCashFlowItemMapper cashFlowMapper;

    @GetMapping("/list")
    public Result<List<GlCashFlowItem>> list() {
        return Result.success(cashFlowMapper.selectList(
                new LambdaQueryWrapper<GlCashFlowItem>().orderByAsc(GlCashFlowItem::getItemCode)));
    }

    @GetMapping("/tree")
    public Result<List<GlCashFlowItem>> tree() {
        List<GlCashFlowItem> all = cashFlowMapper.selectList(
                new LambdaQueryWrapper<GlCashFlowItem>().orderByAsc(GlCashFlowItem::getItemCode));
        return Result.success(all);
    }

    @GetMapping("/{id}")
    public Result<GlCashFlowItem> get(@PathVariable Long id) {
        return Result.success(cashFlowMapper.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody GlCashFlowItem item) {
        if (item.getStatus() == null) item.setStatus(1);
        return Result.success(cashFlowMapper.insert(item) > 0);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody GlCashFlowItem item) {
        return Result.success(cashFlowMapper.updateById(item) > 0);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(cashFlowMapper.deleteById(id) > 0);
    }
}
