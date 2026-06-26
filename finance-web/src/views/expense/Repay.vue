<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">我的报销</h2>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="单号" width="180" />
      <el-table-column prop="applyType" label="类型" width="120" />
      <el-table-column prop="subjectName" label="科目" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="applyDate" label="报销日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag>{{ { '0': '待审', '1': '通过', '2': '驳回', '3': '已付款' }[row.status] || row.status }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { APPLY_TYPE_MAP, STATUS_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/expense/apply/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = (res.data.records || []).filter(r => r.applyType === 'REIMBURSE' || r.status === '3')
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
