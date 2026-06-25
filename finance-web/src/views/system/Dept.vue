<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">部门管理</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border row-key="id" default-expand-all>
      <el-table-column prop="deptCode" label="部门编码" width="140" />
      <el-table-column prop="deptName" label="部门名称" />
      <el-table-column prop="leader" label="负责人" width="120" />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="部门编码"><el-input v-model="form.deptCode" /></el-form-item>
        <el-form-item label="部门名称"><el-input v-model="form.deptName" /></el-form-item>
        <el-form-item label="负责人"><el-input v-model="form.leader" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
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
const form = reactive({ id: null, deptCode: '', deptName: '', leader: '', sortOrder: 0, parentId: 0 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/system/dept/tree', method: 'get' })
    data.list = res.data
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, parentId: 0 }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  if (form.id) await request({ url: '/system/dept', method: 'put', data: form })
  else await request({ url: '/system/dept', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/system/dept/${row.id}`, method: 'delete' })
  loadData()
}

onMounted(loadData)
</script>
