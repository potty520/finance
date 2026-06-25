package com.finance.module.system.controller;

import com.finance.common.exception.BusinessException;
import com.finance.common.util.MenuTreeUtil;
import com.finance.common.response.Result;
import com.finance.common.response.ResultCode;
import com.finance.module.system.entity.SysMenu;
import com.finance.module.system.entity.SysUser;
import com.finance.module.system.mapper.SysUserMapper;
import com.finance.module.system.service.ISysUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证 Controller（登录/退出/当前用户）
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private ISysUserService userService;

    @Resource
    private SysUserMapper userMapper;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        return Result.success(userService.login(username, password));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        userService.logout();
        return Result.success();
    }

    @GetMapping("/me")
    public Result<Map<String, Object>> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        String username = auth.getName();
        SysUser user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        List<String> roles = userMapper.selectRoleCodesByUserId(user.getId());
        List<String> perms = userMapper.selectPermCodesByUserId(user.getId());
        List<SysMenu> menus = MenuTreeUtil.buildTree(
                MenuTreeUtil.filterNavMenus(userMapper.selectMenusByUserId(user.getId())), 0L);
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("realName", user.getRealName());
        data.put("avatar", user.getAvatar());
        data.put("deptId", user.getDeptId());
        data.put("deptName", user.getDeptName());
        data.put("roles", roles);
        data.put("permissions", perms);
        data.put("menus", menus);
        return Result.success(data);
    }
}
