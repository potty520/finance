<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">自定义报表</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
    </div>
    <el-table :data="data.list" border>
      <el-table-column prop="reportCode" label="编码" width="180" />
      <el-table-column prop="reportName" label="名称" />
      <el-table-column prop="reportType" label="类型" width="120" />
      <el-table-column prop="fiscalYear" label="年度" width="80" />
      <el-table-column prop="fiscalPeriod" label="期间" width="80" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'A' ? 'success' : 'info'">{{ row.status === 'A' ? '已发布' : '草稿' }}</el-tag>
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
        <el-form-item label="编码"><el-input v-model="form.reportCode" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="form.reportName" /></el-form-item>
        <el-form-item label="类型"><el-input v-model="form.reportType" /></el-form-item>
        <el-form-item label="年度"><el-input v-model="form.fiscalYear" /></el-form-item>
        <el-form-item label="期间"><el-input-number v-model="form.fiscalPeriod" :min="1" :max="12" /></el-form-item>
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

const data = reactive({ list: [] })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, reportCode: '', reportName: '', reportType: 'CUSTOM', fiscalYear: '2026', fiscalPeriod: 1, status: 'A' })

const loadData = async () => {
  const res = await request({ url: '/report/custom/list', method: 'get' })
  data.list = res.data
}
const onAdd = () => { Object.assign(form, { id: null, reportCode: 'RPT-' + Date.now() }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  await request({ url: '/report/custom', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/report/custom/${row.id}`, method: 'delete' })
  loadData()
}

onMounted(loadData)
</script>
