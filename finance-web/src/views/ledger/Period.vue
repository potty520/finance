<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">{{ pageTitle }}</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="periodCode" label="期间编码" width="120" />
      <el-table-column prop="periodName" label="期间名称" width="140" />
      <el-table-column prop="fiscalYear" label="年度" width="100" />
      <el-table-column prop="periodIndex" label="期间" width="80" />
      <el-table-column prop="startDate" label="开始日期" width="120" />
      <el-table-column prop="endDate" label="结束日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="isClosed(row) ? 'danger' : 'success'">{{ isClosed(row) ? '已结账' : '未结账' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template #default="{ row }">
          <el-button type="warning" link @click.stop="onClose(row)" v-if="!isClosed(row)">结账</el-button>
          <el-button type="success" link @click.stop="onUnclose(row)" v-if="isClosed(row)">反结账</el-button>
          <el-button type="danger" link @click.stop="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" title="新增会计期间" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="年度" prop="fiscalYear"><el-input-number v-model="form.fiscalYear" :min="2000" :max="2100" /></el-form-item>
        <el-form-item label="期间" prop="periodIndex"><el-input-number v-model="form.periodIndex" :min="1" :max="12" /></el-form-item>
        <el-form-item label="开始日期" prop="startDate"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="结束日期" prop="endDate"><el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const route = useRoute()
const pageTitle = computed(() => route.meta.title || '会计期间')

const loading = ref(false)
const data = reactive({ list: [] })
const dialog = reactive({ visible: false })
const formRef = ref()
const form = reactive({ fiscalYear: 2026, periodIndex: 1, startDate: '', endDate: '' })
const rules = {
  fiscalYear: [{ required: true, message: '请输入年度' }],
  periodIndex: [{ required: true, message: '请输入期间' }],
  startDate: [{ required: true, message: '请选择开始日期' }],
  endDate: [{ required: true, message: '请选择结束日期' }]
}

const isClosed = (row) => row.status === 'CLOSED'

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/ledger/period/page', method: 'get', params: { pageNum: 1, pageSize: 100 } })
    data.list = res.data.records
  } finally {
    loading.value = false
  }
}

const onAdd = () => {
  Object.assign(form, { fiscalYear: 2026, periodIndex: 1, startDate: '', endDate: '' })
  dialog.visible = true
}
const onSave = async () => {
  await formRef.value.validate()
  const payload = {
    ...form,
    periodCode: `${form.fiscalYear}${String(form.periodIndex).padStart(2, '0')}`,
    periodName: `${form.fiscalYear}年${form.periodIndex}月`
  }
  await request({ url: '/ledger/period', method: 'post', data: payload })
  ElMessage.success('保存成功')
  dialog.visible = false
  loadData()
}
const onClose = async (row) => {
  await ElMessageBox.confirm('确认结账？', '提示', { type: 'warning' })
  await request({ url: `/ledger/period/close/${row.id}`, method: 'post' })
  ElMessage.success('已结账')
  loadData()
}
const onUnclose = async (row) => {
  await ElMessageBox.confirm('确认反结账？', '提示', { type: 'warning' })
  await request({ url: `/ledger/period/unclose/${row.id}`, method: 'post' })
  ElMessage.success('已反结账')
  loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/ledger/period/${row.id}`, method: 'delete' })
  loadData()
}

onMounted(loadData)
</script>
