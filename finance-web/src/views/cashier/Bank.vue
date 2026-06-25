<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">银行对账</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="accountName" label="账户" width="160" />
      <el-table-column prop="periodCode" label="期间" width="100" />
      <el-table-column prop="bookBalance" label="账面余额" align="right" />
      <el-table-column prop="bankBalance" label="银行余额" align="right" />
      <el-table-column prop="diffAmount" label="差异金额" align="right" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === '1' ? 'success' : 'warning'">{{ row.status === '1' ? '已对账' : '未对账' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
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
    const res = await request({ url: '/cashier/reconciliation/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
