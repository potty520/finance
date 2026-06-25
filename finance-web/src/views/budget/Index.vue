<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">预算编制</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增预算</el-button>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="年度"><el-input v-model="query.fiscalYear" /></el-form-item>
        <el-form-item label="期间"><el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable>
            <el-option label="未提交" value="0" />
            <el-option label="已审核" value="1" />
            <el-option label="已下达" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="budgetNo" label="预算单号" width="180" />
      <el-table-column prop="fiscalYear" label="年度" width="80" />
      <el-table-column prop="fiscalPeriod" label="期间" width="80" />
      <el-table-column prop="budgetType" label="类型" width="120" />
      <el-table-column prop="subjectName" label="科目" />
      <el-table-column prop="deptName" label="部门" width="120" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="usedAmount" label="已用" align="right" />
      <el-table-column prop="availableAmount" label="可用" align="right" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="{ '0': 'info', '1': 'warning', '2': 'success' }[row.status]">
            {{ { '0': '未提交', '1': '已审核', '2': '已下达' }[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template #default="{ row }">
          <el-button type="primary" link @click="onAudit(row)" v-if="row.status === '0'">审核</el-button>
          <el-button type="success" link @click="onIssue(row)" v-if="row.status === '1'">下达</el-button>
          <el-button type="warning" link @click="onEdit(row)" v-if="row.status !== '2'">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="600px">
      <el-form :model="form" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="年度"><el-input v-model="form.fiscalYear" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="期间"><el-input-number v-model="form.fiscalPeriod" :min="1" :max="12" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="类型">
            <el-select v-model="form.budgetType">
              <el-option label="收入预算" value="IN" />
              <el-option label="成本预算" value="CO" />
              <el-option label="费用预算" value="EX" />
              <el-option label="资本预算" value="CA" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="科目编码"><el-input v-model="form.subjectCode" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="科目名称"><el-input v-model="form.subjectName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="部门"><el-input v-model="form.deptName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="金额"><el-input-number v-model="form.amount" :precision="2" :min="0" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const data = reactive({ list: [] })
const query = reactive({ fiscalYear: new Date().getFullYear().toString(), fiscalPeriod: '', status: '' })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, fiscalYear: '2026', fiscalPeriod: 1, budgetType: 'EX', subjectCode: '', subjectName: '', deptName: '', amount: 0, remark: '', status: '0' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/budget/page', method: 'get', params: { ...query, pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  if (form.id) await request({ url: '/budget', method: 'put', data: form })
  else await request({ url: '/budget', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onAudit = async (row) => {
  await ElMessageBox.confirm('确认审核通过？', '提示', { type: 'warning' })
  await request({ url: '/budget/audit', method: 'post', data: { id: row.id, pass: true } })
  ElMessage.success('已审核'); loadData()
}
const onIssue = async (row) => {
  await ElMessageBox.confirm('确认下达？下达后将开始控制预算', '提示', { type: 'warning' })
  await request({ url: `/budget/issue/${row.id}`, method: 'post' })
  ElMessage.success('已下达'); loadData()
}

onMounted(loadData)
</script>
