<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">科目管理</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增科目</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border default-expand-all row-key="id" :tree-props="{ children: 'children' }">
      <el-table-column prop="subjectCode" label="科目编码" width="160" />
      <el-table-column prop="subjectName" label="科目名称" />
      <el-table-column prop="subjectType" label="科目类别" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="tagType(row.category)" size="small">
            {{ subjectTypeMap[row.category] || row.category }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="balanceDirection" label="余额方向" width="80" align="center">
      </el-table-column>
      <el-table-column prop="isLeaf" label="末级" width="80">
        <template #default="{ row }">
          <el-tag :type="row.isLeaf === 1 ? 'success' : 'info'" size="small">{{ row.isLeaf === 1 ? '是' : '否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <el-button type="success" plain size="small" @click="onAdd(row)">新增下级</el-button>
          <el-button type="warning" plain size="small" @click="onEdit(row)">编辑</el-button>
          <el-button type="danger" plain size="small" @click="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="父级科目"><el-input v-model="form.parentName" disabled /></el-form-item>
        <el-form-item label="科目编码" prop="subjectCode"><el-input v-model="form.subjectCode" /></el-form-item>
        <el-form-item label="科目名称" prop="subjectName"><el-input v-model="form.subjectName" /></el-form-item>
        <el-form-item label="科目类别" prop="subjectType">
          <el-select v-model="form.category" style="width:200px" placeholder="请选择科目类别">
            <el-option label="资产" value="ASSET" />
            <el-option label="负债" value="LIAB" />
            <el-option label="权益" value="EQUITY" />
            <el-option label="共同" value="COMMON" />
            <el-option label="成本" value="COST" />
            <el-option label="损益" value="EXPENSE" />
            <el-option label="收入" value="INCOME" />
          </el-select>
        </el-form-item>
        <el-form-item label="余额方向">
          <el-radio-group v-model="form.balanceDirection">
            <el-radio value="借方">借</el-radio>
            <el-radio value="贷方">贷</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="助记码"><el-input v-model="form.mnemonic" /></el-form-item>
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
import { STATUS_MAP, SUBJECT_TYPE_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })
const dialog = reactive({ visible: false, title: '' })
const formRef = ref()
const form = reactive({ id: null, parentId: 0, parentName: '无', subjectCode: '', subjectName: '', category: 'ASSET', balanceDirection: '借方', mnemonic: '', isLeaf: 1, status: 1 })
const rules = {
  subjectCode: [{ required: true, message: '请输入编码' }],
  subjectName: [{ required: true, message: '请输入名称' }],
  category: [{ required: true, message: '请选择' }]
}

const subjectTypeMap = { ASSET: '资产', LIAB: '负债', EQUITY: '权益', COMMON: '共同', COST: '成本', EXPENSE: '费用', INCOME: '收入' }
const tagType = (t) => { const map = { ASSET: '', LIAB: 'warning', EQUITY: 'success', COMMON: 'info', COST: 'danger', EXPENSE: 'info', INCOME: '' }; return map[t] || 'info' }

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/ledger/subject/list', method: 'get' })
    data.list = res.data
  } finally {
    loading.value = false
  }
}

const onAdd = (parent) => {
  Object.assign(form, { id: null, parentId: parent ? parent.id : 0, parentName: parent ? parent.subjectName : '无', subjectCode: '', subjectName: '', mnemonic: '', isLeaf: 1, status: 1, category: parent ? parent.category : 'ASSET', balanceDirection: parent ? parent.balanceDirection : '借方' })
  dialog.title = '新增科目'
  dialog.visible = true
}

const onEdit = (row) => {
  Object.assign(form, row)
  dialog.title = '编辑科目'
  dialog.visible = true
}

const onSave = async () => {
  await formRef.value.validate()
  if (form.id) await request({ url: '/ledger/subject', method: 'put', data: form })
  else await request({ url: '/ledger/subject', method: 'post', data: form })
  ElMessage.success('保存成功')
  dialog.visible = false
  loadData()
}

const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/ledger/subject/${row.id}`, method: 'delete' })
  ElMessage.success('已删除')
  loadData()
}

onMounted(loadData)
</script>
