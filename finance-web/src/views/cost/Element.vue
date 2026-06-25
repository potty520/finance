<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">成本要素</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="elementCode" label="编码" width="160" />
      <el-table-column prop="elementName" label="名称" />
      <el-table-column prop="elementType" label="类型" width="140" />
      <el-table-column prop="subjectCode" label="对应科目" width="140" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
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
    const res = await request({ url: '/cost/item/list', method: 'get' })
    data.list = res.data || []
  } finally { loading.value = false }
}

const onAdd = () => {}

onMounted(loadData)
</script>
