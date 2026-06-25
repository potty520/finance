package com.finance.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询
     */
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 查询用户的角色编码列表
     */
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的权限编码列表
     */
    List<String> selectPermCodesByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的菜单树
     */
    List<com.finance.module.system.entity.SysMenu> selectMenusByUserId(@Param("userId") Long userId);
}
