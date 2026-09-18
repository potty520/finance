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
import com.finance.common.service.CurrentUserResolver;
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

    @Resource
    private CurrentUserResolver currentUser;

    /** 登录失败限流：key -> [失败次数, 锁定截止时间戳ms] */
    private static final java.util.concurrent.ConcurrentMap<String, long[]> LOGIN_FAILS = new java.util.concurrent.ConcurrentHashMap<>();
    private static final int MAX_LOGIN_FAILS = 5;
    private static final long LOCK_MILLIS = 5 * 60 * 1000L;

    @Override
    public Map<String, Object> login(String username, String password) {
        String failKey = username + "|" + clientIpSafe();
        long now = System.currentTimeMillis();
        long[] rec = LOGIN_FAILS.get(failKey);
        if (rec != null && rec[0] >= MAX_LOGIN_FAILS && now < rec[1]) {
            long left = (rec[1] - now) / 1000;
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),
                    "失败次数过多，账号已临时锁定，请 " + Math.max(left, 1) + " 秒后再试");
        }
        SysUser user = baseMapper.selectByUsername(username);
        if (user == null) {
            recordLoginFail(failKey, now);
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户名或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            recordLoginFail(failKey, now);
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        LOGIN_FAILS.remove(failKey);
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
            saveUserRoles(user.getId(), roleIds);
        }
        return ok;
    }

    private void saveUserRoles(Long userId, List<Long> roleIds) {
        baseMapper.deleteUserRoles(userId);
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                if (roleId != null) baseMapper.insertUserRole(userId, roleId);
            }
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long userId) {
        if (userId == null) throw new BusinessException("缺少用户ID");
        SysUser target = getById(userId);
        if (target == null) return true;
        if (userId.equals(currentUser.currentId())) {
            throw new BusinessException("不能删除当前登录用户");
        }
        if ("admin".equals(target.getUsername())) {
            throw new BusinessException("系统内置管理员不可删除，如需停用请编辑其状态");
        }
        List<String> roles = baseMapper.selectRoleCodesByUserId(userId);
        boolean isAdmin = roles != null && roles.stream().anyMatch(r -> "ADMIN".equalsIgnoreCase(r));
        if (isAdmin) {
            Long active = baseMapper.countActiveAdmins();
            if (active != null && active <= 1) {
                throw new BusinessException("系统至少保留一名启用状态的管理员");
            }
        }
        baseMapper.deleteUserRoles(userId);
        return removeById(userId);
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        validatePasswordStrength(newPassword);
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
        // 只信任 nginx 写入的 X-Real-IP，避免客户端伪造 X-Forwarded-For
        String ip = request.getHeader("X-Real-IP");
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String clientIpSafe() {
        HttpServletRequest req = CommonUtil.getCurrentRequest();
        return req == null ? "-" : getClientIp(req);
    }

    private void recordLoginFail(String key, long now) {
        LOGIN_FAILS.compute(key, (k, v) -> {
            if (v == null || now >= v[1]) {
                return new long[]{1, now + LOCK_MILLIS};
            }
            v[0] += 1;
            v[1] = now + LOCK_MILLIS;
            return v;
        });
    }

    /** 密码复杂度：至少 8 位，且同时包含字母和数字 */
    private void validatePasswordStrength(String pwd) {
        if (pwd == null || pwd.length() < 8
                || !pwd.matches(".*[A-Za-z].*") || !pwd.matches(".*\\d.*")) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),
                    "密码强度不足：至少 8 位且同时包含字母和数字");
        }
    }
}
