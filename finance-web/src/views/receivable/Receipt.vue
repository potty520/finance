<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">收款单</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="单号" width="180" />
      <el-table-column prop="customerName" label="客户" width="160" />
      <el-table-column prop="receiptDate" label="收款日期" width="120" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column label="状态" width="100"><template #default="{row}"><el-tag :type="STATUS_TAG_TYPE[row.status]">{{ { DRAFT:'草稿',PENDING:'待审核',APPROVED:'已审核',POSTED:'已过账',VOIDED:'已作废',RECEIVED:'已收款' }[row.status] }}</el-tag></template></el-table-column>
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
    const res = await request({ url: '/receivable/receipt/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
