<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">项目列表</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增项目</el-button>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable>
            <el-option label="筹备" value="0" />
            <el-option label="进行中" value="1" />
            <el-option label="暂停" value="2" />
            <el-option label="完成" value="3" />
            <el-option label="关闭" value="4" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="projectCode" label="项目编码" width="180" />
      <el-table-column prop="projectName" label="项目名称" />
      <el-table-column prop="managerName" label="负责人" width="120" />
      <el-table-column prop="budgetAmount" label="预算" align="right" />
      <el-table-column prop="usedAmount" label="已用" align="right" />
      <el-table-column label="使用率" width="160">
        <template #default="{ row }">
          <el-progress :percentage="row.budgetAmount > 0 ? Math.round(row.usedAmount * 100 / row.budgetAmount) : 0" />
        </template>
      </el-table-column>
      <el-table-column prop="startDate" label="开始" width="120" />
      <el-table-column prop="endDate" label="结束" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="{ '0': 'info', '1': 'success', '2': 'warning', '3': 'primary', '4': 'danger' }[row.status]">
            {{ { '0': '筹备', '1': '进行中', '2': '暂停', '3': '完成', '4': '关闭' }[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="600px">
      <el-form :model="form" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="项目编码"><el-input v-model="form.projectCode" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="项目名称"><el-input v-model="form.projectName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="负责人"><el-input v-model="form.managerName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="部门"><el-input v-model="form.deptName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="预算"><el-input-number v-model="form.budgetAmount" :precision="2" :min="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="开始"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="结束"><el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
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
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { STATUS_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })
const query = reactive({ status: '' })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, projectCode: '', projectName: '', managerName: '', deptName: '', budgetAmount: 0, usedAmount: 0, startDate: '', endDate: '', status: '0' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/project/page', method: 'get', params: { ...query, pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, projectCode: 'PJ-' + Date.now() }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  await request({ url: '/project', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}

onMounted(loadData)
</script>
