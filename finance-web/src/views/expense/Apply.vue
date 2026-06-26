<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">我的申请</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增申请</el-button>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable style="width:100px">
            <el-option label="待审" value="0" />
            <el-option label="通过" value="1" />
            <el-option label="驳回" value="2" />
            <el-option label="已付款" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="单号" width="180" />
      <el-table-column prop="applyType" label="类型" width="120" />
      <el-table-column prop="subjectName" label="科目" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="applyDate" label="申请日期" width="120" />
      <el-table-column prop="applicantName" label="申请人" width="120" />
      <el-table-column prop="deptName" label="部门" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="{ '0': 'warning', '1': 'success', '2': 'danger', '3': 'info' }[row.status]">
            {{ { '0': '待审', '1': '通过', '2': '驳回', '3': '已付款' }[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" title="新增费用申请" width="600px">
      <el-form :model="form" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="类型">
            <el-select v-model="form.applyType">
              <el-option label="差旅费" value="TRAVEL" />
              <el-option label="招待费" value="ENTERTAIN" />
              <el-option label="办公费" value="OFFICE" />
              <el-option label="通讯费" value="COMM" />
              <el-option label="其他" value="OTHER" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="日期"><el-date-picker v-model="form.applyDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="科目"><el-input v-model="form.subjectName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="金额"><el-input-number v-model="form.amount" :precision="2" :min="0" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="事由"><el-input v-model="form.reason" type="textarea" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="申请人"><el-input v-model="form.applicantName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="部门"><el-input v-model="form.deptName" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="onSave">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { APPLY_TYPE_MAP, STATUS_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })
const query = reactive({ status: '' })
const dialog = reactive({ visible: false })
const form = reactive({ id: null, applyType: 'TRAVEL', applyDate: '', subjectCode: '', subjectName: '', amount: 0, reason: '', applicant: 1, applicantName: '当前用户', deptName: '财务部' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/expense/apply/page', method: 'get', params: { ...query, pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, applyDate: new Date().toISOString().substr(0, 10) }); dialog.visible = true }
const onSave = async () => {
  await request({ url: '/expense/apply', method: 'post', data: form })
  ElMessage.success('已提交'); dialog.visible = false; loadData()
}

onMounted(loadData)
</script>
