<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">借款管理</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增借款</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="单号" width="180" />
      <el-table-column prop="applicantName" label="借款人" width="120" />
      <el-table-column prop="deptName" label="部门" width="120" />
      <el-table-column prop="amount" label="借款金额" align="right" />
      <el-table-column prop="repaidAmount" label="已还款" align="right" />
      <el-table-column prop="pendingAmount" label="未还" align="right" />
      <el-table-column prop="loanDate" label="借款日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === '3' ? 'success' : 'warning'">
            {{ { '0': '待审', '1': '已批准', '2': '已拒绝', '3': '已还清' }[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="success" link @click="onRepay(row)" v-if="row.status === '1'">还款</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" title="新增借款" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="借款人"><el-input v-model="form.applicantName" /></el-form-item>
        <el-form-item label="部门"><el-input v-model="form.deptName" /></el-form-item>
        <el-form-item label="金额"><el-input-number v-model="form.amount" :precision="2" :min="0" /></el-form-item>
        <el-form-item label="借款日期"><el-date-picker v-model="form.loanDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="原因"><el-input v-model="form.reason" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="repayDialog.visible" title="还款" width="400px">
      <el-form :model="repayForm" label-width="100px">
        <el-form-item label="还款金额"><el-input-number v-model="repayForm.amount" :precision="2" :min="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="repayDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="onSaveRepay">确认还款</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const data = reactive({ list: [] })
const dialog = reactive({ visible: false })
const form = reactive({ id: null, applicant: 1, applicantName: '当前用户', deptName: '财务部', amount: 0, loanDate: '', reason: '' })
const repayDialog = reactive({ visible: false })
const repayForm = reactive({ loanId: null, amount: 0 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/expense/loan/list', method: 'get', params: { applicant: 1 } })
    data.list = res.data
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, loanDate: new Date().toISOString().substr(0, 10) }); dialog.visible = true }
const onSave = async () => {
  await request({ url: '/expense/loan', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onRepay = (row) => { repayForm.loanId = row.id; repayForm.amount = row.pendingAmount; repayDialog.visible = true }
const onSaveRepay = async () => {
  await request({ url: '/expense/loan/repay', method: 'post', data: repayForm })
  ElMessage.success('还款成功'); repayDialog.visible = false; loadData()
}

onMounted(loadData)
</script>
