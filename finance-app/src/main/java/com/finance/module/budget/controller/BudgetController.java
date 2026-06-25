package com.finance.module.budget.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.budget.entity.Budget;
import com.finance.module.budget.mapper.BudgetMapper;
import com.finance.module.budget.service.IBudgetService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    @Resource private IBudgetService budgetService;
    @Resource private BudgetMapper budgetMapper;

    @GetMapping("/page")
    public Result<PageResult<Budget>> page(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String fiscalYear,
            @RequestParam(required = false) Integer fiscalPeriod,
            @RequestParam(required = false) String status) {
        LambdaQueryWrapper<Budget> qw = new LambdaQueryWrapper<>();
        if (fiscalYear != null && !fiscalYear.isEmpty()) qw.eq(Budget::getFiscalYear, Integer.valueOf(fiscalYear));
        if (fiscalPeriod != null) qw.eq(Budget::getFiscalPeriod, fiscalPeriod);
        if (status != null && !status.isEmpty()) qw.eq(Budget::getStatus, Integer.valueOf(status));
        qw.eq(Budget::getDeleted, 0).orderByDesc(Budget::getCreateTime);
        Page<Budget> p = budgetMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        for (Budget row : p.getRecords()) {
            if (row.getBudgetNo() == null) row.setBudgetNo("BD-" + row.getId());
            if (row.getAvailableAmount() == null && row.getAmount() != null) {
                row.setAvailableAmount(row.getAmount().subtract(
                        row.getUsedAmount() == null ? java.math.BigDecimal.ZERO : row.getUsedAmount()));
            }
            // 填充科目名称（默认显示科目编码，避免前端空白）
            if (row.getSubjectName() == null || row.getSubjectName().isEmpty()) {
                row.setSubjectName(row.getSubjectCode());
            }
            // 填充部门名称
            if (row.getDeptName() == null || row.getDeptName().isEmpty()) {
                row.setDeptName(row.getDeptId() != null ? String.valueOf(row.getDeptId()) : null);
            }
        }
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/analysis")
    public Result<List<Map<String, Object>>> analysis(
            @RequestParam String fiscalYear,
            @RequestParam Integer fiscalPeriod) {
        return Result.success(budgetService.analysis(fiscalYear, fiscalPeriod));
    }

    @GetMapping("/detail/{id}")
    public Result<Budget> get(@PathVariable Long id) {
        return Result.success(budgetMapper.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody Budget b) {
        return Result.success(budgetService.saveBudget(b));
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody Budget b) {
        return Result.success(budgetService.updateBudget(b));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(budgetMapper.deleteById(id) > 0);
    }

    @PostMapping("/audit")
    public Result<Boolean> audit(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        boolean pass = Boolean.parseBoolean(body.get("pass").toString());
        return Result.success(budgetService.auditBudget(id, pass));
    }

    @PostMapping("/issue/{id}")
    public Result<Boolean> issue(@PathVariable Long id) {
        return Result.success(budgetService.issueBudget(id));
    }
}
