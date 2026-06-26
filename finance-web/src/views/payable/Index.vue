<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">应付账款</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="供应商"><el-input v-model="query.supplierName" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable>
            <el-option label="未清" value="0" />
            <el-option label="部分清" value="1" />
            <el-option label="已清" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="单号" width="180" />
      <el-table-column prop="supplierName" label="供应商" width="160" />
      <el-table-column prop="transDate" label="日期" width="120" />
      <el-table-column prop="amount" label="应付金额" align="right" />
      <el-table-column prop="paidAmount" label="已付" align="right" />
      <el-table-column prop="pendingAmount" label="未付" align="right" />
      <el-table-column prop="dueDate" label="到期日" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === '2' ? 'success' : row.status === '1' ? 'warning' : 'danger'">
            {{ { '0': '未清', '1': '部分清', '2': '已清' }[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <el-button type="primary" link @click="onPay(row)" v-if="row.status !== '2'">付款</el-button>
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="单号"><el-input v-model="form.billNo" /></el-form-item>
        <el-form-item label="供应商"><el-input v-model="form.supplierName" /></el-form-item>
        <el-form-item label="日期"><el-date-picker v-model="form.transDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="应付金额"><el-input-number v-model="form.amount" :precision="2" :min="0" /></el-form-item>
        <el-form-item label="到期日"><el-date-picker v-model="form.dueDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="摘要"><el-input v-model="form.summary" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="payDialog.visible" title="付款" width="400px">
      <el-form :model="payForm" label-width="100px">
        <el-form-item label="付款金额"><el-input-number v-model="payForm.amount" :precision="2" :min="0" /></el-form-item>
        <el-form-item label="付款日期"><el-date-picker v-model="payForm.paidDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="付款账户">
          <el-select v-model="payForm.accountId">
            <el-option v-for="a in accounts" :key="a.id" :label="a.accountName" :value="a.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="payDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="onSavePay">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { STATUS_MAP } from '@/constants/enums'

const loading = ref(false)
const accounts = ref([])
const data = reactive({ list: [] })
const query = reactive({ supplierName: '', status: '' })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, billNo: '', supplierName: '', transDate: '', amount: 0, dueDate: '', summary: '' })
const payDialog = reactive({ visible: false })
const payForm = reactive({ id: null, amount: 0, paidDate: '', accountId: null })

const loadAccounts = async () => {
  const res = await request({ url: '/cashier/account/list', method: 'get' })
  accounts.value = res.data
}
const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/payable/invoice/page', method: 'get', params: { ...query, pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, billNo: 'AP-' + Date.now() }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  if (form.id) await request({ url: '/payable/invoice', method: 'put', data: form })
  else await request({ url: '/payable/invoice', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/payable/invoice/${row.id}`, method: 'delete' })
  loadData()
}
const onPay = (row) => {
  Object.assign(payForm, { id: row.id, amount: row.pendingAmount, paidDate: new Date().toISOString().substr(0, 10), accountId: null })
  payDialog.visible = true
}
const onSavePay = async () => {
  await request({ url: '/payable/payment', method: 'post', data: payForm })
  ElMessage.success('付款成功'); payDialog.visible = false; loadData()
}

onMounted(() => { loadAccounts(); loadData() })
</script>
