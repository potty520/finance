<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">应付账龄分析</h2>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="供应商">
          <el-select v-model="supplierId" filterable placeholder="选择供应商" style="width:220px" @change="loadData">
            <el-option v-for="s in suppliers" :key="s.id" :label="s.supplierName" :value="s.id" />
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
const supplierId = ref(null)
const suppliers = ref([])
const data = reactive({ list: [] })

const loadSuppliers = async () => {
  const res = await request({ url: '/payable/supplier/list', method: 'get' })
  suppliers.value = res.data || []
  if (suppliers.value.length && !supplierId.value) supplierId.value = suppliers.value[0].id
}

const loadData = async () => {
  if (!supplierId.value) return
  loading.value = true
  try {
    const res = await request({ url: `/payable/aging/${supplierId.value}`, method: 'get' })
    data.list = res.data || []
  } finally { loading.value = false }
}

onMounted(async () => {
  await loadSuppliers()
  await loadData()
})
</script>
