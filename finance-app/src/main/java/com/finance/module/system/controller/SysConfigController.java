package com.finance.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.response.Result;
import com.finance.module.system.entity.SysConfig;
import com.finance.module.system.mapper.SysConfigMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/config")
public class SysConfigController {

    @Resource
    private SysConfigMapper configMapper;

    @GetMapping("/list")
    public Result<List<SysConfig>> list() {
        return Result.success(configMapper.selectList(
                new LambdaQueryWrapper<SysConfig>().orderByAsc(SysConfig::getConfigKey)));
    }

    @GetMapping("/{id}")
    public Result<SysConfig> get(@PathVariable Long id) {
        return Result.success(configMapper.selectById(id));
    }

    @GetMapping("/key/{configKey}")
    public Result<SysConfig> getByKey(@PathVariable String configKey) {
        return Result.success(configMapper.selectOne(
                new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, configKey)));
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody SysConfig config) {
        return Result.success(configMapper.updateById(config) > 0);
    }
}
