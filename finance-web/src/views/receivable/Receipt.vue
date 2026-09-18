<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">收款单</h2>
      <div class="header-actions">
        <el-input v-model="search.billNo" placeholder="收款单号" clearable style="width:180px" @keyup.enter="onSearch" @clear="onSearch" />
        <el-select v-model="search.customerId" placeholder="客户" clearable filterable style="width:180px" @change="onSearch">
          <el-option v-for="c in customers" :key="c.id" :label="c.customerName" :value="c.id" />
        </el-select>
        <el-button type="primary" :icon="Plus" @click="onAdd">新增收款</el-button>
      </div>
    </div>

    <el-table :data="data.list" v-loading="loading" border stripe>
      <el-table-column prop="billNo" label="收款单号" width="170" />
      <el-table-column prop="customerName" label="客户" width="150" show-overflow-tooltip />
      <el-table-column prop="receiptDate" label="收款日期" width="110" />
      <el-table-column label="方式" width="90">
        <template #default="{ row }">{{ RECEIPT_TYPE_MAP[row.receiptType] || row.receiptType || '-' }}</template>
      </el-table-column>
      <el-table-column prop="accountName" label="收款账户" width="140">
        <template #default="{ row }">{{ row.accountName || '-' }}</template>
      </el-table-column>
      <el-table-column prop="amount" label="金额" align="right" width="130">
        <template #default="{ row }">{{ fmt(row.amount) }}</template>
      </el-table-column>
      <el-table-column prop="appliedAmount" label="已核销" align="right" width="120">
        <template #default="{ row }">{{ fmt(row.appliedAmount) }}</template>
      </el-table-column>
      <el-table-column prop="unappliedAmount" label="未核销" align="right" width="120">
        <template #default="{ row }"><span class="warn-text">{{ fmt(row.unappliedAmount) }}</span></template>
      </el-table-column>
      <el-table-column label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="statusTag(row.status)" size="small">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="voucherNo" label="凭证号" width="150">
        <template #default="{ row }">{{ row.voucherNo || '-' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="240" align="center" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="onEdit(row)">编辑</el-button>
          <el-button v-if="row.status === 'D'" link type="warning" size="small" @click="onAudit(row)">审核</el-button>
          <el-button v-if="row.status === 'A' && !row.voucherNo" link type="success" size="small" @click="onVoucher(row)">生成凭证</el-button>
          <el-button link type="danger" size="small" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      v-model:current-page="search.pageNum"
      v-model:page-size="search.pageSize"
      :total="data.total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @current-change="loadData"
      @size-change="loadData"
    />

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="620px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="收款单号" prop="billNo"><el-input v-model="form.billNo" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款日期" prop="receiptDate">
              <el-date-picker v-model="form.receiptDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户" prop="customerId">
              <el-select v-model="form.customerId" filterable style="width:100%" @change="onCustomerChange">
                <el-option v-for="c in customers" :key="c.id" :label="c.customerName" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款方式">
              <el-select v-model="form.receiptType" style="width:100%">
                <el-option v-for="(label, key) in RECEIPT_TYPE_MAP" :key="key" :label="label" :value="key" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款账户" prop="accountId">
              <el-select v-model="form.accountId" filterable style="width:100%" @change="onAccountChange">
                <el-option v-for="a in accounts" :key="a.id" :label="a.accountName + '（' + a.accountCode + '）'" :value="a.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款金额" prop="amount">
              <el-input-number v-model="form.amount" :min="0.01" :precision="2" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="2" /></el-form-item>
          </el-col>
        </el-row>
        <el-alert type="info" :closable="false" show-icon
          title="审核后可生成凭证：借 现金/银行存款，贷 应收账款。生成凭证后可在「应收核销」中与发票核销。" />
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  receiptPage, saveReceipt, updateReceipt, deleteReceipt,
  auditReceipt, receiptVoucher, customerList
} from '@/api/receivable'
import request from '@/utils/request'

const RECEIPT_TYPE_MAP = { '1': '现金', '2': '银行', '3': '票据', '4': '其他' }

const loading = ref(false)
const saving = ref(false)
const formRef = ref()
const customers = ref([])
const accounts = ref([])
const data = reactive({ list: [], total: 0 })
const search = reactive({ pageNum: 1, pageSize: 10, billNo: '', customerId: null })
const dialog = reactive({ visible: false, title: '新增收款单' })

const emptyForm = () => ({
  id: null, billNo: 'SR' + Date.now(), receiptDate: today(), customerId: null, customerName: '',
  receiptType: '2', accountId: null, accountName: '', amount: 0, remark: '', status: 'D'
})
const form = reactive(emptyForm())

const rules = {
  billNo: [{ required: true, message: '请输入收款单号', trigger: 'blur' }],
  receiptDate: [{ required: true, message: '请选择收款日期', trigger: 'change' }],
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  accountId: [{ required: true, message: '请选择收款账户', trigger: 'change' }],
  amount: [{ required: true, message: '请输入收款金额', trigger: 'blur' }]
}

function today() { return new Date().toISOString().slice(0, 10) }
const fmt = (v) => Number(v || 0).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
const statusText = (s) => ({ D: '草稿', A: '已审核' })[s] || s || '草稿'
const statusTag = (s) => ({ D: 'info', A: 'success' })[s] || 'info'

const onCustomerChange = (id) => {
  const c = customers.value.find((x) => x.id === id)
  form.customerName = c ? c.customerName : ''
}
const onAccountChange = (id) => {
  const a = accounts.value.find((x) => x.id === id)
  form.accountName = a ? a.accountName : ''
  if (a && a.subjectCode) form.subjectCode = a.subjectCode
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await receiptPage({
      pageNum: search.pageNum, pageSize: search.pageSize,
      billNo: search.billNo || undefined,
      customerId: search.customerId || undefined
    })
    data.list = res.data.records || []
    data.total = res.data.total || 0
  } finally { loading.value = false }
}

const onSearch = () => { search.pageNum = 1; loadData() }
const onAdd = () => { Object.assign(form, emptyForm()); dialog.title = '新增收款单'; dialog.visible = true }

const onEdit = (row) => {
  if (row.status !== 'D') { ElMessage.warning('已审核的收款单不可编辑'); return }
  Object.assign(form, emptyForm(), {
    id: row.id, billNo: row.billNo, receiptDate: row.receiptDate,
    customerId: row.customerId, customerName: row.customerName,
    receiptType: row.receiptType || '2', accountId: row.accountId,
    accountName: row.accountName || '', amount: Number(row.amount || 0),
    subjectCode: row.subjectCode || '', remark: row.remark || '', status: row.status
  })
  dialog.title = '编辑收款单'
  dialog.visible = true
}

const onSave = async () => {
  await formRef.value.validate()
  onAccountChange(form.accountId)
  saving.value = true
  try {
    if (form.id) await updateReceipt(form)
    else await saveReceipt(form)
    ElMessage.success('保存成功')
    dialog.visible = false
    loadData()
  } finally { saving.value = false }
}

const onAudit = async (row) => {
  await ElMessageBox.confirm('确定审核收款单「' + row.billNo + '」吗？', '提示', { type: 'warning' })
  await auditReceipt(row.id)
  ElMessage.success('审核成功')
  loadData()
}

const onVoucher = async (row) => {
  await ElMessageBox.confirm(
    '将为收款单「' + row.billNo + '」生成记账凭证（借：现金/银行存款，贷：应收账款），确定继续？',
    '生成凭证', { type: 'warning' }
  )
  const res = await receiptVoucher(row.id)
  ElMessage.success('凭证已生成，ID: ' + res.data)
  loadData()
}

const onDelete = async (row) => {
  await ElMessageBox.confirm('确定删除收款单「' + row.billNo + '」吗？', '提示', { type: 'warning' })
  await deleteReceipt(row.id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(async () => {
  const [cs, ac] = await Promise.all([
    customerList(),
    request({ url: '/cashier/account/list', method: 'get' })
  ])
  customers.value = cs.data || []
  accounts.value = ac.data || []
  loadData()
})
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.header-actions { display: flex; gap: 10px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
.warn-text { color: #e6a23c; }
</style>