package com.finance.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
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
     * 查询用户的角色 ID 列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的权限编码列表
     */
    List<String> selectPermCodesByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的菜单树
     */
    List<com.finance.module.system.entity.SysMenu> selectMenusByUserId(@Param("userId") Long userId);

    /** 统计启用状态的管理员数量，用于防止删空管理员 */
    @Select("SELECT COUNT(DISTINCT u.id) FROM sys_user u "
            + "JOIN sys_user_role ur ON ur.user_id = u.id "
            + "JOIN sys_role r ON r.id = ur.role_id AND r.role_code = 'ADMIN' AND r.deleted = 0 "
            + "WHERE u.deleted = 0 AND u.status = 1")
    Long countActiveAdmins();

    /** 清空用户角色关联 */
    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId}")
    int deleteUserRoles(@Param("userId") Long userId);

    /** 写入用户角色关联 */
    @Insert("INSERT INTO sys_user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
