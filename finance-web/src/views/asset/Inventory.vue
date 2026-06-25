<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">资产盘点</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="inventoryNo" label="盘点单号" width="180" />
      <el-table-column prop="inventoryDate" label="盘点日期" width="120" />
      <el-table-column prop="assetCode" label="资产编码" width="160" />
      <el-table-column prop="assetName" label="资产名称" />
      <el-table-column prop="bookQty" label="账面数量" width="100" align="right" />
      <el-table-column prop="actualQty" label="实盘数量" width="100" align="right" />
      <el-table-column prop="diffQty" label="差异" width="100" align="right" />
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
    const res = await request({ url: '/asset/inventory/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = res.data.records || []
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
