<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">预算执行</h2>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="年度"><el-input v-model="query.fiscalYear" /></el-form-item>
        <el-form-item label="期间"><el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" /></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="subjectCode" label="科目编码" width="120" />
      <el-table-column prop="subjectName" label="科目" />
      <el-table-column prop="amount" label="预算金额" align="right" />
      <el-table-column prop="usedAmount" label="已执行" align="right" />
      <el-table-column prop="availableAmount" label="剩余" align="right" />
      <el-table-column label="执行率" width="140">
        <template #default="{ row }">
          <el-progress :percentage="row.amount > 0 ? Math.round(row.usedAmount * 100 / row.amount) : 0" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'

const loading = ref(false)
const data = reactive({ list: [] })
const query = reactive({ fiscalYear: new Date().getFullYear().toString(), fiscalPeriod: new Date().getMonth() + 1 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/budget/analysis', method: 'get', params: query })
    data.list = res.data || []
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
