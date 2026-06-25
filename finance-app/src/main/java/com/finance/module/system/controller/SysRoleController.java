package com.finance.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.response.Result;
import com.finance.module.system.entity.SysRole;
import com.finance.module.system.service.ISysRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    @Resource
    private ISysRoleService roleService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<List<SysRole>> list() {
        return Result.success(roleService.list(new LambdaQueryWrapper<SysRole>()
                .orderByAsc(SysRole::getSortOrder)));
    }

    @GetMapping("/all")
    public Result<List<SysRole>> all() {
        return Result.success(roleService.list(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getStatus, 1)
                .orderByAsc(SysRole::getSortOrder)));
    }

    @GetMapping("/{id}")
    public Result<SysRole> get(@PathVariable Long id) {
        return Result.success(roleService.getById(id));
    }

    @GetMapping("/{id}/menus")
    public Result<List<Long>> getMenus(@PathVariable Long id) {
        return Result.success(roleService.getMenuIdsByRoleId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:role:add')")
    public Result<Boolean> add(@RequestBody SysRole role) {
        return Result.success(roleService.saveRoleWithMenus(role, null));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Boolean> edit(@RequestBody SysRole role) {
        return Result.success(roleService.updateRoleWithMenus(role, null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        if (id != null && id == 1L) {
            return Result.error("系统管理员角色不可删除");
        }
        return Result.success(roleService.removeById(id));
    }
}
