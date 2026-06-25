<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">票据管理</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="票据号" width="180" />
      <el-table-column prop="billType" label="类型" width="100" />
      <el-table-column prop="amount" label="金额" align="right" width="120" />
      <el-table-column prop="issueDate" label="出票日" width="120" />
      <el-table-column prop="dueDate" label="到期日" width="120" />
      <el-table-column prop="status" label="状态" width="100" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'

const loading = ref(false)
const data = reactive({ list: [] })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/cashier/bill/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = res.data.records || []
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
