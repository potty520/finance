package com.finance.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.module.system.entity.SysRole;

import java.util.List;

public interface ISysRoleService extends IService<SysRole> {

    List<Long> getMenuIdsByRoleId(Long roleId);

    boolean saveRoleWithMenus(SysRole role, List<Long> menuIds);

    boolean updateRoleWithMenus(SysRole role, List<Long> menuIds);
}
