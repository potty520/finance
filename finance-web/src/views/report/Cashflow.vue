<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">现金流量表（企业会计准则）</h2>
      <div>
        <el-button type="primary" size="small" @click="onExportExcel">导出Excel</el-button>
        <el-button size="small" @click="onPrint">打印</el-button>
      </div>
    </div>
    <div style="text-align:center;margin:8px 0;font-size:13px;color:#666;">
      <span>年度：<el-input-number v-model="query.fiscalYear" :min="2000" :max="2099" size="small" style="width:100px;margin:0 4px;" /></span>
      <span style="margin-left:16px;">期间：<el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" size="small" style="width:80px;margin:0 4px;" />月</span>
      <el-button type="primary" size="small" style="margin-left:12px;" @click="onQuery">查询</el-button>
    </div>

    <div style="text-align:center;font-size:14px;font-weight:bold;margin:12px 0;">{{ query.fiscalYear }}年 {{ query.fiscalPeriod }}月&emsp;单位：元</div>

    <!-- 国标现金流量表 -->
    <el-table :data="displayList" border stripe size="small" :show-header="false" style="width:100%;max-width:900px;margin:0 auto;">
      <el-table-column label="项目" min-width="280">
        <template #default="{ row }">
          <span :style="{fontWeight:row.bold?'bold':'normal',paddingLeft:(row.ind||0)+'em'}">{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="行次" width="60" align="center">
        <template #default="{ row }">{{ row.line || '' }}</template>
      </el-table-column>
      <el-table-column label="本期金额" width="140" align="right">
        <template #default="{ row }">
          <span :style="row.bold?{fontWeight:'bold'}:{}">{{ row.amount != null ? row.amount.toLocaleString('zh-CN',{minimumFractionDigits:2,maximumFractionDigits:2}) : '' }}</span>
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
const raw = reactive({ operatingItems: [], investingItems: [], financingItems: [], net: 0 })

const findAmount = (items, code) => {
  const it = items.find(x => x.code === code)
  return it ? Number(it.amount || 0) : 0
}

const sumItems = (items) => items.reduce((s, x) => s + Number(x.amount || 0), 0)

const displayList = computed(() => {
  const oi = raw.operatingItems
  const ii = raw.investingItems
  const fi = raw.financingItems
  if (!oi.length && !ii.length && !fi.length) return []
  const L = []
  const row = (name, line, amount, bold, indent) =>
    L.push({ name, line, amount, bold: !!bold, indent: indent || 0 })

  // 一、经营活动现金流量
  row('一、经营活动产生的现金流量：', '', null, true)
  row('  销售商品、提供劳务收到的现金', 1, findAmount(oi, 'CF01'))
  row('  收到的税费返还', 2, findAmount(oi, 'CF02'))
  row('  收到其他与经营活动有关的现金', 3, findAmount(oi, 'CF03'))
  const operIn = findAmount(oi,'CF01') + findAmount(oi,'CF02') + findAmount(oi,'CF03')
  row('    经营活动现金流入小计', 4, operIn, true)
  row('  购买商品、接受劳务支付的现金', 5, Math.abs(findAmount(oi, 'CF04')))
  row('  支付给职工以及为职工支付的现金', 6, Math.abs(findAmount(oi, 'CF05')))
  row('  支付的各项税费', 7, Math.abs(findAmount(oi, 'CF06')))
  row('  支付其他与经营活动有关的现金', 8, Math.abs(findAmount(oi, 'CF07')))
  const operOut = findAmount(oi,'CF04') + findAmount(oi,'CF05') + findAmount(oi,'CF06') + findAmount(oi,'CF07')
  row('    经营活动现金流出小计', 9, operOut, true)
  const operNet = operIn + operOut
  row('  经营活动产生的现金流量净额', 10, operNet, true)
  L.push({ name: '', line: '', amount: null, bold: false })

  // 二、投资活动现金流量
  const invIn = findAmount(ii,'CF08') + findAmount(ii,'CF09') + findAmount(ii,'CF10')
  const invOut = findAmount(ii,'CF11') + findAmount(ii,'CF12')
  const invNet = invIn + invOut
  row('二、投资活动产生的现金流量：', '', null, true)
  row('  收回投资收到的现金', 11, findAmount(ii,'CF08'))
  row('  取得投资收益收到的现金', 12, findAmount(ii,'CF09'))
  row('  处置固定资产收回的现金净额', 13, findAmount(ii,'CF10'))
  row('    投资活动现金流入小计', 14, invIn, true)
  row('  购建固定资产支付的现金', 15, Math.abs(findAmount(ii, 'CF11')))
  row('  投资支付的现金', 16, Math.abs(findAmount(ii, 'CF12')))
  row('    投资活动现金流出小计', 17, invOut, true)
  row('  投资活动产生的现金流量净额', 18, invNet, true)
  L.push({ name: '', line: '', amount: null, bold: false })

  // 三、筹资活动现金流量
  row('三、筹资活动产生的现金流量：', '', null, true)
  const finIn = findAmount(fi,'CF13') + findAmount(fi,'CF14')
  const finOut = findAmount(fi,'CF15') + findAmount(fi,'CF16')
  const finNet = finIn + finOut
  row('  取得借款收到的现金', 19, findAmount(fi,'CF13'))
  row('  吸收投资收到的现金', 20, findAmount(fi,'CF14'))
  row('    筹资活动现金流入小计', 21, finIn, true)
  row('  偿还债务支付的现金', 22, Math.abs(findAmount(fi, 'CF15')))
  row('  分配股利、利润或偿付利息支付的现金', 23, Math.abs(findAmount(fi, 'CF16')))
  row('    筹资活动现金流出小计', 24, finOut, true)
  row('  筹资活动产生的现金流量净额', 25, finNet, true)
  L.push({ name: '', line: '', amount: null, bold: false })

  // 四、汇率变动
  row('四、汇率变动对现金及现金等价物的影响', 26, 0)
  L.push({ name: '', line: '', amount: null, bold: false })

  // 五、现金净增加额
  const netIncrease = operNet + invNet + finNet
  row('五、现金及现金等价物净增加额', 27, netIncrease, true)
  row('  期初现金及现金等价物余额', 28, 0)
  row('  期末现金及现金等价物余额', 29, netIncrease, true)

  return L
})

const onQuery = async () => {
  const res = await request({ url: '/report/cashFlow', method: 'post',
    data: { fiscalYear: String(query.fiscalYear), fiscalPeriod: query.fiscalPeriod } })
  raw.operatingItems = res.data?.operatingItems || []
  raw.investingItems = res.data?.investingItems || []
  raw.financingItems = res.data?.financingItems || []
  raw.net = Number(res.data?.net || 0)
}

const onExportExcel = async () => {
  try {
    const token = localStorage.getItem('token')
    const resp = await fetch('/api/report/export/excel/cashFlow', {
      method: 'POST', headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
      body: JSON.stringify({ fiscalYear: String(query.fiscalYear), fiscalPeriod: query.fiscalPeriod })
    })
    const blob = await resp.blob()
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a'); a.href = url; a.download = `现金流量表_${query.fiscalYear}_${query.fiscalPeriod}.xlsx`
    document.body.appendChild(a); a.click(); document.body.removeChild(a)
    URL.revokeObjectURL(url); ElMessage.success('导出成功')
  } catch (e) { ElMessage.error('导出失败') }
}

const onPrint = async () => {
  try {
    const token = localStorage.getItem('token')
    const resp = await fetch('/api/report/export/print/cashFlow', {
      method: 'POST', headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
      body: JSON.stringify({ fiscalYear: String(query.fiscalYear), fiscalPeriod: query.fiscalPeriod })
    })
    const html = await resp.text()
    const w = window.open('', '_blank')
    w.document.write(html); w.document.close()
  } catch (e) { ElMessage.error('打印失败') }
}

onQuery()
</script>
