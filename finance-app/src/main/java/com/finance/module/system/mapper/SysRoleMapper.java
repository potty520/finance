package com.finance.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.system.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<String> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
