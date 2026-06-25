package com.finance.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.response.Result;
import com.finance.module.system.entity.SysDept;
import com.finance.module.system.mapper.SysDeptMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/dept")
public class SysDeptController {

    @Resource
    private SysDeptMapper deptMapper;

    @GetMapping("/list")
    public Result<List<SysDept>> list() {
        return Result.success(deptMapper.selectList(
                new LambdaQueryWrapper<SysDept>().orderByAsc(SysDept::getSortOrder)));
    }

    @GetMapping("/tree")
    public Result<List<SysDept>> tree() {
        List<SysDept> all = deptMapper.selectList(
                new LambdaQueryWrapper<SysDept>().orderByAsc(SysDept::getSortOrder));
        return Result.success(buildTree(all, 0L));
    }

    private List<SysDept> buildTree(List<SysDept> all, Long parentId) {
        return all.stream()
                .filter(d -> parentId.equals(d.getParentId()))
                .peek(d -> d.setChildren(buildTree(all, d.getId())))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Result<SysDept> get(@PathVariable Long id) {
        return Result.success(deptMapper.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody SysDept dept) {
        if (dept.getParentId() == null) dept.setParentId(0L);
        return Result.success(deptMapper.insert(dept) > 0);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody SysDept dept) {
        return Result.success(deptMapper.updateById(dept) > 0);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(deptMapper.deleteById(id) > 0);
    }
}
