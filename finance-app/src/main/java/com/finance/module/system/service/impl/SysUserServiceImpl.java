package com.finance.module.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.common.exception.BusinessException;
import com.finance.common.response.PageResult;
import com.finance.common.response.ResultCode;
import com.finance.common.util.CommonUtil;
import com.finance.common.util.MenuTreeUtil;
import com.finance.module.system.entity.SysMenu;
import com.finance.module.system.entity.SysUser;
import com.finance.module.system.mapper.SysUserMapper;
import com.finance.module.system.service.ISysUserService;
import com.finance.security.JwtUtil;
import com.finance.security.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> login(String username, String password) {
        SysUser user = baseMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户名或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        // 查询角色与权限
        List<String> roles = baseMapper.selectRoleCodesByUserId(user.getId());
        List<String> perms = baseMapper.selectPermCodesByUserId(user.getId());

        // 查询菜单
        List<SysMenu> allMenus = baseMapper.selectMenusByUserId(user.getId());
        List<SysMenu> menus = MenuTreeUtil.buildTree(MenuTreeUtil.filterNavMenus(allMenus), 0L);

        // 生成 token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 更新最后登录
        HttpServletRequest req = CommonUtil.getCurrentRequest();
        user.setLastLogin(LocalDateTime.now());
        if (req != null) {
            user.setLastIp(getClientIp(req));
        }
        user.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(user);

        // 返回登录信息
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("avatar", user.getAvatar());
        result.put("deptId", user.getDeptId());
        result.put("deptName", user.getDeptName());
        result.put("roles", roles);
        result.put("permissions", perms);
        result.put("menus", menus);
        return result;
    }

    @Override
    public void logout() {
        // 简化：实际项目应将 token 加入黑名单
    }

    @Override
    public LoginUser getLoginUserByUsername(String username) {
        SysUser user = baseMapper.selectByUsername(username);
        if (user == null) return null;
        List<String> roles = baseMapper.selectRoleCodesByUserId(user.getId());
        List<String> perms = baseMapper.selectPermCodesByUserId(user.getId());
        return LoginUser.fromUser(user, roles, perms);
    }

    @Override
    public PageResult<SysUser> pageQuery(Long pageNum, Long pageSize, String username, String realName, Long deptId, Integer status) {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(username)) qw.like(SysUser::getUsername, username);
        if (StrUtil.isNotBlank(realName)) qw.like(SysUser::getRealName, realName);
        if (deptId != null) qw.eq(SysUser::getDeptId, deptId);
        if (status != null) qw.eq(SysUser::getStatus, status);
        qw.orderByDesc(SysUser::getCreateTime);
        IPage<SysUser> page = page(new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        // 清理密码字段
        page.getRecords().forEach(u -> u.setPassword(null));
        return CommonUtil.toPageResult(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUser(SysUser user, List<Long> roleIds) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "用户名和密码不能为空");
        }
        // 校验用户名唯一
        SysUser exist = baseMapper.selectByUsername(user.getUsername());
        if (exist != null) {
            throw new BusinessException(ResultCode.DATA_EXISTED);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getStatus() == null) user.setStatus(1);
        boolean ok = save(user);
        if (ok && roleIds != null) {
            saveUserRoles(user.getId(), roleIds);
        }
        return ok;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user, List<Long> roleIds) {
        if (user.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        boolean ok = updateById(user);
        if (ok && roleIds != null) {
            // 简化：实际应通过专门的 UserRoleMapper 处理
        }
        return ok;
    }

    private void saveUserRoles(Long userId, List<Long> roleIds) {
        // 简化：实际应通过专门的 UserRoleMapper 处理
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        return updateById(user);
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        SysUser user = getById(userId);
        if (user == null) throw new BusinessException(ResultCode.USER_NOT_EXIST);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        return resetPassword(userId, newPassword);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
