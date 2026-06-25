<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">资产类别</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="categoryCode" label="类别编码" width="160" />
      <el-table-column prop="categoryName" label="类别名称" />
      <el-table-column prop="deprMethod" label="折旧方法" width="140" />
      <el-table-column prop="useLifeMonth" label="使用年限(月)" width="120" />
      <el-table-column prop="residualRate" label="残值率(%)" width="120" align="right" />
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
    const res = await request({ url: '/asset/category/list', method: 'get' })
    data.list = res.data || []
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
