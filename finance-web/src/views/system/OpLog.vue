<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">操作日志</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="username" label="操作人" width="120" />
      <el-table-column prop="module" label="模块" width="140" />
      <el-table-column prop="operation" label="操作" width="140" />
      <el-table-column prop="method" label="方法" />
      <el-table-column prop="ip" label="IP" width="140" />
      <el-table-column prop="createTime" label="操作时间" width="180" />
    </el-table>

    <el-pagination
      v-model:current-page="pager.pageNum"
      v-model:page-size="pager.pageSize"
      :total="data.total"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50, 100]"
      @current-change="loadData"
      @size-change="loadData"
      style="margin-top:16px; justify-content: flex-end;"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'

const loading = ref(false)
const data = reactive({ list: [], total: 0 })
const pager = reactive({ pageNum: 1, pageSize: 10 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/system/log/operation/page', method: 'get', params: { pageNum: pager.pageNum, pageSize: pager.pageSize } })
    data.list = res.data.records
    data.total = res.data.total || 0
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
