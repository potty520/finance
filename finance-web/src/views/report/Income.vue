<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">利润表（企业会计准则）</h2>
      <div>
        <el-button type="primary" size="small" @click="onExportExcel">导出Excel</el-button>
        <el-button size="small" @click="onPrint">打印</el-button>
      </div>
    </div>
    <div style="text-align:center;margin:8px 0;font-size:13px;color:#666;">
      <span>年度：<el-input-number v-model="query.fiscalYear" :min="2000" :max="2099" size="small" style="width:100px;margin:0 4px;" /></span>
      <span style="margin-left:16px;">期间：<el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" size="small" style="width:80px;margin:0 4px;" />
      月</span>
      <el-button type="primary" size="small" style="margin-left:12px;" @click="onQuery">查询</el-button>
    </div>

    <div style="text-align:center;font-size:14px;font-weight:bold;margin:12px 0;">{{ query.fiscalYear }}年 {{ query.fiscalPeriod }}月&emsp;单位：元</div>

    <!-- 国标多步式利润表 -->
    <el-table :data="displayList" border stripe size="small" :show-header="false" style="width:100%;max-width:900px;margin:0 auto;">
      <el-table-column label="项目" min-width="200">
        <template #default="{ row }">
          <span :style="{fontWeight:row.bold?'bold':'normal',paddingLeft:(row.indent||0)+'em'}">{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="行次" width="60" align="center">
        <template #default="{ row }">{{ row.line || '' }}</template>
      </el-table-column>
      <el-table-column label="本期金额" width="140" align="right">
        <template #default="{ row }">
          <span :style="row.bold?{fontWeight:'bold'}:{}">{{ row.cur != null ? row.cur.toLocaleString('zh-CN',{minimumFractionDigits:2,maximumFractionDigits:2}) : '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="本年累计" width="140" align="right">
        <template #default="{ row }">
          <span :style="row.bold?{fontWeight:'bold'}:{}">{{ row.cum != null ? row.cum.toLocaleString('zh-CN',{minimumFractionDigits:2,maximumFractionDigits:2}) : '' }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const query = reactive({ fiscalYear: 2026, fiscalPeriod: 6 })
const raw = reactive({ items: [], revenue: 0, cost: 0, expense: 0, profit: 0, profitYear: 0 })

const codeMap = {}
const absCur = (code) => {
  const it = codeMap[code]
  return it ? Math.abs(it.current) : 0
}
const absCum = (code) => {
  const it = codeMap[code]
  return it ? Math.abs(it.cumulative) : 0
}

const displayList = computed(() => {
  if (!raw.items.length) return []
  raw.items.forEach(it => { codeMap[it.code] = it })
  const L = []
  const row = (name, line, cur, cum, bold, indent) => L.push({ name, line, cur, cum, bold: !!bold, indent: indent || 0 })

  // 一、营业收入
  const rev60 = absCur('6001'), rev60c = absCum('6001')
  const rev61 = absCur('6051'), rev61c = absCum('6051')
  const totalRev = rev60 + rev61, totalRevC = rev60c + rev61c
  row('一、营业收入', '', null, null, true)
  row('  (一)主营业务收入', 1, rev60, rev60c)
  row('  (二)其他业务收入', 2, rev61, rev61c)
  row('　　营业收入合计', 3, totalRev, totalRevC, true)

  L.push({ name: '', line: '', cur: null, cum: null, bold: false })

  // 二、营业成本
  const cost54 = absCur('5401'), cost54c = absCum('5401')
  const cost64 = absCur('6401'), cost64c = absCum('6401')
  const cost66 = absCur('6601') + absCur('6602') + absCur('6603')
  const cost66c = absCum('6601') + absCum('6602') + absCum('6603')
  const totalCost = cost54 + cost64 + cost66
  const totalCostC = cost54c + cost64c + cost66c
  const operProfit = totalRev - totalCost, operProfitC = totalRevC - totalCostC

  row('二、营业成本', '', null, null, true)
  row('  (一)主营业务成本', 4, cost54, cost54c)
  row('  (二)税金及附加', 5, cost64, cost64c)
  row('  (三)期间费用', 6, cost66, cost66c)
  row('　　其中：销售费用', 7, absCur('6601'), absCum('6601'))
  row('　　　　　管理费用', 8, absCur('6602'), absCum('6602'))
  row('　　　　　财务费用', 9, absCur('6603'), absCum('6603'))
  row('　　营业成本合计', 10, totalCost, totalCostC, true)
  L.push({ name: '', line: '', cur: null, cum: null, bold: false })

  // 三、营业利润
  row('三、营业利润（亏损以"-"号填列）', 11, operProfit, operProfitC, true)
  L.push({ name: '', line: '', cur: null, cum: null, bold: false })

  const nonOpInc = absCur('6301') + absCur('6111'), nonOpIncC = absCum('6301') + absCum('6111')
  const nonOpExp = absCur('6711'), nonOpExpC = absCum('6711')
  const totalProfit = operProfit + nonOpInc - nonOpExp
  const totalProfitC = operProfitC + nonOpIncC - nonOpExpC

  row('  加：营业外收入', 12, nonOpInc, nonOpIncC)
  row('  减：营业外支出', 13, nonOpExp, nonOpExpC)
  L.push({ name: '', line: '', cur: null, cum: null, bold: false })

  // 四、利润总额
  row('四、利润总额（亏损以"-"号填列）', 14, totalProfit, totalProfitC, true)
  L.push({ name: '', line: '', cur: null, cum: null, bold: false })

  const tax = absCur('6801'), taxC = absCum('6801')
  const netProfit = totalProfit - tax, netProfitC = totalProfitC - taxC
  row('  减：所得税费用', 15, tax, taxC)
  L.push({ name: '', line: '', cur: null, cum: null, bold: false })

  // 五、净利润
  row('五、净利润（亏损以"-"号填列）', 16, netProfit, netProfitC, true)

  return L
})

const onQuery = async () => {
  const res = await request({ url: '/report/incomeStatement', method: 'post', data: { fiscalYear: String(query.fiscalYear), fiscalPeriod: query.fiscalPeriod } })
  raw.items = res.data.items || []
  raw.revenue = Number(res.data.revenue || 0)
  raw.profit = Number(res.data.profit || 0)
  raw.profitYear = Number(res.data.profitYear || 0)
}

const onExportExcel = async () => {
  try {
    const token = localStorage.getItem('token')
    const resp = await fetch('/api/report/export/excel/incomeStatement', {
      method: 'POST', headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
      body: JSON.stringify({ fiscalYear: String(query.fiscalYear), fiscalPeriod: query.fiscalPeriod })
    })
    const blob = await resp.blob()
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a'); a.href = url; a.download = `利润表_${query.fiscalYear}_${query.fiscalPeriod}.xlsx`
    document.body.appendChild(a); a.click(); document.body.removeChild(a)
    URL.revokeObjectURL(url); ElMessage.success('导出成功')
  } catch (e) { ElMessage.error('导出失败') }
}

const onPrint = async () => {
  try {
    const token = localStorage.getItem('token')
    const resp = await fetch('/api/report/export/print/incomeStatement', {
      method: 'POST', headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
      body: JSON.stringify({ fiscalYear: String(query.fiscalYear), fiscalPeriod: query.fiscalPeriod })
    })
    const html = await resp.text()
    const w = window.open('','_blank')
    w.document.write(html); w.document.close()
  } catch (e) { ElMessage.error('打印失败') }
}

// 初始化加载
onQuery()
</script>
