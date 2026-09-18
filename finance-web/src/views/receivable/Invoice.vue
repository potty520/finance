<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">销售发票</h2>
      <div class="header-actions">
        <el-input v-model="search.billNo" placeholder="发票号" clearable style="width:180px" @keyup.enter="onSearch" @clear="onSearch" />
        <el-select v-model="search.customerId" placeholder="客户" clearable filterable style="width:180px" @change="onSearch">
          <el-option v-for="c in customers" :key="c.id" :label="c.customerName" :value="c.id" />
        </el-select>
        <el-select v-model="search.status" placeholder="状态" clearable style="width:120px" @change="onSearch">
          <el-option label="草稿" value="D" />
          <el-option label="已审核" value="A" />
          <el-option label="已关闭" value="C" />
        </el-select>
        <el-button type="primary" :icon="Plus" @click="onAdd">新增发票</el-button>
      </div>
    </div>

    <el-table :data="data.list" v-loading="loading" border stripe>
      <el-table-column prop="billNo" label="发票号" width="170" />
      <el-table-column prop="customerName" label="客户" width="150" show-overflow-tooltip />
      <el-table-column prop="invoiceDate" label="开票日期" width="110" />
      <el-table-column prop="amount" label="金额" align="right" width="120">
        <template #default="{ row }">{{ fmt(row.amount) }}</template>
      </el-table-column>
      <el-table-column prop="taxAmount" label="税额" align="right" width="110">
        <template #default="{ row }">{{ fmt(row.taxAmount) }}</template>
      </el-table-column>
      <el-table-column prop="totalAmount" label="价税合计" align="right" width="130">
        <template #default="{ row }">{{ fmt(row.totalAmount) }}</template>
      </el-table-column>
      <el-table-column prop="collectedAmount" label="已收款" align="right" width="120">
        <template #default="{ row }">{{ fmt(row.collectedAmount) }}</template>
      </el-table-column>
      <el-table-column prop="uncollectedAmount" label="未收款" align="right" width="120">
        <template #default="{ row }"><span class="warn-text">{{ fmt(row.uncollectedAmount) }}</span></template>
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

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="680px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="发票号" prop="billNo"><el-input v-model="form.billNo" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发票类型">
              <el-select v-model="form.invoiceType" style="width:100%">
                <el-option label="销售发票" value="1" />
                <el-option label="其他应收" value="2" />
              </el-select>
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
            <el-form-item label="开票日期" prop="invoiceDate">
              <el-date-picker v-model="form.invoiceDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日">
              <el-date-picker v-model="form.dueDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同号"><el-input v-model="form.contractNo" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="不含税额" prop="amount">
              <el-input-number v-model="form.amount" :min="0" :precision="2" controls-position="right" style="width:100%" @change="calcTax" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="税率">
              <el-select v-model="form.taxRateNum" style="width:100%" @change="calcTax">
                <el-option v-for="r in TAX_RATES" :key="r" :label="r + '%'" :value="r" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="税额">
              <el-input-number v-model="form.taxAmount" :min="0" :precision="2" controls-position="right" style="width:100%" @change="calcTotal" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="价税合计">
              <el-input :model-value="fmt(form.totalAmount)" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收入科目">
              <el-select v-model="form.subjectCode" filterable clearable placeholder="默认取系统配置" style="width:100%">
                <el-option v-for="s in incomeSubjects" :key="s.subjectCode" :label="s.subjectCode + ' ' + s.subjectName" :value="s.subjectCode" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="2" /></el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  invoicePage, saveInvoice, updateInvoice, deleteInvoice,
  auditInvoice, invoiceVoucher, customerList
} from '@/api/receivable'
import { listSubjects } from '@/api/ledger'

const TAX_RATES = [13, 9, 6, 3, 1, 0]
const round2 = (v) => Math.round((Number(v) || 0) * 100) / 100

const loading = ref(false)
const saving = ref(false)
const formRef = ref()
const customers = ref([])
const subjects = ref([])
const data = reactive({ list: [], total: 0 })
const search = reactive({ pageNum: 1, pageSize: 10, billNo: '', customerId: null, status: '' })
const dialog = reactive({ visible: false, title: '新增销售发票' })

// 收入类明细科目（5xxx 收入 / 6001 主营业务收入），用于业财一体凭证的收入方
const incomeSubjects = computed(() =>
  subjects.value.filter((s) => s.isLeaf === 1 && /^[56]/.test(s.subjectCode || ''))
)

const emptyForm = () => ({
  id: null, billNo: 'SI' + Date.now(), invoiceType: '1', invoiceDate: today(), dueDate: '',
  customerId: null, customerName: '', taxNo: '', contractNo: '',
  amount: 0, taxRateNum: 13, taxAmount: 0, totalAmount: 0,
  subjectCode: '', remark: '', status: 'D'
})
const form = reactive(emptyForm())

const rules = {
  billNo: [{ required: true, message: '请输入发票号', trigger: 'blur' }],
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  invoiceDate: [{ required: true, message: '请选择开票日期', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }]
}

function today() { return new Date().toISOString().slice(0, 10) }
const fmt = (v) => Number(v || 0).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
const statusText = (s) => ({ D: '草稿', A: '已审核', C: '已关闭' })[s] || s || '草稿'
const statusTag = (s) => ({ D: 'info', A: 'success', C: '' })[s] || 'info'

const calcTax = () => {
  form.taxAmount = round2(Number(form.amount || 0) * Number(form.taxRateNum || 0) / 100)
  calcTotal()
}
const calcTotal = () => {
  form.totalAmount = round2(Number(form.amount || 0) + Number(form.taxAmount || 0))
}
const onCustomerChange = (id) => {
  const c = customers.value.find((x) => x.id === id)
  form.customerName = c ? c.customerName : ''
  form.taxNo = c ? (c.taxNo || '') : ''
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await invoicePage({
      pageNum: search.pageNum, pageSize: search.pageSize,
      billNo: search.billNo || undefined,
      customerId: search.customerId || undefined,
      status: search.status || undefined
    })
    data.list = res.data.records || []
    data.total = res.data.total || 0
  } finally { loading.value = false }
}

const onSearch = () => { search.pageNum = 1; loadData() }

const onAdd = () => {
  Object.assign(form, emptyForm())
  dialog.title = '新增销售发票'
  dialog.visible = true
}

const onEdit = (row) => {
  if (row.status !== 'D') { ElMessage.warning('已审核的发票不可编辑'); return }
  Object.assign(form, emptyForm(), {
    id: row.id, billNo: row.billNo, invoiceType: row.invoiceType || '1',
    invoiceDate: row.invoiceDate, dueDate: row.dueDate || '',
    customerId: row.customerId, customerName: row.customerName, taxNo: row.taxNo || '',
    contractNo: row.contractNo || '', amount: Number(row.amount || 0),
    taxRateNum: Number(row.taxRate || 13), taxAmount: Number(row.taxAmount || 0),
    totalAmount: Number(row.totalAmount || 0), subjectCode: row.subjectCode || '',
    remark: row.remark || '', status: row.status
  })
  dialog.title = '编辑销售发票'
  dialog.visible = true
}

const onSave = async () => {
  await formRef.value.validate()
  calcTotal()
  const payload = { ...form, taxRate: String(form.taxRateNum ?? 13) }
  delete payload.taxRateNum
  saving.value = true
  try {
    if (payload.id) await updateInvoice(payload)
    else await saveInvoice(payload)
    ElMessage.success('保存成功')
    dialog.visible = false
    loadData()
  } finally { saving.value = false }
}

const onAudit = async (row) => {
  await ElMessageBox.confirm('确定审核发票「' + row.billNo + '」吗？审核后不可修改。', '提示', { type: 'warning' })
  await auditInvoice(row.id)
  ElMessage.success('审核成功')
  loadData()
}

const onVoucher = async (row) => {
  await ElMessageBox.confirm(
    '将为发票「' + row.billNo + '」生成记账凭证（借：应收账款，贷：主营业务收入/应交税费），确定继续？',
    '生成凭证', { type: 'warning' }
  )
  const res = await invoiceVoucher(row.id)
  ElMessage.success('凭证已生成，ID: ' + res.data)
  loadData()
}

const onDelete = async (row) => {
  await ElMessageBox.confirm('确定删除发票「' + row.billNo + '」吗？', '提示', { type: 'warning' })
  await deleteInvoice(row.id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(async () => {
  const [cs, ss] = await Promise.all([customerList(), listSubjects()])
  customers.value = cs.data || []
  subjects.value = ss.data || []
  loadData()
})
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.header-actions { display: flex; gap: 10px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
.warn-text { color: #e6a23c; }
</style>