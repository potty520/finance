<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">数据字典</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="dictType" label="字典类型" width="200" />
      <el-table-column prop="dictLabel" label="标签" width="160" />
      <el-table-column prop="dictValue" label="值" />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column prop="remark" label="备注" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="字典类型"><el-input v-model="form.dictType" /></el-form-item>
        <el-form-item label="标签"><el-input v-model="form.dictLabel" /></el-form-item>
        <el-form-item label="值"><el-input v-model="form.dictValue" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item>
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
const form = reactive({ id: null, dictType: '', dictLabel: '', dictValue: '', sortOrder: 0, remark: '' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/system/dict/data/list', method: 'get' })
    data.list = res.data
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  if (form.id) await request({ url: '/system/dict/data', method: 'put', data: form })
  else await request({ url: '/system/dict/data', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/system/dict/data/${row.id}`, method: 'delete' })
  loadData()
}

onMounted(loadData)
</script>
