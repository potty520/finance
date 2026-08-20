<script setup>
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { resolveMenuRoute, filterRouteMenus } from '@/utils/menuRoute'
import { getModuleColor } from '@/utils/moduleColor'
import { computed } from 'vue'

defineOptions({ name: 'SidebarMenuItem' })

const props = defineProps({
  menus: { type: Array, default: () => [] },
  parentPath: { type: String, default: '' },
  accent: { type: Object, default: null }
})

const visibleMenus = computed(() => filterRouteMenus(props.menus))

function iconComponent(name) {
  if (name && ElementPlusIconsVue[name]) return ElementPlusIconsVue[name]
  // 默认图标映射
  const map = {
    '总账管理': ElementPlusIconsVue.Notebook,
    '出纳管理': ElementPlusIconsVue.Wallet,
    '应收管理': ElementPlusIconsVue.Sell,
    '应付管理': ElementPlusIconsVue.ShoppingCart,
    '资产管理': ElementPlusIconsVue.OfficeBuilding,
    '存货核算': ElementPlusIconsVue.Box,
    '成本管理': ElementPlusIconsVue.DataAnalysis,
    '预算管理': ElementPlusIconsVue.TrendCharts,
    '合并报表': ElementPlusIconsVue.Files,
    '审批流': ElementPlusIconsVue.Checked,
    '报表管理': ElementPlusIconsVue.Document,
    '费用管理': ElementPlusIconsVue.Money,
    '合同管理': ElementPlusIconsVue.Tickets,
    '项目管理': ElementPlusIconsVue.Aim,
    '系统管理': ElementPlusIconsVue.Setting,
    '税务管理': ElementPlusIconsVue.DocumentChecked,
    '财务报表': ElementPlusIconsVue.Document
  }
  return map[props.parentPath] || ElementPlusIconsVue.Menu
}

function childParentPath(menu) {
  return menu.path || props.parentPath
}

function menuRoute(menu) {
  return resolveMenuRoute(menu, props.parentPath)
}

function moduleStyle(menu) {
  const mc = getModuleColor(menu?.menuName || props.parentPath)
  return { '--mc': mc.color, '--mc-rgb': mc.rgb }
}

function accentFor(menu) {
  return props.accent || getModuleColor(menu?.menuName || props.parentPath)
}
</script>

<template>
  <template v-for="menu in visibleMenus" :key="menu.id">
    <el-sub-menu
      v-if="menu.menuType === 'M' && menu.children && menu.children.length"
      :index="`group-${menu.id}`"
      popper-class="sidebar-popper"
      :style="moduleStyle(menu)"
    >
      <template #title>
        <el-icon :size="18"><component :is="iconComponent(menu.menuName)" /></el-icon>
        <span class="menu-title">{{ menu.menuName }}</span>
      </template>
      <SidebarMenuItem :menus="menu.children" :parent-path="childParentPath(menu)" :accent="accentFor(menu)" />
    </el-sub-menu>
    <el-menu-item
      v-else-if="menu.menuType === 'C' && menuRoute(menu)"
      :index="String(menu.id)"
      class="menu-item-child"
      :style="{ '--mc': accentFor(menu).color, '--mc-rgb': accentFor(menu).rgb }"
    >
      <span class="menu-dot">•</span>
      <span>{{ menu.menuName }}</span>
    </el-menu-item>
  </template>
</template>

<style scoped>
.menu-title {
  font-weight: 500;
  letter-spacing: 0.3px;
}
.menu-item-child {
  padding-left: 44px !important;
  font-size: 12px !important;
  height: 32px !important;
  line-height: 32px !important;
}
.menu-item-child .menu-dot {
  margin-right: 4px;
  font-weight: bold;
  color: #5a6b80;
  transition: color .2s;
}
.menu-item-child:hover .menu-dot {
  color: var(--mc, #5a6b80);
}
</style>
