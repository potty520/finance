package com.finance.common.util;

import com.finance.module.system.entity.SysMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单树构建
 */
public final class MenuTreeUtil {

    private MenuTreeUtil() {
    }

    public static List<SysMenu> buildTree(List<SysMenu> all, Long parentId) {
        return all.stream()
                .filter(m -> parentId.equals(m.getParentId() == null ? 0L : m.getParentId()))
                .peek(m -> m.setChildren(buildTree(all, m.getId())))
                .collect(Collectors.toList());
    }

    public static List<SysMenu> filterNavMenus(List<SysMenu> all) {
        List<SysMenu> result = new ArrayList<>();
        for (SysMenu menu : all) {
            if (menu.getStatus() != null && menu.getStatus() == 0) {
                continue;
            }
            if (menu.getVisible() != null && menu.getVisible() == 0) {
                continue;
            }
            if ("F".equalsIgnoreCase(menu.getMenuType())) {
                continue;
            }
            result.add(menu);
        }
        return result;
    }
}
