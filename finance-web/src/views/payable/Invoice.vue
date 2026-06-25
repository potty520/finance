<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">采购发票</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="发票号"><el-input v-model="query.invoiceNo" /></el-form-item>
        <el-form-item label="供应商"><el-input v-model="query.supplierName" /></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="invoiceNo" label="发票号" width="180" />
      <el-table-column prop="supplierName" label="供应商" width="160" />
      <el-table-column prop="invoiceDate" label="开票日期" width="120" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="taxAmount" label="税额" align="right" />
      <el-table-column prop="totalAmount" label="价税合计" align="right" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag>{{ { '0': '未核销', '1': '已核销' }[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="发票号"><el-input v-model="form.invoiceNo" /></el-form-item>
        <el-form-item label="供应商"><el-input v-model="form.supplierName" /></el-form-item>
        <el-form-item label="开票日期"><el-date-picker v-model="form.invoiceDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="金额"><el-input-number v-model="form.amount" :precision="2" :min="0" /></el-form-item>
        <el-form-item label="税率"><el-input-number v-model="form.taxRate" :precision="2" :min="0" :max="100" /> %</el-form-item>
        <el-form-item label="税额"><el-input-number v-model="form.taxAmount" :precision="2" :min="0" /></el-form-item>
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
const query = reactive({ invoiceNo: '', supplierName: '' })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, invoiceNo: '', supplierName: '', invoiceDate: '', amount: 0, taxRate: 13, taxAmount: 0, remark: '', status: '0' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/payable/invoice/page', method: 'get', params: { ...query, pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, invoiceNo: 'PINV-' + Date.now(), invoiceDate: new Date().toISOString().substr(0, 10) }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  form.totalAmount = Number(form.amount || 0) + Number(form.taxAmount || 0)
  if (form.id) await request({ url: '/payable/invoice', method: 'put', data: form })
  else await request({ url: '/payable/invoice', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/payable/invoice/${row.id}`, method: 'delete' })
  loadData()
}

onMounted(loadData)
</script>
