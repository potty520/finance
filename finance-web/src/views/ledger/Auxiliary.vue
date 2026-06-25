<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">辅助核算</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="auxCode" label="编码" width="160" />
      <el-table-column prop="auxName" label="名称" />
      <el-table-column prop="auxType" label="类型" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
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

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/ledger/auxiliary/list', method: 'get' })
    data.list = res.data || []
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
