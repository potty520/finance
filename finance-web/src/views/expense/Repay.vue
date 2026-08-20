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
import { APPLY_TYPE_MAP, STATUS_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [], total: 0 })
const pager = reactive({ pageNum: 1, pageSize: 10 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/expense/apply/page', method: 'get', params: { pageNum: pager.pageNum, pageSize: pager.pageSize } })
    const records = res.data.records || []
    data.list = records.filter(r => r.applyType === 'REIMBURSE' || r.status === '3')
    data.total = records.length
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
