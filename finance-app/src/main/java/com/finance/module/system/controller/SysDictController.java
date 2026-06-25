package com.finance.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.response.Result;
import com.finance.module.system.entity.SysDictData;
import com.finance.module.system.entity.SysDictType;
import com.finance.module.system.mapper.SysDictDataMapper;
import com.finance.module.system.mapper.SysDictTypeMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/dict")
public class SysDictController {

    @Resource
    private SysDictTypeMapper dictTypeMapper;

    @Resource
    private SysDictDataMapper dictDataMapper;

    @GetMapping("/type/list")
    public Result<List<SysDictType>> typeList() {
        return Result.success(dictTypeMapper.selectList(null));
    }

    @GetMapping("/data/list")
    public Result<List<SysDictData>> dataList(@RequestParam(required = false) String dictType) {
        LambdaQueryWrapper<SysDictData> qw = new LambdaQueryWrapper<>();
        if (dictType != null && !dictType.isEmpty()) {
            qw.eq(SysDictData::getDictType, dictType);
        }
        qw.orderByAsc(SysDictData::getSortOrder);
        return Result.success(dictDataMapper.selectList(qw));
    }

    @GetMapping("/data/type/{dictType}")
    public Result<List<SysDictData>> byType(@PathVariable String dictType) {
        return Result.success(dictDataMapper.selectByDictType(dictType));
    }

    @PostMapping("/type")
    public Result<Boolean> addType(@RequestBody SysDictType type) {
        return Result.success(dictTypeMapper.insert(type) > 0);
    }

    @PutMapping("/type")
    public Result<Boolean> editType(@RequestBody SysDictType type) {
        return Result.success(dictTypeMapper.updateById(type) > 0);
    }

    @DeleteMapping("/type/{id}")
    public Result<Boolean> delType(@PathVariable Long id) {
        return Result.success(dictTypeMapper.deleteById(id) > 0);
    }

    @PostMapping("/data")
    public Result<Boolean> addData(@RequestBody SysDictData data) {
        return Result.success(dictDataMapper.insert(data) > 0);
    }

    @PutMapping("/data")
    public Result<Boolean> editData(@RequestBody SysDictData data) {
        return Result.success(dictDataMapper.updateById(data) > 0);
    }

    @DeleteMapping("/data/{id}")
    public Result<Boolean> delData(@PathVariable Long id) {
        return Result.success(dictDataMapper.deleteById(id) > 0);
    }
}
