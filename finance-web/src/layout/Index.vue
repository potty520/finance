<template>
  <el-container class="layout-container">
    <div
      v-if="isMobile && mobileMenuOpen"
      class="mobile-backdrop"
      aria-hidden="true"
      @click="closeMobileMenu"
    />
    <!-- ===== 侧边栏 ===== -->
    <el-aside
      :width="isMobile ? '220px' : (collapse ? '64px' : '220px')"
      :class="['sidebar', { 'mobile-open': mobileMenuOpen }]"
    >
      <div class="logo-area" @click="toggleNavigation">
        <div class="logo-icon">💰</div>
        <transition name="fade">
          <div v-if="!collapse" class="logo-text">
            <div class="logo-title">清账财务</div>
            <div class="logo-subtitle">QingZhang Finance</div>
          </div>
        </transition>
      </div>

      <div class="sidebar-menu-wrap">
        <el-menu
          :default-active="activeMenuId"
          :collapse="collapse"
          :collapse-transition="false"
          text-color="#b8c4d4"
          active-text-color="#ffffff"
          @select="handleMenuSelect"
        >
          <el-menu-item index="dashboard" :style="{ '--mc': 'var(--mod-dashboard)', '--mc-rgb': 'var(--mod-dashboard-rgb)' }">
            <template #title>
              <el-icon :size="18"><HomeFilled /></el-icon>
              <span>工作台</span>
            </template>
          </el-menu-item>
          <SidebarMenuItem v-if="navMenus.length" :menus="navMenus" />
        </el-menu>
      </div>

      <!-- 侧边栏底部信息 -->
      <div class="sidebar-footer" v-if="!collapse">
        <div class="sidebar-footer-item">
          <el-icon><Clock /></el-icon>
          <span>{{ currentPeriod }}</span>
        </div>
      </div>
    </el-aside>

    <!-- ===== 右侧主体 ===== -->
    <el-container class="main-container">
      <!-- 顶部导航 -->
      <el-header class="header" height="56px">
        <div class="header-left">
          <el-icon class="collapse-btn" :size="20" @click="toggleNavigation">
            <Menu v-if="isMobile" />
            <Fold v-else-if="!collapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">
              <el-icon style="vertical-align: -2px;"><HomeFilled /></el-icon>
              <span style="margin-left: 4px;">首页</span>
            </el-breadcrumb-item>
            <el-breadcrumb-item>
              <span style="color: var(--text-primary); font-weight: 500;">
                {{ route.meta.title || '工作台' }}
              </span>
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <!-- 快捷搜索 -->
          <el-input
            v-model="searchKeyword"
            class="header-search"
            placeholder="搜索凭证号..."
            :prefix-icon="Search"
            clearable
            size="small"
            @keyup.enter="handleGlobalSearch"
          />
          <!-- 通知 -->
          <el-badge :value="noticeCount" :hidden="noticeCount === 0" class="header-notice">
            <el-button link @click="showNotices">
              <el-icon :size="20"><Bell /></el-icon>
            </el-button>
          </el-badge>
          <!-- 用户 -->
          <el-dropdown trigger="click" @command="handleCmd">
            <div class="user-info">
              <el-avatar :size="34" class="user-avatar">
                {{ userStore.userInfo?.realName?.charAt(0) || 'U' }}
              </el-avatar>
              <div class="user-text">
                <div class="user-name">{{ userStore.userInfo?.realName || userStore.userInfo?.username || '用户' }}</div>
                <div class="user-role">{{ userRoleText }}</div>
              </div>
              <el-icon class="user-arrow"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><UserFilled /></el-icon> 个人信息
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="slide-up" mode="out-in">
            <component :is="Component" :key="route.fullPath + (route.query.m || '')" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage, ElNotification } from 'element-plus'
import {
  HomeFilled, Fold, Expand, Menu, Search, Bell, Clock,
  ArrowDown, UserFilled, SwitchButton
} from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import SidebarMenuItem from '@/components/SidebarMenuItem.vue'
import { findActiveMenuId, buildMenuLocation } from '@/utils/menuRoute'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const collapse = ref(false)
const isMobile = ref(false)
const mobileMenuOpen = ref(false)
const searchKeyword = ref('')
const currentPeriod = computed(() => {
  const now = new Date()
  return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
})

const navMenus = computed(() => userStore.menus || [])
const noticeCount = ref(0)
const activeMenuId = computed(() => {
  if (route.path === '/dashboard') return 'dashboard'
  return findActiveMenuId(navMenus.value, route.path, route.query.m) || 'dashboard'
})

const handleMenuSelect = (index) => {
  if (index === 'dashboard') {
    router.push('/dashboard')
    closeMobileMenu()
    return
  }
  if (String(index).startsWith('group-')) return
  const loc = buildMenuLocation(navMenus.value, index)
  if (loc) {
    router.push(loc)
    closeMobileMenu()
  }
}

const updateViewport = () => {
  isMobile.value = window.matchMedia('(max-width: 768px)').matches
  if (!isMobile.value) mobileMenuOpen.value = false
}

const toggleNavigation = () => {
  if (isMobile.value) {
    mobileMenuOpen.value = !mobileMenuOpen.value
  } else {
    collapse.value = !collapse.value
  }
}

const closeMobileMenu = () => {
  if (isMobile.value) mobileMenuOpen.value = false
}

const handleGlobalSearch = () => {
  const kw = searchKeyword.value.trim()
  if (!kw) return
  router.push({ path: '/ledger/voucher-list', query: { keyword: kw } })
  ElMessage.success(`已按凭证号搜索：${kw}`)
}

onMounted(async () => {
  updateViewport()
  window.addEventListener('resize', updateViewport)
  if (!userStore.userInfo) {
    try { await userStore.loadInfo() } catch (e) {}
  }
  // 拉取真实业务预警作为通知
  try {
    const { getAlerts } = await import('@/api/dashboard')
    const res = await getAlerts()
    noticeList.value = res?.data || []
    noticeCount.value = noticeList.value.length
  } catch (e) { /* 忽略 */ }
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', updateViewport)
})

const userRoleText = computed(() => {
  const roles = userStore.userInfo?.roles
  if (Array.isArray(roles) && roles.length) {
    const names = roles.map(r => (typeof r === 'string' ? r : (r.roleName || r.roleCode || ''))).filter(Boolean)
    if (names.length) return names.join(' / ')
  }
  if (typeof roles === 'string' && roles) return roles
  return '系统用户'
})

const noticeList = ref([])
const showNotices = () => {
  ElNotification.closeAll()
  if (!noticeList.value.length) {
    ElNotification({ title: '暂无预警', message: '当前没有新的业务预警', type: 'success', duration: 3000 })
    return
  }
  noticeList.value.forEach(n => {
    ElNotification({
      title: n.title,
      message: n.desc || '',
      type: n.level === 'danger' ? 'error' : (n.level === 'warning' ? 'warning' : 'info'),
      duration: 5000
    })
  })
}

const handleCmd = async (cmd) => {
  if (cmd === 'logout') {
    await ElMessageBox.confirm('确定退出登录吗？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    await userStore.logout()
    ElMessage.success('已安全退出')
    router.push('/login')
  }
}
</script>

<style scoped lang="scss">
.layout-container {
  height: 100vh;
  background: var(--bg-page);
  min-width: 0;
}

// ===== 侧边栏（深藏青深色）=====
.sidebar {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #1e293b;
  border-right: 1px solid rgba(255, 255, 255, 0.06);
  transition: width 0.28s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  box-shadow: 1px 0 10px rgba(0, 0, 0, 0.25);
  z-index: 30;
}
.mobile-backdrop {
  display: none;
}

.logo-area {
  display: flex;
  align-items: center;
  padding: 10px 14px;
  height: 52px;
  cursor: pointer;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
  gap: 10px;
  transition: background 0.2s;
  &:hover { background: #263449; }
}
.logo-icon {
  flex-shrink: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1f5eaa, #3a7bc4);
  border-radius: 8px;
  font-size: 16px;
}
.logo-text {
  overflow: hidden;
  white-space: nowrap;
}
.logo-title {
  font-size: 14px;
  font-weight: 700;
  color: #eef2f7;
  line-height: 1.2;
}
.logo-subtitle {
  font-size: 10px;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.sidebar-menu-wrap {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 8px 0;
}

// 侧边栏底部
.sidebar-footer {
  flex-shrink: 0;
  padding: 6px 14px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  background: #182234;
}
.sidebar-footer-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 11px;
  color: #94a3b8;
}

// ===== 右侧主体 =====
.main-container {
  background: var(--bg-page);
  min-width: 0;
}

// 顶部
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid var(--border-base);
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
  z-index: 10;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}
.collapse-btn {
  cursor: pointer;
  color: var(--text-secondary);
  transition: color 0.2s;
  &:hover { color: var(--primary); }
}
.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
  min-width: 0;
}

// 搜索
.header-search {
  width: 240px;
  :deep(.el-input__wrapper) {
    background: #f5f7fa;
    border-radius: 20px;
    border: none;
    box-shadow: none;
    transition: all 0.3s;
    &:hover, &.is-focus {
      background: #fff;
      box-shadow: 0 0 0 1px var(--primary) inset;
    }
  }
}

// 通知
.header-notice {
  :deep(.el-badge__content) {
    border: none;
    font-size: 11px;
  }
}

// 用户
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 4px 10px 4px 4px;
  border-radius: 24px;
  transition: background 0.2s;
  &:hover { background: #f5f7fa; }
}
.user-avatar {
  background: linear-gradient(135deg, #1f5eaa, #3a7bc4);
  color: #fff;
  font-weight: 600;
}
.user-text {
  line-height: 1.3;
}
.user-name {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-primary);
}
.user-role {
  font-size: 11px;
  color: var(--text-secondary);
}
.user-arrow {
  font-size: 12px;
  color: var(--text-placeholder);
  transition: transform 0.2s;
}

// 菜单
:deep(.el-menu) {
  border-right: none;
  background: transparent;
  .el-menu-item {
    margin: 1px 6px;
    border-radius: 6px;
    height: 36px;
    line-height: 36px;
    font-size: 13px;
    transition: all 0.2s;
    color: #b8c4d4;
    &:hover { background: rgba(255,255,255,.08) !important; color: #ffffff !important; }
    &.is-active {
      background: rgba(var(--mc-rgb, var(--mod-dashboard-rgb)), .18) !important;
      color: #ffffff !important;
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 3px;
        height: 16px;
        background: var(--mc, var(--mod-dashboard));
        border-radius: 0 3px 3px 0;
      }
    }
  }
  .el-sub-menu {
    margin: 1px 6px;
    border-radius: 6px;
    .el-sub-menu__title {
      border-radius: 6px;
      height: 36px;
      line-height: 36px;
      font-size: 13px;
      color: #b8c4d4;
      &:hover { background: rgba(255,255,255,.08) !important; color: #ffffff !important; }
      .el-icon { color: var(--mc, #94a3b8); transition: color .2s; }
      .el-sub-menu__icon-arrow { color: #94a3b8; }
    }
    &.is-active .el-sub-menu__title {
      color: #ffffff !important;
      .el-icon { color: var(--mc, #94a3b8); }
    }
  }
  // 展开的子菜单最大高度限制 + 滚动
  .el-sub-menu .el-menu {
    max-height: 260px;
    overflow-y: auto;
    overflow-x: hidden;
  }
}

// 面包屑
:deep(.el-breadcrumb__inner) {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: var(--text-secondary);
}
:deep(.el-breadcrumb__separator) {
  margin: 0 8px;
  color: #dcdfe6;
  &::before {
    content: '›';
    font-size: 18px;
    font-weight: 300;
  }
}

// 内容区
.main-content {
  padding: 20px;
  background: var(--bg-page);
  overflow-y: auto;
  min-width: 0;
}

@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    inset: 0 auto 0 0;
    width: 220px !important;
    transform: translateX(-100%);
    transition: transform 0.24s ease;
  }
  .sidebar.mobile-open {
    transform: translateX(0);
  }
  .mobile-backdrop {
    display: block;
    position: fixed;
    inset: 0;
    z-index: 20;
    background: rgba(15, 23, 42, 0.42);
  }
  .header {
    padding: 0 12px;
  }
  .header-left {
    gap: 8px;
    min-width: 0;
  }
  .header-left :deep(.el-breadcrumb__item:first-child) {
    display: none;
  }
  .header-right {
    gap: 6px;
  }
  .header-search,
  .user-text,
  .user-arrow {
    display: none;
  }
  .user-info {
    padding-right: 0;
  }
  .main-content {
    padding: 12px;
  }
}
</style>
