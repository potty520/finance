package com.finance.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.response.Result;
import com.finance.common.util.MenuTreeUtil;
import com.finance.module.system.entity.SysMenu;
import com.finance.module.system.mapper.SysMenuMapper;
import com.finance.module.system.mapper.SysUserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    @Resource
    private SysMenuMapper menuMapper;

    @Resource
    private SysUserMapper userMapper;

    @GetMapping("/list")
    public Result<List<SysMenu>> list() {
        return Result.success(menuMapper.selectList(null));
    }

    @GetMapping("/tree")
    public Result<List<SysMenu>> tree() {
        List<SysMenu> all = menuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSortOrder));
        return Result.success(MenuTreeUtil.buildTree(MenuTreeUtil.filterNavMenus(all), 0L));
    }

    @GetMapping("/userTree/{userId}")
    public Result<List<SysMenu>> userTree(@PathVariable Long userId) {
        List<SysMenu> menus = MenuTreeUtil.filterNavMenus(userMapper.selectMenusByUserId(userId));
        return Result.success(MenuTreeUtil.buildTree(menus, 0L));
    }

    @GetMapping("/{id}")
    public Result<SysMenu> get(@PathVariable Long id) {
        return Result.success(menuMapper.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody SysMenu menu) {
        return Result.success(menuMapper.insert(menu) > 0);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody SysMenu menu) {
        return Result.success(menuMapper.updateById(menu) > 0);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(menuMapper.deleteById(id) > 0);
    }
}
