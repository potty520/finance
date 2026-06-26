<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">仓库管理</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="warehouseCode" label="仓库编码" width="160" />
      <el-table-column prop="warehouseName" label="仓库名称" />
      <el-table-column prop="warehouseType" label="类型" width="120" />
      <el-table-column prop="address" label="地址" />
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
import { STATUS_MAP, WAREHOUSE_TYPE_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/stock/warehouse/list', method: 'get' })
    data.list = res.data || []
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
