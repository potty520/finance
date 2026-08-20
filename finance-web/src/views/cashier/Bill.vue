<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">票据管理</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="票据号" width="180" />
      <el-table-column label="类型" width="100"><template #default="{row}">{{ BILL_TYPE_MAP[row.billType] || row.billType }}</template></el-table-column>
      <el-table-column prop="amount" label="金额" align="right" width="120" />
      <el-table-column prop="issueDate" label="出票日" width="120" />
      <el-table-column prop="dueDate" label="到期日" width="120" />
      <el-table-column label="状态" width="100"><template #default="{row}"><el-tag>{{ BILL_STATUS_MAP[row.status] || row.status }}</el-tag></template></el-table-column>
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
import { BILL_TYPE_MAP, STATUS_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [], total: 0 })
const pager = reactive({ pageNum: 1, pageSize: 10 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/cashier/bill/page', method: 'get', params: { pageNum: pager.pageNum, pageSize: pager.pageSize } })
    data.list = res.data.records
    data.total = res.data.total || 0
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
