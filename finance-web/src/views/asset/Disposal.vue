<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">资产处置</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="disposalNo" label="处置单号" width="180" />
      <el-table-column prop="assetCode" label="资产编码" width="160" />
      <el-table-column prop="assetName" label="资产名称" />
      <el-table-column prop="disposalDate" label="处置日期" width="120" />
      <el-table-column prop="disposalAmount" label="处置金额" align="right" width="120" />
      <el-table-column prop="disposalType" label="处置方式" width="120" />
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
    const res = await request({ url: '/asset/disposal/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = res.data.records || []
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
