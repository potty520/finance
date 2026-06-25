<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">费用归集</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增归集</el-button>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="年度"><el-input v-model="query.fiscalYear" /></el-form-item>
        <el-form-item label="期间"><el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" /></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="单号" width="180" />
      <el-table-column prop="fiscalYear" label="年度" width="80" />
      <el-table-column prop="fiscalPeriod" label="期间" width="80" />
      <el-table-column prop="allocationDate" label="日期" width="120" />
      <el-table-column prop="sourceCenterName" label="源中心" width="160" />
      <el-table-column prop="targetCenterName" label="目标中心" width="160" />
      <el-table-column prop="itemName" label="费用项" width="160" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="remark" label="备注" />
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="600px">
      <el-form :model="form" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="年度"><el-input v-model="form.fiscalYear" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="期间"><el-input-number v-model="form.fiscalPeriod" :min="1" :max="12" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="日期"><el-date-picker v-model="form.allocationDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="金额"><el-input-number v-model="form.amount" :precision="2" :min="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="源中心"><el-input v-model="form.sourceCenterName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="目标中心"><el-input v-model="form.targetCenterName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="费用项"><el-input v-model="form.itemName" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item></el-col>
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

const loading = ref(false)
const data = reactive({ list: [] })
const query = reactive({ fiscalYear: new Date().getFullYear().toString(), fiscalPeriod: new Date().getMonth() + 1 })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, fiscalYear: '2026', fiscalPeriod: 1, allocationDate: '', sourceCenterName: '', targetCenterName: '', itemName: '', amount: 0, remark: '' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/cost/allocation/page', method: 'get', params: { ...query, pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, allocationDate: new Date().toISOString().substr(0, 10) }); dialog.visible = true; dialog.title = '新增' }
const onSave = async () => {
  await request({ url: '/cost/allocation', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}

onMounted(loadData)
</script>
