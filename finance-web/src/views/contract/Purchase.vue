<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">采购合同</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增合同</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="contractNo" label="合同号" width="180" />
      <el-table-column prop="contractName" label="合同名称" />
      <el-table-column prop="partyName" label="供应商" width="160" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="signDate" label="签订日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag>{{ { '0': '待审', '1': '生效', '2': '终止', '3': '完成' }[row.status] || row.status }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const data = reactive({ list: [] })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/contract/page', method: 'get', params: { contractType: 'BUY', pageNum: 1, pageSize: 50 } })
    data.list = res.data.records || []
  } finally { loading.value = false }
}

const onAdd = () => {}

onMounted(loadData)
</script>
