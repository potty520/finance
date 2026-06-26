<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">资产负债表（企业会计准则）</h2>
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

    <div style="text-align:center;font-size:14px;font-weight:bold;margin:12px 0;">{{ query.fiscalYear }}年 {{ query.fiscalPeriod }}月 {{ query.fiscalDay }}日&emsp;单位：元</div>

    <!-- 国标双列资产负债表 -->
    <el-table :data="displayList" border stripe size="small" :show-header="true" style="width:100%;max-width:1100px;margin:0 auto;">
      <el-table-column label="资产" min-width="190">
        <template #default="{ row }">
          <span :style="{fontWeight:row.ab?'bold':'normal',paddingLeft:(row.ind||0)+'em'}">{{ row.an || '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="行次" width="45" align="center">
        <template #default="{ row }">{{ row.al || '' }}</template>
      </el-table-column>
      <el-table-column label="期末余额" width="130" align="right">
        <template #default="{ row }">
          <span :style="row.ab?{fontWeight:'bold'}:{}">{{ row.ae != null ? row.ae.toLocaleString('zh-CN',{minimumFractionDigits:2,maximumFractionDigits:2}) : '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="年初余额" width="130" align="right">
        <template #default="{ row }">
          <span>{{ row.ab0 != null ? row.ab0.toLocaleString('zh-CN',{minimumFractionDigits:2,maximumFractionDigits:2}) : '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="负债和所有者权益" min-width="190">
        <template #default="{ row }">
          <span :style="{fontWeight:row.lb?'bold':'normal',paddingLeft:(row.ind||0)+'em'}">{{ row.ln || '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="行次" width="45" align="center">
        <template #default="{ row }">{{ row.ll || '' }}</template>
      </el-table-column>
      <el-table-column label="期末余额" width="130" align="right">
        <template #default="{ row }">
          <span :style="row.lb?{fontWeight:'bold'}:{}">{{ row.le != null ? row.le.toLocaleString('zh-CN',{minimumFractionDigits:2,maximumFractionDigits:2}) : '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="年初余额" width="130" align="right">
        <template #default="{ row }">
          <span>{{ row.lb0 != null ? row.lb0.toLocaleString('zh-CN',{minimumFractionDigits:2,maximumFractionDigits:2}) : '' }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const query = reactive({ fiscalYear: 2026, fiscalPeriod: 6, fiscalDay: 30 })

const raw = reactive({ items: [] })

// 按 code 找金额
const codeVal = (code, field) => {
  const it = raw.items?.find(x => x.code === code)
  return it ? Number(it[field] || 0) : 0
}

const displayList = computed(() => {
  if (!raw.items?.length) return []
  const L = []
  const addR = (an, al, ae, ab0, ab, ln, ll, le, lb0, lb, ind) => {
    L.push({ an: an||'', al: al||'', ae, ab0, ab: !!ab, ln: ln||'', ll: ll||'', le, lb0, lb: !!lb, ind: ind||0 })
  }
  const va = (c, f) => codeVal(c, f)
  const end = (c) => va(c, 'endBalance'), beg = (c) => va(c, 'beginBalance')

  // 资产部分
  const sumAsset = (codes, f) => codes.reduce((s, c) => s + va(c, f), 0)

  // 流动资产
  const curAssetCodes = ['1001','1002','1012','1101','1121','1122','1123','1131','1132','1221','1231',
    '1301','1401','1402','1403','1405','1406','1411','1413','1421','1431','1471','1481','1491','1501','1521']
  const curEnd = sumAsset(curAssetCodes, 'endBalance')
  const curBeg = sumAsset(curAssetCodes, 'beginBalance')

  // 非流动资产
  const nonCurAssetCodes = ['1511','1512','1522','1531','1601','1602','1603','1604','1621','1701','1702','1703',
    '1704','1711','1801','1811','1821','1901','1902']
  const nonCurEnd = sumAsset(nonCurAssetCodes, 'endBalance')
  const nonCurBeg = sumAsset(nonCurAssetCodes, 'beginBalance')
  const assetTotalEnd = curEnd + nonCurEnd
  const assetTotalBeg = curBeg + nonCurBeg

  addR('流动资产：', '', null, null, true, '', '', null, null, false)
  addR('  货币资金', 1, end('1001')+end('1002')+end('1012'), beg('1001')+beg('1002')+beg('1012'), false, '', '', null, null, false)
  addR('  应收账款', 2, end('1122'), beg('1122'), false, '', '', null, null, false)
  addR('  预付款项', 3, end('1123'), beg('1123'), false, '', '', null, null, false)
  addR('  其他应收款', 4, end('1221'), beg('1221'), false, '', '', null, null, false)
  addR('  存货', 5, end('1405'), beg('1405'), false, '', '', null, null, false)
  addR('  流动资产合计', 6, curEnd, curBeg, true, '', '', null, null, false)
  addR('', '', null, null, false, '', '', null, null, false)

  addR('非流动资产：', '', null, null, true, '', '', null, null, false)
  addR('  长期股权投资', 7, end('1511'), beg('1511'), false, '', '', null, null, false)
  addR('  固定资产', 8, end('1601')-end('1602'), beg('1601')-beg('1602'), false, '', '', null, null, false)
  addR('  在建工程', 9, end('1604'), beg('1604'), false, '', '', null, null, false)
  addR('  无形资产', 10, end('1701')-end('1702'), beg('1701')-beg('1702'), false, '', '', null, null, false)
  addR('  非流动资产合计', 11, nonCurEnd, nonCurBeg, true, '', '', null, null, false)
  addR('', '', null, null, false, '', '', null, null, false)
  addR('资产总计', 12, assetTotalEnd, assetTotalBeg, true, '', '', null, null, false)

  // 负债
  const liabEnd = end('2202') + end('222101') + end('222103') + end('222106')
  const liabBeg = beg('2202') + beg('222101') + beg('222103') + beg('222106')

  // 权益
  const eqEnd = end('4001') + end('4103')
  const eqBeg = beg('4001') + beg('4103')
  const leTotalEnd = liabEnd + eqEnd
  const leTotalBeg = liabBeg + eqBeg

  // 填充到已有行
  L[0].ln = '负债和所有者权益：'; L[0].lb = true
  L[1].ln = '  短期借款'; L[1].ll = 13; L[1].le = end('2202'); L[1].lb0 = beg('2202')
  L[2].ln = '  应付账款'; L[2].ll = 14; L[2].le = end('222101'); L[2].lb0 = beg('222101')
  L[3].ln = '  应付职工薪酬'; L[3].ll = 15; L[3].le = end('222103'); L[3].lb0 = beg('222103')
  L[4].ln = '  应交税费'; L[4].ll = 16; L[4].le = end('222106'); L[4].lb0 = beg('222106')
  L[5].ln = '  负债合计'; L[5].ll = 17; L[5].le = liabEnd; L[5].lb0 = liabBeg; L[5].lb = true
  L[6].ln = ''; L[6].le = null; L[6].lb0 = null; L[6].lb = false
  L[7].ln = '所有者权益：'; L[7].lb = true
  L[8].ln = '  实收资本'; L[8].ll = 18; L[8].le = end('4001'); L[8].lb0 = beg('4001')
  L[9].ln = '  未分配利润'; L[9].ll = 19; L[9].le = end('4103'); L[9].lb0 = beg('4103')
  L[10].ln = '  所有者权益合计'; L[10].ll = 20; L[10].le = eqEnd; L[10].lb0 = eqBeg; L[10].lb = true
  L[11].ln = ''; L[11].le = null; L[11].lb0 = null; L[11].lb = false
  L[12].ln = '负债和所有者权益总计'; L[12].ll = 21; L[12].le = leTotalEnd; L[12].lb0 = leTotalBeg; L[12].lb = true

  return L
})

const onQuery = async () => {
  const res = await request({ url: '/report/balanceSheet', method: 'post',
    data: { fiscalYear: String(query.fiscalYear), fiscalPeriod: query.fiscalPeriod } })
  raw.items = res.data?.items || []
  // 推算月末日期
  const d = new Date(query.fiscalYear, query.fiscalPeriod, 0)
  query.fiscalDay = d.getDate()
}

const onExportExcel = async () => {
  try {
    const token = localStorage.getItem('token')
    const resp = await fetch('/api/report/export/excel/balanceSheet', {
      method: 'POST', headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
      body: JSON.stringify({ fiscalYear: String(query.fiscalYear), fiscalPeriod: query.fiscalPeriod })
    })
    const blob = await resp.blob()
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a'); a.href = url; a.download = `资产负债表_${query.fiscalYear}_${query.fiscalPeriod}.xlsx`
    document.body.appendChild(a); a.click(); document.body.removeChild(a)
    URL.revokeObjectURL(url); ElMessage.success('导出成功')
  } catch (e) { ElMessage.error('导出失败') }
}

const onPrint = async () => {
  try {
    const token = localStorage.getItem('token')
    const resp = await fetch('/api/report/export/print/balanceSheet', {
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
