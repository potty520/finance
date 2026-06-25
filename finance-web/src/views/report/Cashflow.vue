<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">现金流量表</h2>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="年度"><el-input v-model="query.fiscalYear" /></el-form-item>
        <el-form-item label="期间"><el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" /></el-form-item>
        <el-form-item><el-button type="primary" @click="onQuery">查询</el-button><el-button @click="onExport">导出</el-button></el-form-item>
      </el-form>
    </div>
    <el-row :gutter="16" style="margin-bottom: 16px;">
      <el-col :span="6"><el-statistic title="经营活动净额" :value="data.operatingTotal" :precision="2" /></el-col>
      <el-col :span="6"><el-statistic title="投资活动净额" :value="data.investingTotal" :precision="2" /></el-col>
      <el-col :span="6"><el-statistic title="筹资活动净额" :value="data.financingTotal" :precision="2" /></el-col>
      <el-col :span="6"><el-statistic title="现金净增加额" :value="data.netTotal" :precision="2" /></el-col>
    </el-row>
    <h3>经营活动现金流量</h3>
    <el-table :data="data.operating" border show-summary :summary-method="() => ['小计', data.operatingTotal]">
      <el-table-column prop="name" label="项目" />
      <el-table-column prop="amount" label="金额" align="right" />
    </el-table>
    <h3 style="margin-top:16px;">投资活动现金流量</h3>
    <el-table :data="data.investing" border show-summary :summary-method="() => ['小计', data.investingTotal]">
      <el-table-column prop="name" label="项目" />
      <el-table-column prop="amount" label="金额" align="right" />
    </el-table>
    <h3 style="margin-top:16px;">筹资活动现金流量</h3>
    <el-table :data="data.financing" border show-summary :summary-method="() => ['小计', data.financingTotal]">
      <el-table-column prop="name" label="项目" />
      <el-table-column prop="amount" label="金额" align="right" />
    </el-table>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const query = reactive({ fiscalYear: '2026', fiscalPeriod: 6 })
const data = reactive({
  operating: [],
  investing: [],
  financing: [],
  operatingTotal: 0,
  investingTotal: 0,
  financingTotal: 0,
  netTotal: 0
})

const onQuery = async () => {
  const res = await request({ url: '/report/cashFlow', method: 'post', data: query })
  data.operating = res.data.operatingItems || []
  data.investing = res.data.investingItems || []
  data.financing = res.data.financingItems || []
  data.operatingTotal = Number(res.data.operating || 0)
  data.investingTotal = Number(res.data.investing || 0)
  data.financingTotal = Number(res.data.financing || 0)
  data.netTotal = Number(res.data.net || 0)
}
const onExport = async () => {
  try {
    const token = localStorage.getItem('token')
    const resp = await fetch('/api/report/export/cashFlow', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
      body: JSON.stringify(query)
    })
    const blob = await resp.blob()
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a'); a.href = url; a.download = `现金流量表_${query.fiscalYear}_${query.fiscalPeriod}.csv`
    document.body.appendChild(a); a.click(); document.body.removeChild(a)
    URL.revokeObjectURL(url); ElMessage.success('导出成功')
  } catch(e) { ElMessage.error('导出失败') }
}

onQuery()
</script>
