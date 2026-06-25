<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">公司主体</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="companyCode" label="编码" width="160" />
      <el-table-column prop="companyName" label="公司名称" />
      <el-table-column prop="shortName" label="简称" width="160" />
      <el-table-column prop="taxNo" label="税号" width="200" />
      <el-table-column prop="legalPerson" label="法人" width="120" />
      <el-table-column prop="address" label="地址" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="编码"><el-input v-model="form.companyCode" /></el-form-item>
        <el-form-item label="公司名"><el-input v-model="form.companyName" /></el-form-item>
        <el-form-item label="简称"><el-input v-model="form.shortName" /></el-form-item>
        <el-form-item label="税号"><el-input v-model="form.taxNo" /></el-form-item>
        <el-form-item label="法人"><el-input v-model="form.legalPerson" /></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
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
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const data = reactive({ list: [] })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, companyCode: '', companyName: '', shortName: '', taxNo: '', legalPerson: '', address: '', status: 1 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/system/company/list', method: 'get' })
    data.list = res.data
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, companyCode: 'C-' + Date.now() }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  await request({ url: '/system/company', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}

onMounted(loadData)
</script>
