package com.finance.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.exception.BusinessException;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.response.ResultCode;
import com.finance.common.util.CommonUtil;
import com.finance.module.system.entity.SysUser;
import com.finance.module.system.service.ISysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController {

    @Resource
    private ISysUserService userService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<PageResult<SysUser>> page(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) Integer status) {
        return Result.success(userService.pageQuery(pageNum, pageSize, username, realName, deptId, status));
    }

    @GetMapping("/list")
    public Result<List<SysUser>> list() {
        List<SysUser> list = userService.list(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getStatus, 1)
                .orderByDesc(SysUser::getCreateTime));
        list.forEach(u -> u.setPassword(null));
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<SysUser> get(@PathVariable Long id) {
        SysUser u = userService.getById(id);
        if (u != null) u.setPassword(null);
        return Result.success(u);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<Boolean> add(@RequestBody Map<String, Object> body) {
        SysUser user = new SysUser();
        user.setUsername((String) body.get("username"));
        user.setPassword((String) body.get("password"));
        user.setRealName((String) body.get("realName"));
        user.setPhone((String) body.get("phone"));
        user.setEmail((String) body.get("email"));
        user.setGender(body.get("gender") != null ? ((Number) body.get("gender")).intValue() : null);
        user.setDeptId(body.get("deptId") != null ? ((Number) body.get("deptId")).longValue() : null);
        user.setStatus(body.get("status") != null ? ((Number) body.get("status")).intValue() : null);
        @SuppressWarnings("unchecked")
        List<Long> roleIds = (List<Long>) body.get("roleIds");
        return Result.success(userService.saveUser(user, roleIds));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Boolean> edit(@RequestBody Map<String, Object> body) {
        SysUser user = new SysUser();
        user.setId(body.get("id") != null ? ((Number) body.get("id")).longValue() : null);
        user.setUsername((String) body.get("username"));
        user.setPassword((String) body.get("password"));
        user.setRealName((String) body.get("realName"));
        user.setPhone((String) body.get("phone"));
        user.setEmail((String) body.get("email"));
        user.setGender(body.get("gender") != null ? ((Number) body.get("gender")).intValue() : null);
        user.setDeptId(body.get("deptId") != null ? ((Number) body.get("deptId")).longValue() : null);
        user.setStatus(body.get("status") != null ? ((Number) body.get("status")).intValue() : null);
        @SuppressWarnings("unchecked")
        List<Long> roleIds = (List<Long>) body.get("roleIds");
        return Result.success(userService.updateUser(user, roleIds));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        if (id != null && id == 1L) {
            throw new BusinessException("系统管理员不可删除");
        }
        return Result.success(userService.removeById(id));
    }

    @PostMapping("/resetPwd")
    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    public Result<Boolean> resetPwd(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        String pwd = body.get("password").toString();
        return Result.success(userService.resetPassword(id, pwd));
    }

    @PostMapping("/changePwd")
    public Result<Boolean> changePwd(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("userId").toString());
        String oldPwd = body.get("oldPassword").toString();
        String newPwd = body.get("newPassword").toString();
        return Result.success(userService.changePassword(id, oldPwd, newPwd));
    }
}
