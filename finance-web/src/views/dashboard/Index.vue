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
            <div class="stat-label">本月收入</div>
            <div class="stat-value">¥1,256,890</div>
            <div class="stat-change up">
              <el-icon><CaretTop /></el-icon> 12.5% <span class="stat-vs">vs 上月</span>
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
            <div class="stat-label">本月支出</div>
            <div class="stat-value">¥856,420</div>
            <div class="stat-change down">
              <el-icon><CaretBottom /></el-icon> 4.8% <span class="stat-vs">vs 上月</span>
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
            <div class="stat-label">应收账款</div>
            <div class="stat-value">¥432,150</div>
            <div class="stat-extra">
              <el-tag type="danger" size="small" effect="dark">3笔即将到期</el-tag>
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
            <div class="stat-label">应付账款</div>
            <div class="stat-value">¥218,750</div>
            <div class="stat-extra">
              <el-tag type="warning" size="small" effect="dark">5笔待支付</el-tag>
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
          <h3>月度收支趋势</h3>
          <div class="card-header-right">
            <el-radio-group v-model="trendPeriod" size="small">
              <el-radio-button value="6m">近6个月</el-radio-button>
              <el-radio-button value="12m">近12个月</el-radio-button>
            </el-radio-group>
          </div>
        </div>
        <v-chart :option="trendOption" autoresize style="height: 280px;" />
      </div>
    </div>

    <!-- ===== 下排：预警 + 待办 + 最近凭证 ===== -->
    <div class="dashboard-bottom">
      <!-- 预警 -->
      <div class="alert-card">
        <div class="card-header">
          <h3>
            <el-badge :value="alerts.length" class="alert-badge">业务预警</el-badge>
          </h3>
        </div>
        <div class="alert-list">
          <div class="alert-item" v-for="(item, idx) in alerts" :key="idx" :class="'alert-' + item.levelType">
            <div class="alert-dot" :class="'dot-' + item.levelType"></div>
            <div class="alert-content">
              <div class="alert-title">{{ item.title }}</div>
              <div class="alert-time">{{ item.time }}</div>
            </div>
            <el-tag :type="tagType(item.levelType)" size="small" effect="plain">{{ item.level }}</el-tag>
          </div>
        </div>
      </div>

      <!-- 最近凭证 -->
      <div class="voucher-card">
        <div class="card-header">
          <h3>最近凭证</h3>
          <el-button link type="primary" @click="router.push('/ledger/voucher-list')">查看全部 →</el-button>
        </div>
        <el-table :data="recentVouchers" stripe size="small">
          <el-table-column prop="voucherNo" label="凭证号" width="130" />
          <el-table-column prop="date" label="日期" width="100" />
          <el-table-column prop="summary" label="摘要" min-width="160" show-overflow-tooltip />
          <el-table-column prop="amount" label="金额" width="120" align="right">
            <template #default="{ row }">
              <span class="amount">¥{{ row.amount }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80" align="center">
            <template #default="{ row }">
              <el-tag :type="row.status === '已审核' ? 'success' : 'info'" size="small">
                {{ row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 待办 -->
      <div class="todo-card">
        <div class="card-header">
          <h3>我的待办</h3>
          <el-tag type="warning" size="small" effect="dark" round>{{ todos.length }} 项</el-tag>
        </div>
        <div class="todo-list">
          <div class="todo-item" v-for="(item, idx) in todos" :key="idx">
            <div class="todo-left">
              <div class="todo-type-tag" :class="'type-' + item.tagType">{{ item.tag }}</div>
            </div>
            <div class="todo-center">
              <div class="todo-biz">{{ item.bizNo }}</div>
              <div class="todo-meta">
                <span>{{ item.applicant }}</span>
                <span class="todo-dot">·</span>
                <span>{{ item.date }}</span>
              </div>
            </div>
            <div class="todo-right">
              <span class="todo-amount">¥{{ item.amount }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart, PieChart } from 'echarts/charts'
import {
  TitleComponent, TooltipComponent, LegendComponent, GridComponent
} from 'echarts/components'
import {
  TrendCharts, Money, Sell, ShoppingCart, CaretTop, CaretBottom,
  EditPen, DocumentAdd, Upload, CreditCard,
  Wallet, Notebook, ArrowRight
} from '@element-plus/icons-vue'

use([CanvasRenderer, LineChart, BarChart, PieChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent])

const router = useRouter()
const trendPeriod = ref('6m')

// 快捷操作
const quickActions = [
  { label: '录入凭证', desc: '记一笔', icon: EditPen, path: '/ledger/voucher', bg: 'linear-gradient(135deg, #409eff, #66b1ff)' },
  { label: '费用报销', desc: '提交报销单', icon: DocumentAdd, path: '/expense/apply', bg: 'linear-gradient(135deg, #67c23a, #95d475)' },
  { label: '收款单', desc: '登记收款', icon: Upload, path: '/receivable/receipt', bg: 'linear-gradient(135deg, #e6a23c, #ebb563)' },
  { label: '付款单', desc: '登记付款', icon: CreditCard, path: '/payable/payment', bg: 'linear-gradient(135deg, #f56c6c, #f89898)' },
  { label: '银行对账', desc: '核对银行流水', icon: Wallet, path: '/cashier/bank', bg: 'linear-gradient(135deg, #909399, #b4b8bf)' },
  { label: '科目余额表', desc: '查看余额', icon: Notebook, path: '/report/balance', bg: 'linear-gradient(135deg, #7c3aed, #a78bfa)' }
]

// 趋势图
const trendOption = computed(() => {
  const xData = trendPeriod.value === '6m'
    ? ['1月', '2月', '3月', '4月', '5月', '6月']
    : ['7月', '8月', '9月', '10月', '11月', '12月', '1月', '2月', '3月', '4月', '5月', '6月']
  const incomeData = trendPeriod.value === '6m'
    ? [800, 950, 1100, 1050, 1200, 1256]
    : [780, 820, 900, 950, 880, 1020, 800, 950, 1100, 1050, 1200, 1256]
  const expenseData = trendPeriod.value === '6m'
    ? [600, 720, 850, 800, 900, 856]
    : [580, 650, 720, 780, 750, 800, 600, 720, 850, 800, 900, 856]

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
            ${p.seriesName}: <b>¥${(p.value * 1000).toLocaleString()}</b>
          </div>`
        })
        return html
      }
    },
    legend: {
      bottom: 0,
      textStyle: { color: '#666', fontSize: 12 }
    },
    grid: { top: 10, left: 10, right: 20, bottom: 40 },
    xAxis: {
      type: 'category',
      data: xData,
      axisLine: { lineStyle: { color: '#e4e7ed' } },
      axisTick: { show: false },
      axisLabel: { color: '#909399' }
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { color: '#f2f3f5', type: 'dashed' } },
      axisLabel: {
        color: '#909399',
        formatter: (v) => '¥' + v + 'k'
      }
    },
    series: [
      {
        name: '收入',
        type: 'line',
        data: incomeData,
        smooth: true,
        symbol: 'circle',
        symbolSize: 6,
        lineStyle: { color: '#409eff', width: 2.5 },
        itemStyle: { color: '#409eff' },
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
        name: '支出',
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

// 预警
const alerts = [
  { title: '应收账款到期催收：客户A 50,000元', level: '高', levelType: 'high', time: '2026-06-28到期' },
  { title: '预算超支预警：管理费用超预算12%', level: '中', levelType: 'mid', time: '本月累计' },
  { title: '库存预警：A物料低于安全库存', level: '中', levelType: 'mid', time: '当前库存23件' },
  { title: '固定资产折旧待计提', level: '低', levelType: 'low', time: '6月30日前' }
]

const tagType = (t) => ({ high: 'danger', mid: 'warning', low: 'info' }[t])

// 待办
const todos = [
  { bizNo: 'EX-1700000001', type: '费用申请', tag: '费用', tagType: 'expense', amount: '5,200.00', applicant: '张三', date: '2026-06-22 14:30' },
  { bizNo: 'CT-1700000002', type: '合同审批', tag: '合同', tagType: 'contract', amount: '128,000.00', applicant: '李四', date: '2026-06-22 15:20' },
  { bizNo: 'PJ-1700000003', type: '项目预算', tag: '项目', tagType: 'project', amount: '50,000.00', applicant: '王五', date: '2026-06-23 09:15' },
  { bizNo: 'PY-1700000004', type: '付款审批', tag: '付款', tagType: 'pay', amount: '36,800.00', applicant: '赵六', date: '2026-06-24 11:00' }
]

// 最近凭证
const recentVouchers = [
  { voucherNo: '记-202606-001', date: '2026-06-25', summary: '收到客户A货款', amount: '50,000.00', status: '已审核' },
  { voucherNo: '记-202606-002', date: '2026-06-24', summary: '支付供应商B材料款', amount: '28,500.00', status: '已审核' },
  { voucherNo: '记-202606-003', date: '2026-06-24', summary: '计提本月折旧费用', amount: '12,300.00', status: '草稿' },
  { voucherNo: '记-202606-004', date: '2026-06-23', summary: '销售商品确认收入', amount: '85,000.00', status: '已审核' }
]

const handleQuick = (path) => {
  if (path) router.push(path)
}
</script>

<style scoped lang="scss">
.dashboard {
  max-width: 1400px;
  margin: 0 auto;
}

// ===== 统计卡片 =====
.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}
.stat-card {
  background: #fff;
  border-radius: var(--radius-base);
  padding: 20px 24px;
  box-shadow: var(--shadow-card);
  transition: all var(--transition);
  cursor: pointer;
  overflow: hidden;
  position: relative;
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
  gap: 16px;
  align-items: center;
}
.stat-icon-wrap {
  flex-shrink: 0;
}
.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}
.stat-income {
  .stat-icon { background: linear-gradient(135deg, #409eff, #66b1ff); }
  &::after { background: #409eff; }
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
.stat-label { font-size: 13px; color: var(--text-secondary); margin-bottom: 6px; }
.stat-value { font-size: 26px; font-weight: 700; color: var(--text-primary); margin-bottom: 4px; }
.stat-change { font-size: 12px; display: flex; align-items: center; gap: 2px; }
.stat-change.up { color: var(--success); }
.stat-change.down { color: var(--danger); }
.stat-vs { color: var(--text-secondary); margin-left: 4px; }
.stat-extra { margin-top: 2px; }

// ===== 快捷操作 + 趋势 =====
.dashboard-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.quick-actions-card, .trend-card {
  background: #fff;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-card);
  overflow: hidden;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-base);
  h3 {
    margin: 0;
    font-size: 15px;
    font-weight: 600;
    color: var(--text-primary);
  }
}
.card-header-right { display: flex; align-items: center; }

// 快捷操作
.quick-actions {
  padding: 8px;
}
.quick-action-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 16px;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.2s;
  &:hover {
    background: #f5f7fa;
    .qa-arrow { opacity: 1; transform: translateX(0); }
  }
}
.qa-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}
.qa-info { flex: 1; }
.qa-label { font-size: 14px; font-weight: 500; color: var(--text-primary); }
.qa-desc { font-size: 12px; color: var(--text-secondary); margin-top: 2px; }
.qa-arrow {
  opacity: 0;
  transform: translateX(-8px);
  transition: all 0.2s;
  color: var(--text-placeholder);
  font-size: 14px;
}

// 趋势图
.trend-card {
  padding-bottom: 8px;
}

// ===== 底部三栏 =====
.dashboard-bottom {
  display: grid;
  grid-template-columns: 1fr 1.2fr 0.9fr;
  gap: 20px;
}

.alert-card, .voucher-card, .todo-card {
  background: #fff;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-card);
  overflow: hidden;
}

// 预警列表
.alert-list {
  padding: 8px 0;
}
.alert-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  transition: background 0.2s;
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
.alert-content { flex: 1; min-width: 0; }
.alert-title { font-size: 13px; color: var(--text-primary); margin-bottom: 2px; }
.alert-time { font-size: 11px; color: var(--text-secondary); }
.alert-badge :deep(.el-badge__content) { border: none; }

// 最近凭证
.voucher-card {
  :deep(.el-table) { font-size: 13px; }
  .amount { font-weight: 500; color: var(--text-primary); font-variant-numeric: tabular-nums; }
}

// 待办列表
.todo-list {
  padding: 4px 0;
}
.todo-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 20px;
  transition: background 0.2s;
  cursor: pointer;
  &:hover { background: #fafafa; }
}
.todo-type-tag {
  width: 44px;
  height: 24px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 600;
  color: #fff;
}
.type-expense { background: #409eff; }
.type-contract { background: #e6a23c; }
.type-project { background: #7c3aed; }
.type-pay { background: #f56c6c; }
.todo-center { flex: 1; min-width: 0; }
.todo-biz { font-size: 13px; font-weight: 500; color: var(--text-primary); }
.todo-meta { font-size: 11px; color: var(--text-secondary); margin-top: 2px; }
.todo-dot { margin: 0 4px; }
.todo-amount { font-size: 14px; font-weight: 600; color: var(--text-primary); white-space: nowrap; }

// 响应式
@media (max-width: 1200px) {
  .stat-grid { grid-template-columns: repeat(2, 1fr); }
  .dashboard-grid { grid-template-columns: 1fr; }
  .dashboard-bottom { grid-template-columns: 1fr; }
}
</style>
