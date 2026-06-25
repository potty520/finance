<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">系统参数</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="configKey" label="参数键" width="200" />
      <el-table-column prop="configName" label="参数名称" width="200" />
      <el-table-column prop="configValue" label="参数值" />
      <el-table-column prop="remark" label="备注" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" title="编辑参数" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="参数键"><el-input v-model="form.configKey" disabled /></el-form-item>
        <el-form-item label="参数名"><el-input v-model="form.configName" disabled /></el-form-item>
        <el-form-item label="参数值"><el-input v-model="form.configValue" /></el-form-item>
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
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const data = reactive({ list: [] })
const dialog = reactive({ visible: false })
const form = reactive({ id: null, configKey: '', configName: '', configValue: '', remark: '' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/system/config/list', method: 'get' })
    data.list = res.data
  } finally { loading.value = false }
}
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true }
const onSave = async () => {
  await request({ url: '/system/config', method: 'put', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}

onMounted(loadData)
</script>
