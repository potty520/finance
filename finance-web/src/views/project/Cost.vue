<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">项目成本</h2>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="项目">
          <el-select v-model="query.projectId" clearable filterable placeholder="全部项目" style="width:200px">
            <el-option v-for="p in projects" :key="p.id" :label="p.projectName" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="list" border>
      <el-table-column prop="projectName" label="项目" width="160" />
      <el-table-column prop="businessType" label="业务类型" width="100">
        <template #default="{ row }">
          <el-tag size="small">{{ { 'EXPENSE': '费用', 'INCOME': '收入', 'LABOR': '人工', 'MATERIAL': '材料' }[row.businessType] || row.businessType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="businessNo" label="业务单号" width="150" />
      <el-table-column prop="summary" label="摘要" min-width="160" show-overflow-tooltip />
      <el-table-column prop="amount" label="金额" align="right" width="120" />
      <el-table-column prop="handleDate" label="日期" width="120" />
    </el-table>
    <div style="margin-top: 16px; text-align: right;">
      <el-tag type="primary" size="large">成本合计: ¥{{ totalCost.toFixed(2) }}</el-tag>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'

const list = ref([])
const projects = ref([])
const query = reactive({ projectId: null })
const totalCost = ref(0)

const loadData = async () => {
  const params = {}
  if (query.projectId) params.projectId = query.projectId
  const res = await request({ url: '/project/ledger', method: 'get', params })
  list.value = res.data || []
  totalCost.value = list.value.reduce((sum, i) => sum + (parseFloat(i.amount) || 0), 0)
}

const loadProjects = async () => {
  const res = await request({ url: '/project/page', method: 'get', params: { page: 1, size: 100 } })
  projects.value = res.data?.records || []
}

onMounted(() => {
  loadProjects()
  loadData()
})
</script>
