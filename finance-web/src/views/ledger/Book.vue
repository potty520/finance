<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">账簿查询</h2>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="会计年度"><el-input v-model="query.fiscalYear" placeholder="2026" style="width:120px" /></el-form-item>
        <el-form-item label="会计期间"><el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" /></el-form-item>
        <el-form-item label="科目编码"><el-input v-model="query.subjectCode" style="width:160px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" border>
      <el-table-column prop="subjectCode" label="科目编码" width="160" />
      <el-table-column prop="subjectName" label="科目名称" width="160" />
      <el-table-column prop="periodCode" label="期间" width="100" />
      <el-table-column prop="openingDebit" label="期初借方" align="right" />
      <el-table-column prop="openingCredit" label="期初贷方" align="right" />
      <el-table-column prop="periodDebit" label="本期借方" align="right" />
      <el-table-column prop="periodCredit" label="本期贷方" align="right" />
      <el-table-column prop="endingDebit" label="期末借方" align="right" />
      <el-table-column prop="endingCredit" label="期末贷方" align="right" />
    </el-table>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import request from '@/utils/request'

const query = reactive({ fiscalYear: '2026', fiscalPeriod: 6, subjectCode: '' })
const data = reactive({ list: [] })

const loadData = async () => {
  const params = { fiscalYear: query.fiscalYear, fiscalPeriod: query.fiscalPeriod }
  const url = query.subjectCode ? '/ledger/balance/bySubject' : '/ledger/balance/list'
  if (query.subjectCode) params.subjectCode = query.subjectCode
  const res = await request({ url, method: 'get', params })
  data.list = res.data
}

onMounted(loadData)
</script>
