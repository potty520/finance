<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">操作日志</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="username" label="操作人" width="120" />
      <el-table-column prop="module" label="模块" width="140" />
      <el-table-column prop="operation" label="操作" width="140" />
      <el-table-column prop="method" label="方法" />
      <el-table-column prop="ip" label="IP" width="140" />
      <el-table-column prop="createTime" label="操作时间" width="180" />
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
    const res = await request({ url: '/system/log/operation/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
