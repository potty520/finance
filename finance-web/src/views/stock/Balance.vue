<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">库存余额</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="goodsCode" label="物料编码" width="160" />
      <el-table-column prop="goodsName" label="物料名称" />
      <el-table-column prop="categoryName" label="类别" width="120" />
      <el-table-column prop="beginQuantity" label="期初数量" align="right" />
      <el-table-column prop="inQuantity" label="入库数量" align="right" />
      <el-table-column prop="outQuantity" label="出库数量" align="right" />
      <el-table-column prop="endQuantity" label="结存数量" align="right" />
      <el-table-column prop="endAmount" label="结存金额" align="right" />
      <el-table-column prop="avgPrice" label="加权均价" align="right" />
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
    const res = await request({ url: '/stock/summary', method: 'get' })
    data.list = res.data
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
