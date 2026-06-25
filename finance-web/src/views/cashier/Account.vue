<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">资金账户</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="accountCode" label="账户编码" width="160" />
      <el-table-column prop="accountName" label="账户名称" />
      <el-table-column prop="bankName" label="开户行" />
      <el-table-column prop="accountNo" label="账号" width="200" />
      <el-table-column prop="balance" label="余额" align="right" width="140" />
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
    const res = await request({ url: '/cashier/account/list', method: 'get' })
    data.list = res.data || []
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
