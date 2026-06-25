<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">资产负债表</h2>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="年度"><el-input v-model="query.fiscalYear" /></el-form-item>
        <el-form-item label="期间"><el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" /></el-form-item>
        <el-form-item><el-button type="primary" @click="onQuery">查询</el-button><el-button @click="onExport">导出</el-button></el-form-item>
      </el-form>
    </div>
    <h3>资产</h3>
    <el-table :data="data.asset" border show-summary :summary-method="summaryAsset">
      <el-table-column prop="name" label="项目" />
      <el-table-column prop="endBalance" label="期末余额" align="right" />
      <el-table-column prop="beginBalance" label="年初余额" align="right" />
    </el-table>
    <h3 style="margin-top:16px;">负债和所有者权益</h3>
    <el-table :data="data.liab" border show-summary :summary-method="summaryLiab">
      <el-table-column prop="name" label="项目" />
      <el-table-column prop="endBalance" label="期末余额" align="right" />
      <el-table-column prop="beginBalance" label="年初余额" align="right" />
    </el-table>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const query = reactive({ fiscalYear: '2026', fiscalPeriod: 6 })
const data = reactive({ asset: [], liab: [], assetTotal: 0, liabEquityTotal: 0 })

const onQuery = async () => {
  const res = await request({ url: '/report/balanceSheet', method: 'post', data: query })
  data.asset = res.data.items?.filter(x => x.type === 'asset') || []
  data.liab = res.data.items?.filter(x => x.type === 'liab') || []
  data.assetTotal = res.data.assetTotal || 0
  data.liabEquityTotal = res.data.liabEquityTotal || 0
}

const summaryAsset = () => ['合计', data.assetTotal, '']
const summaryLiab = () => ['合计', data.liabEquityTotal, '']
const onExport = async () => {
  try {
    const token = localStorage.getItem('token')
    const resp = await fetch('/api/report/export/balanceSheet', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
      body: JSON.stringify(query)
    })
    const blob = await resp.blob()
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a'); a.href = url; a.download = `资产负债表_${query.fiscalYear}_${query.fiscalPeriod}.csv`
    document.body.appendChild(a); a.click(); document.body.removeChild(a)
    URL.revokeObjectURL(url); ElMessage.success('导出成功')
  } catch(e) { ElMessage.error('导出失败') }
}

onQuery()
</script>
