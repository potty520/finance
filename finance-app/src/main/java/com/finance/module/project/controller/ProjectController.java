package com.finance.module.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.project.entity.Project;
import com.finance.module.project.entity.ProjectBudget;
import com.finance.module.project.entity.ProjectLedger;
import com.finance.module.project.mapper.ProjectBudgetMapper;
import com.finance.module.project.mapper.ProjectLedgerMapper;
import com.finance.module.project.mapper.ProjectMapper;
import com.finance.module.project.service.IProjectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Resource private IProjectService projectService;
    @Resource private ProjectMapper projectMapper;
    @Resource private ProjectLedgerMapper ledgerMapper;
    @Resource private ProjectBudgetMapper projectBudgetMapper;

    @GetMapping("/page")
    public Result<PageResult<Project>> page(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String status) {
        LambdaQueryWrapper<Project> qw = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) qw.eq(Project::getStatus, status);
        qw.eq(Project::getDeleted, 0).orderByDesc(Project::getCreateTime);
        Page<Project> p = projectMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        for (Project row : p.getRecords()) {
            row.setUsedAmount(row.getActualAmount());
        }
        return Result.success(CommonUtil.toPageResult(p));
    }

    @PostMapping
    public Result<Project> save(@RequestBody Project p) {
        return Result.success(projectService.save(p));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> del(@PathVariable Long id) {
        return Result.success(projectMapper.deleteById(id) > 0);
    }

    @GetMapping("/ledger")
    public Result<List<ProjectLedger>> ledger(@RequestParam(required = false) Long projectId) {
        LambdaQueryWrapper<ProjectLedger> qw = new LambdaQueryWrapper<>();
        if (projectId != null) qw.eq(ProjectLedger::getProjectId, projectId);
        qw.orderByDesc(ProjectLedger::getHandleDate);
        return Result.success(ledgerMapper.selectList(qw));
    }

    @GetMapping("/budget/list")
    public Result<List<ProjectBudget>> budgetList(@RequestParam(required = false) Long projectId) {
        LambdaQueryWrapper<ProjectBudget> qw = new LambdaQueryWrapper<>();
        if (projectId != null) qw.eq(ProjectBudget::getProjectId, projectId);
        qw.eq(ProjectBudget::getDeleted, 0).orderByDesc(ProjectBudget::getCreateTime);
        return Result.success(projectBudgetMapper.selectList(qw));
    }

    @GetMapping("/stats")
    public Result<List<Map<String, Object>>> stats(@RequestParam(required = false) String status) {
        return Result.success(java.util.Collections.emptyList());
    }

    @GetMapping("/detail/{id}")
    public Result<Project> get(@PathVariable Long id) {
        return Result.success(projectMapper.selectById(id));
    }
}
