<template>
  <div class="dashboard">
    <!-- ===== 顶部统计卡片 ===== -->
    <div class="stat-grid">
      <div class="stat-card stat-income">
        <div class="stat-card-inner">
          <div class="stat-icon-wrap">
            <div class="stat-icon">
              <el-icon :size="22"><TrendCharts /></el-icon>
            </div>
          </div>
          <div class="stat-body">
            <div class="stat-label">本月借方发生额</div>
            <div class="stat-value">¥{{ fmtMoney(summary.monthDebit) }}</div>
            <div class="stat-extra">
              <el-tag type="primary" size="small" effect="plain">本月 {{ summary.monthVoucherCount }} 张凭证</el-tag>
            </div>
          </div>
        </div>
      </div>

      <div class="stat-card stat-expense">
        <div class="stat-card-inner">
          <div class="stat-icon-wrap">
            <div class="stat-icon">
              <el-icon :size="22"><Money /></el-icon>
            </div>
          </div>
          <div class="stat-body">
            <div class="stat-label">本月贷方发生额</div>
            <div class="stat-value">¥{{ fmtMoney(summary.monthCredit) }}</div>
            <div class="stat-extra">
              <el-tag type="success" size="small" effect="plain">累计 {{ summary.voucherTotal }} 张</el-tag>
            </div>
          </div>
        </div>
      </div>

      <div class="stat-card stat-receivable">
        <div class="stat-card-inner">
          <div class="stat-icon-wrap">
            <div class="stat-icon">
              <el-icon :size="22"><Sell /></el-icon>
            </div>
          </div>
          <div class="stat-body">
            <div class="stat-label">应收未收</div>
            <div class="stat-value">¥{{ fmtMoney(summary.arUncollected) }}</div>
            <div class="stat-extra">
              <el-tag type="warning" size="small" effect="plain">{{ summary.customerCount }} 家客户</el-tag>
            </div>
          </div>
        </div>
      </div>

      <div class="stat-card stat-payable">
        <div class="stat-card-inner">
          <div class="stat-icon-wrap">
            <div class="stat-icon">
              <el-icon :size="22"><ShoppingCart /></el-icon>
            </div>
          </div>
          <div class="stat-body">
            <div class="stat-label">应付未付</div>
            <div class="stat-value">¥{{ fmtMoney(summary.apUnpaid) }}</div>
            <div class="stat-extra">
              <el-tag type="danger" size="small" effect="plain">{{ summary.supplierCount }} 家供应商</el-tag>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ===== 快捷操作 + 趋势图 ===== -->
    <div class="dashboard-grid">
      <!-- 快捷操作 -->
      <div class="quick-actions-card">
        <div class="card-header">
          <h3>快捷操作</h3>
        </div>
        <div class="quick-actions">
          <div class="quick-action-item" v-for="action in quickActions" :key="action.label" @click="handleQuick(action.path)">
            <div class="qa-icon" :style="{ background: action.bg }">
              <el-icon :size="20"><component :is="action.icon" /></el-icon>
            </div>
            <div class="qa-info">
              <div class="qa-label">{{ action.label }}</div>
              <div class="qa-desc">{{ action.desc }}</div>
            </div>
            <el-icon class="qa-arrow"><ArrowRight /></el-icon>
          </div>
        </div>
      </div>

      <!-- 资金趋势图 -->
      <div class="trend-card">
        <div class="card-header">
          <h3>月度借贷发生额趋势</h3>
          <div class="card-header-right">
            <el-radio-group v-model="trendPeriod" size="small" @change="loadTrend">
              <el-radio-button value="6">近6个月</el-radio-button>
              <el-radio-button value="12">近12个月</el-radio-button>
            </el-radio-group>
          </div>
        </div>
        <div v-if="!loading && trendData.length === 0" class="trend-empty">
          <el-empty description="暂无趋势数据" :image-size="64" />
        </div>
        <v-chart v-else :option="trendOption" autoresize style="height: 240px;" />
      </div>
    </div>

    <!-- ===== 下排：预警 + 最近凭证 + 待办 ===== -->
    <div class="dashboard-bottom">
      <!-- 预警 -->
      <div class="alert-card">
        <div class="card-header">
          <h3>
            <el-badge :value="alerts.length" class="alert-badge">业务预警</el-badge>
          </h3>
        </div>
        <div class="alert-list" v-loading="loading">
          <el-empty v-if="!loading && alerts.length === 0" description="暂无预警" :image-size="60" />
          <div class="alert-item" v-for="(item, idx) in alerts" :key="idx" :class="'alert-' + item.levelType">
            <div class="alert-dot" :class="'dot-' + item.levelType"></div>
            <div class="alert-content">
              <div class="alert-title">{{ item.title }}</div>
              <div class="alert-time">{{ item.desc }}</div>
            </div>
            <el-tag :type="tagType(item.levelType)" size="small" effect="plain">{{ item.level }}</el-tag>
          </div>
        </div>
      </div>

      <!-- 最近凭证 -->
      <div class="voucher-card">
        <div class="card-header">
          <h3>最近凭证</h3>
          <el-button type="primary" size="small" text style="font-weight: 500;" @click="router.push('/ledger/voucher-list')">查看全部 →</el-button>
        </div>
        <el-table :data="recentVouchers" stripe size="small" v-loading="loading">
          <el-table-column prop="voucherNo" label="凭证号" width="140" />
          <el-table-column prop="voucherDate" label="日期" width="100" />
          <el-table-column prop="summary" label="摘要" min-width="140" show-overflow-tooltip />
          <el-table-column label="借方金额" width="120" align="right">
            <template #default="{ row }">
              <span class="amount">¥{{ fmtMoney(row.totalDebit) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="80" align="center">
            <template #default="{ row }">
              <el-tag :type="statusTag(row.status)" size="small">
                {{ statusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="!loading && recentVouchers.length === 0" description="暂无凭证数据" :image-size="60" />
      </div>

      <!-- 待办 -->
      <div class="todo-card">
        <div class="card-header">
          <h3>待办事项</h3>
          <el-tag type="warning" size="small" effect="dark" round>{{ todoCount }} 项</el-tag>
        </div>
        <div class="todo-list">
          <div class="todo-item" v-for="(item, idx) in todoItems" :key="idx" @click="router.push(item.path)">
            <div class="todo-left">
              <div class="todo-type-tag" :class="'type-' + item.tagType">{{ item.tag }}</div>
            </div>
            <div class="todo-center">
              <div class="todo-biz">{{ item.title }}</div>
              <div class="todo-meta">{{ item.desc }}</div>
            </div>
            <div class="todo-right">
              <el-icon color="#c0c4cc"><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
        <el-empty v-if="!loading && todoItems.length === 0" description="没有待办事项" :image-size="60" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart, PieChart } from 'echarts/charts'
import {
  TitleComponent, TooltipComponent, LegendComponent, GridComponent
} from 'echarts/components'
import {
  TrendCharts, Money, Sell, ShoppingCart,
  EditPen, DocumentAdd, Upload, CreditCard,
  Wallet, Notebook, ArrowRight
} from '@element-plus/icons-vue'
import { getSummary, getTrend, getAlerts, getRecentVouchers } from '@/api/dashboard'

use([CanvasRenderer, LineChart, BarChart, PieChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent])

const router = useRouter()
const loading = ref(false)
const trendPeriod = ref('6')
const summary = ref({ monthDebit: 0, monthCredit: 0, monthVoucherCount: 0, voucherTotal: 0, arUncollected: 0, apUnpaid: 0, customerCount: 0, supplierCount: 0, pendingVoucherCount: 0 })
const trendData = ref([])
const alerts = ref([])
const recentVouchers = ref([])

// 快捷操作
const quickActions = [
  { label: '录入凭证', desc: '记一笔', icon: EditPen, path: '/ledger/voucher?add=1', bg: 'linear-gradient(135deg, #1f5eaa, #3a7bc4)' },
  { label: '费用报销', desc: '提交报销单', icon: DocumentAdd, path: '/expense/apply', bg: 'linear-gradient(135deg, #67c23a, #95d475)' },
  { label: '收款单', desc: '登记收款', icon: Upload, path: '/receivable/receipt', bg: 'linear-gradient(135deg, #e6a23c, #ebb563)' },
  { label: '付款单', desc: '登记付款', icon: CreditCard, path: '/payable/payment', bg: 'linear-gradient(135deg, #f56c6c, #f89898)' },
  { label: '银行对账', desc: '核对银行流水', icon: Wallet, path: '/cashier/bank', bg: 'linear-gradient(135deg, #909399, #b4b8bf)' },
  { label: '科目余额表', desc: '查看余额', icon: Notebook, path: '/report/balance', bg: 'linear-gradient(135deg, #7c3aed, #a78bfa)' }
]

// 趋势图
const trendOption = computed(() => {
  const xData = trendData.value.map(t => t.ym || '')
  const incomeData = trendData.value.map(t => t.credit || 0)
  const expenseData = trendData.value.map(t => t.debit || 0)

  return {
    tooltip: {
      trigger: 'axis',
      backgroundColor: '#fff',
      borderColor: '#ebeef5',
      textStyle: { color: '#333' },
      formatter: (params) => {
        let html = `<div style="font-weight:600;margin-bottom:6px;">${params[0].axisValue}</div>`
        params.forEach(p => {
          html += `<div style="display:flex;align-items:center;gap:8px;margin:4px 0;">
            <span style="width:10px;height:10px;border-radius:50%;background:${p.color};display:inline-block;"></span>
            ${p.seriesName}: <b>¥${Number(p.value || 0).toLocaleString('zh-CN', { minimumFractionDigits: 2 })}</b>
          </div>`
        })
        return html
      }
    },
    legend: {
      bottom: 0,
      textStyle: { color: '#666', fontSize: 12 }
    },
    grid: { top: 10, left: 56, right: 20, bottom: 40 },
    xAxis: {
      type: 'category',
      data: xData,
      axisLine: { lineStyle: { color: '#e4e7ed' } },
      axisTick: { show: false },
      axisLabel: { color: '#606266', fontSize: 12 }
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { color: '#f2f3f5', type: 'dashed' } },
      axisLabel: {
        color: '#606266',
        fontSize: 12,
        formatter: (v) => '¥' + v + 'k'
      }
    },
    series: [
      {
        name: '贷方发生额',
        type: 'line',
        data: incomeData,
        smooth: true,
        symbol: 'circle',
        symbolSize: 6,
        lineStyle: { color: '#1f5eaa', width: 2.5 },
        itemStyle: { color: '#1f5eaa' },
        areaStyle: {
          color: {
            type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(64,158,255,0.15)' },
              { offset: 1, color: 'rgba(64,158,255,0.02)' }
            ]
          }
        }
      },
      {
        name: '借方发生额',
        type: 'line',
        data: expenseData,
        smooth: true,
        symbol: 'circle',
        symbolSize: 6,
        lineStyle: { color: '#f56c6c', width: 2.5 },
        itemStyle: { color: '#f56c6c' },
        areaStyle: {
          color: {
            type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(245,108,108,0.12)' },
              { offset: 1, color: 'rgba(245,108,108,0.02)' }
            ]
          }
        }
      }
    ]
  }
})

// 待办汇总（基于预警 + 待审凭证）
const todoItems = computed(() => {
  const items = []
  const pending = Number(summary.value.pendingVoucherCount || 0)
  if (pending > 0) {
    items.push({ tag: '凭证', tagType: 'expense', title: `${pending} 张凭证待审核`, desc: '前往凭证管理处理', path: '/ledger/voucher-list' })
  }
  const arOverdue = alerts.value.filter(a => a.type === 'arOverdue').length
  if (arOverdue > 0) {
    items.push({ tag: '应收', tagType: 'pay', title: `${arOverdue} 笔应收逾期`, desc: '尽快催收账款', path: '/receivable/aging' })
  }
  const apDue = alerts.value.filter(a => a.type === 'apDue').length
  if (apDue > 0) {
    items.push({ tag: '应付', tagType: 'contract', title: `${apDue} 笔应付将到期`, desc: '安排资金付款', path: '/payable/aging' })
  }
  return items
})
const todoCount = computed(() => todoItems.value.length)

const tagType = (t) => ({ high: 'danger', mid: 'warning', low: 'info' }[t])

const statusText = (s) => {
  const map = { DRAFT: '草稿', D: '草稿', SUBMITTED: '待审核', APPROVING: '审核中', APPROVED: '已审核', A: '已审核', POSTED: '已过账', P: '已过账', REJECTED: '已驳回', R: '已驳回' }
  return map[s] || s || '草稿'
}
const statusTag = (s) => {
  const map = { DRAFT: 'info', D: 'info', SUBMITTED: 'warning', APPROVING: 'warning', APPROVED: 'success', A: 'success', POSTED: 'success', P: 'success', REJECTED: 'danger', R: 'danger' }
  return map[s] || 'info'
}

const fmtMoney = (v) => {
  const n = Number(v || 0)
  return n.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const loadTrend = async () => {
  const res = await getTrend(Number(trendPeriod.value))
  trendData.value = res?.data || []
}

const loadAll = async () => {
  loading.value = true
  try {
    const [s, a, r, t] = await Promise.all([
      getSummary(), getAlerts(), getRecentVouchers(8), getTrend(Number(trendPeriod.value))
    ])
    summary.value = s?.data || summary.value
    alerts.value = (a?.data || []).map(x => ({
      ...x,
      levelType: x.level === 'danger' ? 'high' : (x.level === 'warning' ? 'mid' : 'low'),
      level: x.level === 'danger' ? '高' : (x.level === 'warning' ? '中' : '低')
    }))
    recentVouchers.value = r?.data || []
    trendData.value = t?.data || []
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleQuick = (path) => {
  if (path) router.push(path)
}

onMounted(loadAll)
</script>

<style scoped lang="scss">
.dashboard {
  width: 100%;
  min-height: 0;
}

// ===== 统计卡片 =====
.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}
.stat-card {
  background: #fff;
  border-radius: var(--radius-base);
  padding: 14px 18px;
  box-shadow: var(--shadow-card);
  transition: all var(--transition);
  cursor: pointer;
  overflow: hidden;
  position: relative;
  min-width: 0;
  &::after {
    content: '';
    position: absolute;
    right: -20px;
    bottom: -20px;
    width: 100px;
    height: 100px;
    border-radius: 50%;
    opacity: 0.06;
  }
  &:hover { transform: translateY(-2px); box-shadow: var(--shadow-hover); }
}
.stat-card-inner {
  display: flex;
  gap: 12px;
  align-items: center;
}
.stat-icon-wrap {
  flex-shrink: 0;
}
.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}
.stat-income {
  .stat-icon { background: linear-gradient(135deg, #1f5eaa, #3a7bc4); }
  &::after { background: #1f5eaa; }
}
.stat-expense {
  .stat-icon { background: linear-gradient(135deg, #67c23a, #95d475); }
  &::after { background: #67c23a; }
}
.stat-receivable {
  .stat-icon { background: linear-gradient(135deg, #e6a23c, #ebb563); }
  &::after { background: #e6a23c; }
}
.stat-payable {
  .stat-icon { background: linear-gradient(135deg, #f56c6c, #f89898); }
  &::after { background: #f56c6c; }
}
.stat-body { flex: 1; min-width: 0; }
.stat-label { font-size: 12px; color: var(--text-secondary); margin-bottom: 4px; }
.stat-value {
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.stat-extra { margin-top: 2px; }

// ===== 快捷操作 + 趋势 =====
.dashboard-grid {
  display: grid;
  grid-template-columns: 1fr 1.5fr;
  gap: 16px;
  margin-bottom: 16px;
}

.quick-actions-card, .trend-card {
  background: #fff;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-card);
  overflow: hidden;
  min-width: 0;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid var(--border-base);
  h3 {
    margin: 0;
    font-size: 14px;
    font-weight: 600;
    color: var(--text-primary);
    flex-shrink: 0;
    white-space: nowrap;
  }
}
.card-header-right { display: flex; align-items: center; }

// 快捷操作
.quick-actions {
  padding: 6px;
}
.quick-action-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 14px;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.2s;
  min-width: 0;
  &:hover {
    background: #f5f7fa;
    .qa-arrow { opacity: 1; transform: translateX(0); }
  }
}
.qa-icon {
  width: 36px;
  height: 36px;
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}
.qa-info { flex: 1; min-width: 0; overflow: hidden; }
.qa-label { font-size: 13px; font-weight: 500; color: var(--text-primary); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.qa-desc { font-size: 12px; color: var(--text-secondary); margin-top: 2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.qa-arrow {
  opacity: 0;
  transform: translateX(-8px);
  transition: all 0.2s;
  color: var(--text-placeholder);
  font-size: 14px;
  flex-shrink: 0;
}

// 趋势图
.trend-card {
  padding-bottom: 6px;
}
.trend-empty {
  height: 240px;
  display: flex;
  align-items: center;
  justify-content: center;
}

// ===== 底部三栏 =====
.dashboard-bottom {
  display: grid;
  grid-template-columns: 1fr 1.2fr 0.9fr;
  gap: 16px;
}

.alert-card, .voucher-card, .todo-card {
  background: #fff;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-card);
  overflow: hidden;
  min-width: 0;
}

// 预警列表
.alert-list {
  padding: 6px 0;
  min-height: 100px;
}
.alert-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  transition: background 0.2s;
  min-width: 0;
  &:hover { background: #fafafa; }
}
.alert-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}
.dot-high { background: var(--danger); box-shadow: 0 0 6px rgba(245,108,108,0.4); }
.dot-mid { background: var(--warning); box-shadow: 0 0 6px rgba(230,162,60,0.4); }
.dot-low { background: var(--info); }
.alert-content { flex: 1; min-width: 0; overflow: hidden; }
.alert-title { font-size: 13px; color: var(--text-primary); margin-bottom: 2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.alert-time { font-size: 11px; color: var(--text-secondary); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.alert-badge :deep(.el-badge__content) { border: none; }

// 最近凭证
.voucher-card {
  overflow-x: auto;
  :deep(.el-table) { font-size: 13px; min-width: 480px; }
  .amount { font-weight: 500; color: var(--text-primary); font-variant-numeric: tabular-nums; }
}

// 待办列表
.todo-list {
  padding: 4px 0;
  min-height: 100px;
}
.todo-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 16px;
  transition: background 0.2s;
  cursor: pointer;
  min-width: 0;
  &:hover { background: #fafafa; }
}
.todo-type-tag {
  width: 40px;
  height: 22px;
  border-radius: 5px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 600;
  color: #fff;
  flex-shrink: 0;
}
.type-expense { background: #1f5eaa; }
.type-contract { background: #e6a23c; }
.type-project { background: #7c3aed; }
.type-pay { background: #f56c6c; }
.todo-center { flex: 1; min-width: 0; overflow: hidden; }
.todo-biz { font-size: 13px; font-weight: 500; color: var(--text-primary); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.todo-meta { font-size: 11px; color: var(--text-secondary); margin-top: 2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.todo-right { display: flex; align-items: center; flex-shrink: 0; }

// 响应式
@media (max-width: 1400px) {
  .dashboard-bottom { grid-template-columns: 1fr 1fr; }
  .todo-card { grid-column: 1 / -1; }
}
@media (max-width: 1200px) {
  .stat-grid { grid-template-columns: repeat(2, 1fr); }
  .dashboard-grid { grid-template-columns: 1fr; }
  .dashboard-bottom { grid-template-columns: 1fr; }
  .todo-card { grid-column: auto; }
}
@media (max-width: 768px) {
  .stat-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 10px;
  }
  .stat-card {
    padding: 12px;
  }
  .stat-card-inner {
    gap: 8px;
  }
  .stat-icon {
    width: 34px;
    height: 34px;
    border-radius: 10px;
  }
  .stat-value {
    font-size: 17px;
  }
  .stat-extra :deep(.el-tag) {
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .dashboard-grid,
  .dashboard-bottom {
    gap: 10px;
  }
  .card-header {
    padding: 11px 12px;
  }
  .quick-action-item,
  .alert-item,
  .todo-item {
    padding-left: 12px;
    padding-right: 12px;
  }
  .voucher-card {
    overflow-x: auto;
  }
}
</style>
