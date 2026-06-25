<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">成本中心</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border default-expand-all row-key="id" :tree-props="{ children: 'children' }">
      <el-table-column prop="centerCode" label="编码" width="160" />
      <el-table-column prop="centerName" label="名称" />
      <el-table-column prop="centerType" label="类型" width="120">
        <template #default="{ row }">
          <el-tag>{{ { 'P': '生产', 'S': '服务', 'M': '管理', 'R': '研发' }[row.centerType] || row.centerType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="managerName" label="负责人" width="120" />
      <el-table-column prop="deptName" label="部门" width="160" />
      <el-table-column prop="subjectCode" label="对应科目" width="160" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="编码"><el-input v-model="form.centerCode" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="form.centerName" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.centerType">
            <el-option label="生产" value="P" />
            <el-option label="服务" value="S" />
            <el-option label="管理" value="M" />
            <el-option label="研发" value="R" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人"><el-input v-model="form.managerName" /></el-form-item>
        <el-form-item label="部门"><el-input v-model="form.deptName" /></el-form-item>
        <el-form-item label="对应科目"><el-input v-model="form.subjectCode" /></el-form-item>
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
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, centerCode: '', centerName: '', centerType: 'P', managerName: '', deptName: '', subjectCode: '', status: 1 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/cost/center/list', method: 'get' })
    data.list = res.data
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, centerCode: 'CC-' + Date.now() }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  if (form.id) await request({ url: '/cost/center', method: 'put', data: form })
  else await request({ url: '/cost/center', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/cost/center/${row.id}`, method: 'delete' })
  loadData()
}

onMounted(loadData)
</script>
