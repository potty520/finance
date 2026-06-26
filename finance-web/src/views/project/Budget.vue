<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">项目预算</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="projectId" label="项目ID" width="100" />
      <el-table-column prop="projectName" label="项目名称" />
      <el-table-column prop="budgetType" label="类型" width="120">
        <template #default="{ row }">
          <el-tag>{{ row.budgetType === 'REVENUE' ? '收入' : '成本' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="subjectName" label="科目" width="120" />
      <el-table-column prop="amount" label="预算金额" align="right" />
      <el-table-column prop="remark" label="备注" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { BUDGET_TYPE_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/project/budget/list', method: 'get' })
    data.list = res.data || []
  } finally { loading.value = false }
}

onMounted(loadData)
</script>
