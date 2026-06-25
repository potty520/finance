package com.finance.module.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.module.system.entity.SysRole;
import com.finance.module.system.mapper.SysRoleMapper;
import com.finance.module.system.service.ISysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        // 简化实现
        return java.util.Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRoleWithMenus(SysRole role, List<Long> menuIds) {
        return save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleWithMenus(SysRole role, List<Long> menuIds) {
        return updateById(role);
    }
}
