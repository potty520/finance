<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">菜单管理</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增菜单</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border default-expand-all row-key="id" :tree-props="{ children: 'children' }">
      <el-table-column prop="menuName" label="菜单名称" />
      <el-table-column prop="menuCode" label="权限标识" width="180" />
      <el-table-column prop="menuType" label="类型" width="100">
        <template #default="{ row }">
          <el-tag>{{ { 'D': '目录', 'M': '菜单', 'B': '按钮' }[row.menuType] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="path" label="路径" width="200" />
      <el-table-column prop="icon" label="图标" width="80" />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="菜单名"><el-input v-model="form.menuName" /></el-form-item>
        <el-form-item label="权限标识"><el-input v-model="form.menuCode" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.menuType">
            <el-option label="目录" value="D" />
            <el-option label="菜单" value="M" />
            <el-option label="按钮" value="B" />
          </el-select>
        </el-form-item>
        <el-form-item label="路径"><el-input v-model="form.path" /></el-form-item>
        <el-form-item label="图标"><el-input v-model="form.icon" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
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
const form = reactive({ id: null, parentId: 0, menuName: '', menuCode: '', menuType: 'M', path: '', icon: '', sort: 0 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/system/menu/list', method: 'get' })
    data.list = res.data
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  if (form.id) await request({ url: '/system/menu', method: 'put', data: form })
  else await request({ url: '/system/menu', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/system/menu/${row.id}`, method: 'delete' })
  loadData()
}

onMounted(loadData)
</script>
