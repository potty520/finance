<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">现金流量项目</h2>
    <el-table :data="data.list" v-loading="loading" border row-key="id" default-expand-all>
      <el-table-column prop="itemCode" label="项目编码" width="140" />
      <el-table-column prop="itemName" label="项目名称" />
      <el-table-column prop="itemType" label="类型" width="120" />
      <el-table-column prop="direction" label="方向" width="100" />
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
    const res = await request({ url: '/ledger/cashFlow/list', method: 'get' })
    data.list = res.data || []
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
