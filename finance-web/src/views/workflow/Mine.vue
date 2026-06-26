<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">我的待办</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="flowCode" label="流程号" width="180" />
      <el-table-column prop="businessType" label="业务类型" width="120">
        <template #default="{ row }">
          <el-tag>{{ { 'voucher': '凭证', 'contract': '合同', 'expense': '费用', 'budget': '预算' }[row.businessType] || row.businessType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="businessNo" label="业务单号" width="180" />
      <el-table-column prop="nodeName" label="节点" width="120" />
      <el-table-column prop="level" label="层级" width="80" />
      <el-table-column prop="createTime" label="发起时间" width="180" />
      <el-table-column label="操作" width="280">
        <template #default="{ row }">
          <el-button type="success" link @click="onApprove(row)">通过</el-button>
          <el-button type="danger" link @click="onReject(row)">驳回</el-button>
          <el-button type="warning" link @click="onTransfer(row)">转交</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { BUSINESS_TYPE_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })

const loadData = async () => {
  loading.value = true
  try {
    const userStore = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const res = await request({ url: '/workflow/myTasks', method: 'get', params: { userId: userStore.id || 2 } })
    data.list = res.data
  } finally { loading.value = false }
}
const onApprove = async (row) => {
  await ElMessageBox.confirm('确认通过？', '提示', { type: 'warning' })
  await request({ url: '/workflow/approve', method: 'post', data: { taskId: row.id, userId: 2, userName: '审批员', opinion: '同意' } })
  ElMessage.success('已通过'); loadData()
}
const onReject = async (row) => {
  const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回', { inputType: 'textarea' })
  await request({ url: '/workflow/reject', method: 'post', data: { taskId: row.id, userId: 2, userName: '审批员', opinion: value } })
  ElMessage.success('已驳回'); loadData()
}
const onTransfer = async (row) => {
  const { value } = await ElMessageBox.prompt('请输入转交人姓名', '转交')
  await request({ url: '/workflow/transfer', method: 'post', data: { taskId: row.id, fromUserId: 2, fromUserName: '审批员', toUserId: 3, toUserName: value, opinion: '转交' } })
  ElMessage.success('已转交'); loadData()
}

onMounted(loadData)
</script>
