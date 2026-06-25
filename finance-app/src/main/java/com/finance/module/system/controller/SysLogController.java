package com.finance.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.system.entity.SysLogLogin;
import com.finance.module.system.entity.SysLogOperation;
import com.finance.module.system.mapper.SysLogLoginMapper;
import com.finance.module.system.mapper.SysLogOperationMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/log")
public class SysLogController {

    @Resource
    private SysLogLoginMapper loginLogMapper;

    @Resource
    private SysLogOperationMapper opLogMapper;

    @GetMapping("/login/page")
    public Result<PageResult<SysLogLogin>> loginPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<SysLogLogin> qw = new LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) qw.like(SysLogLogin::getUsername, username);
        if (status != null) qw.eq(SysLogLogin::getStatus, status);
        qw.orderByDesc(SysLogLogin::getLoginTime);
        Page<SysLogLogin> page = loginLogMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(page));
    }

    @GetMapping("/operation/page")
    public Result<PageResult<SysLogOperation>> opPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String username) {
        LambdaQueryWrapper<SysLogOperation> qw = new LambdaQueryWrapper<>();
        if (module != null && !module.isEmpty()) qw.like(SysLogOperation::getModule, module);
        if (username != null && !username.isEmpty()) qw.like(SysLogOperation::getUsername, username);
        qw.orderByDesc(SysLogOperation::getCreateTime);
        Page<SysLogOperation> page = opLogMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(page));
    }

    @DeleteMapping("/login/clear")
    public Result<Boolean> clearLogin() {
        return Result.success(loginLogMapper.delete(null) > 0);
    }

    @DeleteMapping("/operation/clear")
    public Result<Boolean> clearOp() {
        return Result.success(opLogMapper.delete(null) > 0);
    }
}
