package com.finance.module.ledger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.response.Result;
import com.finance.module.ledger.entity.GlSubject;
import com.finance.module.ledger.mapper.GlSubjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 科目
 */
@RestController
@RequestMapping("/ledger/subject")
public class GlSubjectController {

    @Resource
    private GlSubjectMapper subjectMapper;

    @GetMapping("/list")
    public Result<List<GlSubject>> list() {
        return Result.success(subjectMapper.selectList(
                new LambdaQueryWrapper<GlSubject>()
                        .eq(GlSubject::getStatus, 1)
                        .orderByAsc(GlSubject::getSubjectCode)));
    }

    @GetMapping("/tree")
    public Result<List<GlSubject>> tree() {
        List<GlSubject> all = subjectMapper.selectList(
                new LambdaQueryWrapper<GlSubject>().orderByAsc(GlSubject::getSubjectCode));
        return Result.success(buildTree(all, 0L));
    }

    @GetMapping("/category/{category}")
    public Result<List<GlSubject>> byCategory(@PathVariable String category) {
        return Result.success(subjectMapper.selectByCategory(category));
    }

    @GetMapping("/{id}")
    public Result<GlSubject> get(@PathVariable Long id) {
        return Result.success(subjectMapper.selectById(id));
    }

    @GetMapping("/code/{code}")
    public Result<GlSubject> byCode(@PathVariable String code) {
        return Result.success(subjectMapper.selectByCode(code));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody GlSubject subject) {
        if (subject.getStatus() == null) subject.setStatus(1);
        return Result.success(subjectMapper.insert(subject) > 0);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody GlSubject subject) {
        return Result.success(subjectMapper.updateById(subject) > 0);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(subjectMapper.deleteById(id) > 0);
    }

    private List<GlSubject> buildTree(List<GlSubject> all, Long parentId) {
        return all.stream()
                .filter(s -> parentId.equals(s.getParentId()))
                .peek(s -> s.setChildren(buildTree(all, s.getId())))
                .collect(java.util.stream.Collectors.toList());
    }
}
