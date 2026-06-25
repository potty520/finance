<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">供应商档案</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="supplierCode" label="供应商编码" width="140" />
      <el-table-column prop="supplierName" label="供应商名称" />
      <el-table-column prop="contact" label="联系人" width="120" />
      <el-table-column prop="phone" label="电话" width="140" />
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
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const data = reactive({ list: [] })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/payable/supplier/list', method: 'get' })
    data.list = res.data
  } finally { loading.value = false }
}
const onAdd = () => {}

onMounted(loadData)
</script>
