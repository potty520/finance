package com.finance.module.system.controller;

import com.finance.common.exception.BusinessException;
import com.finance.common.util.MenuTreeUtil;
import com.finance.common.response.Result;
import com.finance.common.response.ResultCode;
import com.finance.module.system.entity.SysMenu;
import com.finance.module.system.entity.SysUser;
import com.finance.module.system.mapper.SysUserMapper;
import com.finance.module.system.service.ISysUserService;
import com.finance.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private JwtUtil jwtUtil;

    @Resource
    private ISysUserService userService;

    @Resource
    private SysUserMapper userMapper;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, Object> body) {
        Object usernameObj = body.get("username");
        Object passwordObj = body.get("password");
        if (!(usernameObj instanceof String) || ((String) usernameObj).trim().isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "用户名不能为空");
        }
        if (!(passwordObj instanceof String) || ((String) passwordObj).isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "密码不能为空");
        }
        String username = (String) usernameObj;
        String password = (String) passwordObj;
        if (username.length() > 100 || password.length() > 100) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "用户名或密码长度不能超过100字符");
        }
        return Result.success(userService.login(username, password));
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        // 从请求头获取 token 并加入黑名单
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            jwtUtil.blacklistToken(token);
        }
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
