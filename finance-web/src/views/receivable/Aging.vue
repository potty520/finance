<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">应收账龄分析</h2>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="客户">
          <el-select v-model="customerId" filterable placeholder="选择客户" style="width:220px" @change="loadData">
            <el-option v-for="c in customers" :key="c.id" :label="c.customerName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="bucket" label="账龄区间" width="140" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="ratio" label="占比" width="120" align="right" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'

const loading = ref(false)
const customerId = ref(null)
const customers = ref([])
const data = reactive({ list: [] })

const loadCustomers = async () => {
  const res = await request({ url: '/receivable/customer/list', method: 'get' })
  customers.value = res.data || []
  if (customers.value.length && !customerId.value) customerId.value = customers.value[0].id
}

const loadData = async () => {
  if (!customerId.value) return
  loading.value = true
  try {
    const res = await request({ url: `/receivable/aging/${customerId.value}`, method: 'get' })
    data.list = res.data || []
  } finally { loading.value = false }
}

onMounted(async () => {
  await loadCustomers()
  await loadData()
})
</script>
