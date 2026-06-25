package com.finance.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.common.response.PageResult;
import com.finance.module.system.entity.SysUser;
import com.finance.security.LoginUser;

import java.util.List;
import java.util.Map;

/**
 * 用户服务
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 登录
     */
    Map<String, Object> login(String username, String password);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 根据用户名查询（含角色权限）
     */
    LoginUser getLoginUserByUsername(String username);

    /**
     * 分页查询
     */
    PageResult<SysUser> pageQuery(Long pageNum, Long pageSize, String username, String realName, Long deptId, Integer status);

    /**
     * 保存（含角色）
     */
    boolean saveUser(SysUser user, List<Long> roleIds);

    /**
     * 更新（含角色）
     */
    boolean updateUser(SysUser user, List<Long> roleIds);

    /**
     * 重置密码
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 修改自己的密码
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);
}
