package com.finance.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.response.Result;
import com.finance.module.system.entity.SysCompany;
import com.finance.module.system.mapper.SysCompanyMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/company")
public class SysCompanyController {

    @Resource
    private SysCompanyMapper companyMapper;

    @GetMapping("/list")
    public Result<List<SysCompany>> list() {
        return Result.success(companyMapper.selectList(
                new LambdaQueryWrapper<SysCompany>()
                        .orderByAsc(SysCompany::getCompanyCode)));
    }

    @GetMapping("/{id}")
    public Result<SysCompany> get(@PathVariable Long id) {
        return Result.success(companyMapper.selectById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody SysCompany company) {
        if (company.getStatus() == null) company.setStatus(1);
        if (company.getId() != null) {
            return Result.success(companyMapper.updateById(company) > 0);
        }
        return Result.success(companyMapper.insert(company) > 0);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody SysCompany company) {
        return Result.success(companyMapper.updateById(company) > 0);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(companyMapper.deleteById(id) > 0);
    }
}
