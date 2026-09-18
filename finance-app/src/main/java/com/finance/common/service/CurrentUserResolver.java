package com.finance.common.service;

import com.finance.module.system.entity.SysUser;
import com.finance.module.system.mapper.SysUserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 从 SecurityContext 解析当前登录用户，用于审计痕迹与操作人记录
 */
@Component
public class CurrentUserResolver {

    @Resource
    private SysUserMapper userMapper;

    /** 当前用户，未登录返回 null */
    public SysUser current() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return userMapper.selectByUsername(auth.getName());
    }

    /** 当前用户，未登录抛出异常 */
    public SysUser require() {
        SysUser u = current();
        if (u == null) {
            throw new com.finance.common.exception.BusinessException(
                    com.finance.common.response.ResultCode.UNAUTHORIZED);
        }
        return u;
    }

    public Long currentId() {
        SysUser u = current();
        return u == null ? null : u.getId();
    }

    public String currentName() {
        SysUser u = current();
        return u == null ? null : (u.getRealName() != null ? u.getRealName() : u.getUsername());
    }
}
