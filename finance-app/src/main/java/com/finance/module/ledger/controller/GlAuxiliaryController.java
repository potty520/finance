package com.finance.module.ledger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.ledger.entity.GlAuxiliary;
import com.finance.module.ledger.mapper.GlAuxiliaryMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 辅助核算
 */
@RestController
@RequestMapping("/ledger/auxiliary")
public class GlAuxiliaryController {

    @Resource
    private GlAuxiliaryMapper auxiliaryMapper;

    @GetMapping("/list")
    public Result<List<GlAuxiliary>> list(@RequestParam(required = false) String auxType) {
        LambdaQueryWrapper<GlAuxiliary> qw = new LambdaQueryWrapper<>();
        if (auxType != null && !auxType.isEmpty()) {
            qw.eq(GlAuxiliary::getTypeCode, auxType);
        }
        qw.orderByAsc(GlAuxiliary::getItemCode);
        return Result.success(auxiliaryMapper.selectList(qw));
    }

    @GetMapping("/page")
    public Result<PageResult<GlAuxiliary>> page(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String auxType,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<GlAuxiliary> qw = new LambdaQueryWrapper<>();
        if (auxType != null && !auxType.isEmpty()) qw.eq(GlAuxiliary::getTypeCode, auxType);
        if (keyword != null && !keyword.isEmpty()) {
            qw.and(w -> w.like(GlAuxiliary::getItemCode, keyword).or().like(GlAuxiliary::getItemName, keyword));
        }
        qw.orderByAsc(GlAuxiliary::getItemCode);
        Page<GlAuxiliary> p = auxiliaryMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/{id}")
    public Result<GlAuxiliary> get(@PathVariable Long id) {
        return Result.success(auxiliaryMapper.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody GlAuxiliary aux) {
        if (aux.getStatus() == null) aux.setStatus(1);
        return Result.success(auxiliaryMapper.insert(aux) > 0);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody GlAuxiliary aux) {
        return Result.success(auxiliaryMapper.updateById(aux) > 0);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(auxiliaryMapper.deleteById(id) > 0);
    }
}
