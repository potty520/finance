<template>
  <el-container class="layout-container">
    <!-- ===== 侧边栏 ===== -->
    <el-aside :width="collapse ? '64px' : '220px'" class="sidebar">
      <div class="logo-area" @click="collapse = !collapse">
        <div class="logo-icon">💰</div>
        <transition name="fade">
          <div v-if="!collapse" class="logo-text">
            <div class="logo-title">铁壳财务</div>
            <div class="logo-subtitle">Finance System</div>
          </div>
        </transition>
      </div>

      <div class="sidebar-menu-wrap">
        <el-menu
          :default-active="activeMenuId"
          :collapse="collapse"
          :collapse-transition="false"
          background-color="#ffffff"
          text-color="#555"
          active-text-color="#409eff"
          @select="handleMenuSelect"
        >
          <el-menu-item index="dashboard">
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
          <el-icon class="collapse-btn" :size="20" @click="collapse = !collapse">
            <Fold v-if="!collapse" />
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
            placeholder="搜索客户/供应商/凭证号..."
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
                <div class="user-role">管理员</div>
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
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage, ElNotification } from 'element-plus'
import {
  HomeFilled, Fold, Expand, Search, Bell, Clock,
  ArrowDown, UserFilled, SwitchButton
} from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import SidebarMenuItem from '@/components/SidebarMenuItem.vue'
import { findActiveMenuId, buildMenuLocation } from '@/utils/menuRoute'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const collapse = ref(false)
const searchKeyword = ref('')
const currentPeriod = computed(() => {
  const now = new Date()
  return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
})

const navMenus = computed(() => userStore.menus || [])
const noticeCount = ref(3)
const activeMenuId = computed(() => {
  if (route.path === '/dashboard') return 'dashboard'
  return findActiveMenuId(navMenus.value, route.path, route.query.m) || 'dashboard'
})

const handleMenuSelect = (index) => {
  if (index === 'dashboard') {
    router.push('/dashboard')
    return
  }
  if (String(index).startsWith('group-')) return
  const loc = buildMenuLocation(navMenus.value, index)
  if (loc) router.push(loc)
}

const handleGlobalSearch = () => {
  if (!searchKeyword.value.trim()) return
  ElMessage.info(`搜索: ${searchKeyword.value}（功能开发中）`)
}

onMounted(async () => {
  if (!userStore.userInfo) {
    try { await userStore.loadInfo() } catch (e) {}
  }
})

const notices = [
  { title: '应收账款到期提醒', content: '客户A 50,000元将于2026-06-28到期', time: '2026-06-25 14:30' },
  { title: '预算超支预警', content: '管理费用已超预算12%，请关注', time: '2026-06-25 10:15' },
  { title: '库存预警', content: 'A物料当前库存23件，低于安全库存', time: '2026-06-24 16:00' }
]
const showNotices = () => {
  ElNotification.closeAll()
  notices.forEach(n => {
    ElNotification({
      title: n.title,
      message: `${n.content} (${n.time})`,
      type: 'warning',
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
}

// ===== 侧边栏 =====
.sidebar {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #fff;
  border-right: 1px solid var(--border-base);
  transition: width 0.28s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  box-shadow: 1px 0 8px rgba(0, 0, 0, 0.04);
}

.logo-area {
  display: flex;
  align-items: center;
  padding: 16px 18px;
  height: 68px;
  cursor: pointer;
  border-bottom: 1px solid var(--border-base);
  gap: 12px;
  transition: background 0.2s;
  &:hover { background: #f5f7fa; }
}
.logo-icon {
  flex-shrink: 0;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #409eff, #66b1ff);
  border-radius: 10px;
  font-size: 18px;
}
.logo-text {
  overflow: hidden;
  white-space: nowrap;
}
.logo-title {
  font-size: 15px;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1.2;
}
.logo-subtitle {
  font-size: 11px;
  color: var(--text-secondary);
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
  padding: 12px 16px;
  border-top: 1px solid var(--border-base);
  background: #fafafa;
}
.sidebar-footer-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: var(--text-secondary);
}

// ===== 右侧主体 =====
.main-container { background: var(--bg-page); }

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
  background: linear-gradient(135deg, #409eff, #66b1ff);
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
  .el-menu-item {
    margin: 2px 8px;
    border-radius: 8px;
    height: 44px;
    line-height: 44px;
    font-size: 14px;
    transition: all 0.2s;
    &:hover { background: #ecf5ff !important; }
    &.is-active {
      background: linear-gradient(135deg, #ecf5ff, #d9ecff) !important;
      font-weight: 600;
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 3px;
        height: 20px;
        background: var(--primary);
        border-radius: 0 3px 3px 0;
      }
    }
  }
  .el-sub-menu {
    margin: 2px 8px;
    border-radius: 8px;
    .el-sub-menu__title {
      border-radius: 8px;
      height: 44px;
      line-height: 44px;
      font-size: 14px;
      &:hover { background: #ecf5ff !important; }
    }
    &.is-active .el-sub-menu__title {
      color: var(--primary) !important;
      font-weight: 600;
    }
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
}
</style>
