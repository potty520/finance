<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">采购发票</h2>
      <div class="header-actions">
        <el-input v-model="search.billNo" placeholder="发票号" clearable style="width:180px" @keyup.enter="onSearch" @clear="onSearch" />
        <el-select v-model="search.supplierId" placeholder="供应商" clearable filterable style="width:180px" @change="onSearch">
          <el-option v-for="s in suppliers" :key="s.id" :label="s.supplierName" :value="s.id" />
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
      <el-table-column prop="supplierName" label="供应商" width="150" show-overflow-tooltip />
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
      <el-table-column prop="paidAmount" label="已付款" align="right" width="120">
        <template #default="{ row }">{{ fmt(row.paidAmount) }}</template>
      </el-table-column>
      <el-table-column prop="unpaidAmount" label="未付款" align="right" width="120">
        <template #default="{ row }"><span class="warn-text">{{ fmt(row.unpaidAmount) }}</span></template>
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
                <el-option label="采购发票" value="1" />
                <el-option label="费用发票" value="2" />
                <el-option label="其他应付" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplierId">
              <el-select v-model="form.supplierId" filterable style="width:100%" @change="onSupplierChange">
                <el-option v-for="s in suppliers" :key="s.id" :label="s.supplierName" :value="s.id" />
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
            <el-form-item label="采购单号"><el-input v-model="form.purchaseOrderNo" /></el-form-item>
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
            <el-form-item label="价税合计"><el-input :model-value="fmt(form.totalAmount)" readonly /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="费用科目">
              <el-select v-model="form.subjectCode" filterable clearable placeholder="默认取系统配置" style="width:100%">
                <el-option v-for="s in costSubjects" :key="s.subjectCode" :label="s.subjectCode + ' ' + s.subjectName" :value="s.subjectCode" />
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
  apInvoicePage, saveApInvoice, updateApInvoice, deleteApInvoice,
  auditApInvoice, apInvoiceVoucher, supplierList
} from '@/api/payable'
import { listSubjects } from '@/api/ledger'

const TAX_RATES = [13, 9, 6, 3, 1, 0]
const round2 = (v) => Math.round((Number(v) || 0) * 100) / 100

const loading = ref(false)
const saving = ref(false)
const formRef = ref()
const suppliers = ref([])
const subjects = ref([])
const data = reactive({ list: [], total: 0 })
const search = reactive({ pageNum: 1, pageSize: 10, billNo: '', supplierId: null, status: '' })
const dialog = reactive({ visible: false, title: '新增采购发票' })

// 成本费用类明细科目（14xx 存货 / 54xx 成本 / 66xx 费用），作为凭证借方
const costSubjects = computed(() =>
  subjects.value.filter((s) => s.isLeaf === 1 && /^(14|54|66)/.test(s.subjectCode || ''))
)

const emptyForm = () => ({
  id: null, billNo: 'PI' + Date.now(), invoiceType: '1', invoiceDate: today(), dueDate: '',
  supplierId: null, supplierName: '', taxNo: '', purchaseOrderNo: '',
  amount: 0, taxRateNum: 13, taxAmount: 0, totalAmount: 0,
  subjectCode: '', remark: '', status: 'D'
})
const form = reactive(emptyForm())

const rules = {
  billNo: [{ required: true, message: '请输入发票号', trigger: 'blur' }],
  supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
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
const onSupplierChange = (id) => {
  const s = suppliers.value.find((x) => x.id === id)
  form.supplierName = s ? s.supplierName : ''
  form.taxNo = s ? (s.taxNo || '') : ''
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await apInvoicePage({
      pageNum: search.pageNum, pageSize: search.pageSize,
      billNo: search.billNo || undefined,
      supplierId: search.supplierId || undefined,
      status: search.status || undefined
    })
    data.list = res.data.records || []
    data.total = res.data.total || 0
  } finally { loading.value = false }
}

const onSearch = () => { search.pageNum = 1; loadData() }
const onAdd = () => { Object.assign(form, emptyForm()); dialog.title = '新增采购发票'; dialog.visible = true }

const onEdit = (row) => {
  if (row.status !== 'D') { ElMessage.warning('已审核的发票不可编辑'); return }
  Object.assign(form, emptyForm(), {
    id: row.id, billNo: row.billNo, invoiceType: row.invoiceType || '1',
    invoiceDate: row.invoiceDate, dueDate: row.dueDate || '',
    supplierId: row.supplierId, supplierName: row.supplierName, taxNo: row.taxNo || '',
    purchaseOrderNo: row.purchaseOrderNo || '', amount: Number(row.amount || 0),
    taxRateNum: Number(row.taxRate || 13), taxAmount: Number(row.taxAmount || 0),
    totalAmount: Number(row.totalAmount || 0), subjectCode: row.subjectCode || '',
    remark: row.remark || '', status: row.status
  })
  dialog.title = '编辑采购发票'
  dialog.visible = true
}

const onSave = async () => {
  await formRef.value.validate()
  calcTotal()
  const payload = { ...form, taxRate: String(form.taxRateNum ?? 13) }
  delete payload.taxRateNum
  saving.value = true
  try {
    if (payload.id) await updateApInvoice(payload)
    else await saveApInvoice(payload)
    ElMessage.success('保存成功')
    dialog.visible = false
    loadData()
  } finally { saving.value = false }
}

const onAudit = async (row) => {
  await ElMessageBox.confirm('确定审核发票「' + row.billNo + '」吗？审核后不可修改。', '提示', { type: 'warning' })
  await auditApInvoice(row.id)
  ElMessage.success('审核成功')
  loadData()
}

const onVoucher = async (row) => {
  await ElMessageBox.confirm(
    '将为发票「' + row.billNo + '」生成记账凭证（借：库存商品或费用/应交税费-进项，贷：应付账款），确定继续？',
    '生成凭证', { type: 'warning' }
  )
  const res = await apInvoiceVoucher(row.id)
  ElMessage.success('凭证已生成，ID: ' + res.data)
  loadData()
}

const onDelete = async (row) => {
  await ElMessageBox.confirm('确定删除发票「' + row.billNo + '」吗？', '提示', { type: 'warning' })
  await deleteApInvoice(row.id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(async () => {
  const [sp, ss] = await Promise.all([supplierList(), listSubjects()])
  suppliers.value = sp.data || []
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