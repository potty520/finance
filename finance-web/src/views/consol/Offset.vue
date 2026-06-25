<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">抵消分录</h2>
      <div>
        <el-button type="primary" :icon="Plus" @click="onAdd">新增抵消</el-button>
      </div>
    </div>
    <el-row :gutter="16" style="margin-bottom:16px;">
      <el-col :span="8">
        <el-input v-model="query.fiscalYear" placeholder="年度" />
      </el-col>
      <el-col :span="8">
        <el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" placeholder="期间" style="width:100%" />
      </el-col>
      <el-col :span="8">
        <el-button type="primary" @click="loadData">查询</el-button>
      </el-col>
    </el-row>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="fiscalYear" label="年度" width="80" />
      <el-table-column prop="fiscalPeriod" label="期间" width="80" />
      <el-table-column prop="offsetType" label="类型" width="120">
        <template #default="{ row }">
          <el-tag>{{ { 'INV': '存货', 'AR': '往来', 'INCOME': '内部交易', 'DIV': '股利', 'FIXED': '固定资产' }[row.offsetType] || row.offsetType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="subjectName" label="科目" />
      <el-table-column prop="summary" label="摘要" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="sourceCompany" label="来源公司" width="160" />
      <el-table-column prop="targetCompany" label="目标公司" width="160" />
    </el-table>

    <el-dialog v-model="dialog.visible" title="新增抵消" width="600px">
      <el-form :model="form" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="年度"><el-input v-model="form.fiscalYear" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="期间"><el-input-number v-model="form.fiscalPeriod" :min="1" :max="12" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="类型">
            <el-select v-model="form.offsetType">
              <el-option label="存货" value="INV" />
              <el-option label="往来" value="AR" />
              <el-option label="内部交易" value="INCOME" />
              <el-option label="股利" value="DIV" />
              <el-option label="固定资产" value="FIXED" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="金额"><el-input-number v-model="form.amount" :precision="2" :min="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="科目编码"><el-input v-model="form.subjectCode" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="科目名称"><el-input v-model="form.subjectName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="来源公司"><el-input v-model="form.sourceCompany" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="目标公司"><el-input v-model="form.targetCompany" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="摘要"><el-input v-model="form.summary" /></el-form-item></el-col>
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
const dialog = reactive({ visible: false })
const form = reactive({ id: null, fiscalYear: '2026', fiscalPeriod: 1, offsetType: 'INV', subjectCode: '', subjectName: '', summary: '', amount: 0, sourceCompany: '', targetCompany: '' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/consol/offset/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null }); dialog.visible = true }
const onSave = async () => {
  await request({ url: '/consol/offset', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}

onMounted(loadData)
</script>
