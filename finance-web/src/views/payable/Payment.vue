<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">付款单</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增付款</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="付款单号" width="180" />
      <el-table-column prop="supplierName" label="供应商" width="160" />
      <el-table-column prop="paymentDate" label="付款日期" width="120" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="paymentType" label="方式" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag>{{ row.status || '-' }}</el-tag>
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
    const res = await request({ url: '/payable/payment/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = res.data.records || []
  } finally { loading.value = false }
}

const onAdd = () => {}

onMounted(loadData)
</script>
