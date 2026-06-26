<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">登录日志</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="ip" label="IP" width="140" />
      <el-table-column prop="browser" label="浏览器" />
      <el-table-column prop="os" label="操作系统" width="140" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '成功' : '失败' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="loginTime" label="登录时间" width="180" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { STATUS_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/system/log/login/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
