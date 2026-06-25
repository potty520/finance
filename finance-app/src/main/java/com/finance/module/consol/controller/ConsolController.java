package com.finance.module.consol.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.consol.entity.ConsolGroup;
import com.finance.module.consol.entity.ConsolOffset;
import com.finance.module.consol.mapper.ConsolGroupMapper;
import com.finance.module.consol.mapper.ConsolOffsetMapper;
import com.finance.module.consol.service.IConsolService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/consol")
public class ConsolController {

    @Resource private IConsolService consolService;
    @Resource private ConsolGroupMapper groupMapper;
    @Resource private ConsolOffsetMapper offsetMapper;

    @GetMapping("/group/list")
    public Result<List<ConsolGroup>> groupList() {
        return Result.success(groupMapper.selectList(
                new LambdaQueryWrapper<ConsolGroup>().orderByAsc(ConsolGroup::getGroupCode)));
    }

    @PostMapping("/group")
    public Result<Boolean> addGroup(@RequestBody ConsolGroup g) {
        if (g.getStatus() == null) g.setStatus(1);
        return Result.success(groupMapper.insert(g) > 0);
    }

    @PutMapping("/group")
    public Result<Boolean> editGroup(@RequestBody ConsolGroup g) {
        return Result.success(groupMapper.updateById(g) > 0);
    }

    @DeleteMapping("/group/{id}")
    public Result<Boolean> delGroup(@PathVariable Long id) {
        return Result.success(groupMapper.deleteById(id) > 0);
    }

    @PostMapping("/offset")
    public Result<Boolean> addOffset(@RequestBody ConsolOffset offset) {
        return Result.success(consolService.addOffset(offset));
    }

    @GetMapping("/offset/page")
    public Result<PageResult<ConsolOffset>> offsetPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize) {
        Page<ConsolOffset> p = offsetMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)),
                new LambdaQueryWrapper<ConsolOffset>()
                        .eq(ConsolOffset::getDeleted, 0)
                        .orderByDesc(ConsolOffset::getId));
        return Result.success(CommonUtil.toPageResult(p));
    }

    @PostMapping("/generate")
    public Result<Boolean> generate(@RequestBody Map<String, Object> body) {
        String year = (String) body.get("fiscalYear");
        Integer period = Integer.valueOf(body.get("fiscalPeriod").toString());
        return Result.success(consolService.generate(year, period));
    }

    @GetMapping("/analysis")
    public Result<List<Map<String, Object>>> analysis(
            @RequestParam String fiscalYear,
            @RequestParam Integer fiscalPeriod,
            @RequestParam(required = false) String reportCode) {
        return Result.success(consolService.analysis(fiscalYear, fiscalPeriod, reportCode));
    }
}
