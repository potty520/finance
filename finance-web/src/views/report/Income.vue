<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">利润表</h2>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="年度"><el-input v-model="query.fiscalYear" /></el-form-item>
        <el-form-item label="期间"><el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" /></el-form-item>
        <el-form-item><el-button type="primary" @click="onQuery">查询</el-button><el-button @click="onExport">导出</el-button></el-form-item>
      </el-form>
    </div>
    <el-row :gutter="16" style="margin-bottom: 16px;">
      <el-col :span="6"><el-statistic title="营业收入(本期)" :value="data.revenue" :precision="2" /></el-col>
      <el-col :span="6"><el-statistic title="营业成本(本期)" :value="data.cost" :precision="2" /></el-col>
      <el-col :span="6"><el-statistic title="期间费用(本期)" :value="data.expense" :precision="2" /></el-col>
      <el-col :span="6"><el-statistic title="净利润(本期)" :value="data.profit" :precision="2" /></el-col>
    </el-row>
    <el-table :data="data.list" border show-summary :summary-method="summaryMethod">
      <el-table-column prop="name" label="项目" />
      <el-table-column prop="current" label="本期金额" align="right" />
      <el-table-column prop="cumulative" label="本年累计" align="right" />
    </el-table>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const query = reactive({ fiscalYear: '2026', fiscalPeriod: 6 })
const data = reactive({
  list: [],
  revenue: 0,
  cost: 0,
  expense: 0,
  profit: 0,
  profitYear: 0
})

const onQuery = async () => {
  const res = await request({ url: '/report/incomeStatement', method: 'post', data: query })
  data.list = res.data.items || []
  data.revenue = Number(res.data.revenue || 0)
  data.cost = Number(res.data.cost || 0)
  data.expense = Number(res.data.expense || 0)
  data.profit = Number(res.data.profit || 0)
  data.profitYear = Number(res.data.profitYear || 0)
}

const summaryMethod = () => ['净利润', data.profit, data.profitYear]
const onExport = async () => {
  try {
    const token = localStorage.getItem('token')
    const resp = await fetch('/api/report/export/incomeStatement', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
      body: JSON.stringify(query)
    })
    const blob = await resp.blob()
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a'); a.href = url; a.download = `利润表_${query.fiscalYear}_${query.fiscalPeriod}.csv`
    document.body.appendChild(a); a.click(); document.body.removeChild(a)
    URL.revokeObjectURL(url); ElMessage.success('导出成功')
  } catch(e) { ElMessage.error('导出失败') }
}

onQuery()
</script>
