<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">项目台账</h2>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="项目"><el-input v-model="query.projectId" placeholder="项目ID" /></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="projectName" label="项目" width="160" />
      <el-table-column prop="businessType" label="业务类型" width="120" />
      <el-table-column prop="businessNo" label="业务单号" width="180" />
      <el-table-column prop="summary" label="摘要" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="handleDate" label="发生日期" width="120" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { BUSINESS_TYPE_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })
const query = reactive({ projectId: '' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/project/ledger', method: 'get', params: query })
    data.list = res.data
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
