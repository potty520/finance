<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">收款单</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="单号" width="180" />
      <el-table-column prop="customerName" label="客户" width="160" />
      <el-table-column prop="receiptDate" label="收款日期" width="120" />
      <el-table-column prop="amount" label="金额" align="right" />
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
    const res = await request({ url: '/receivable/receipt/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
