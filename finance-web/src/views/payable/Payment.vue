<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">付款单</h2>
      <div class="header-actions">
        <el-input v-model="search.billNo" placeholder="付款单号" clearable style="width:180px" @keyup.enter="onSearch" @clear="onSearch" />
        <el-select v-model="search.supplierId" placeholder="供应商" clearable filterable style="width:180px" @change="onSearch">
          <el-option v-for="s in suppliers" :key="s.id" :label="s.supplierName" :value="s.id" />
        </el-select>
        <el-button type="primary" :icon="Plus" @click="onAdd">新增付款</el-button>
      </div>
    </div>

    <el-table :data="data.list" v-loading="loading" border stripe>
      <el-table-column prop="billNo" label="付款单号" width="180" />
      <el-table-column prop="supplierName" label="供应商" width="160" />
      <el-table-column prop="paymentDate" label="付款日期" width="120" />
      <el-table-column prop="amount" label="金额" align="right" width="130">
        <template #default="{ row }">¥{{ fmt(row.amount) }}</template>
      </el-table-column>
      <el-table-column label="方式" width="100">
        <template #default="{row}">{{ PAY_TYPE_MAP[row.paymentType] || row.paymentType || '-' }}</template>
      </el-table-column>
      <el-table-column prop="accountName" label="账户" width="130" />
      <el-table-column label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="statusTag(row.status)" size="small">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="190" align="center" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="onEdit(row)">编辑</el-button>
          <el-button v-if="!isAudited(row)" link type="warning" size="small" @click="onAudit(row)">审核</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="560px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="供应商" prop="supplierId">
          <el-select v-model="form.supplierId" placeholder="请选择供应商" filterable style="width:100%">
            <el-option v-for="s in suppliers" :key="s.id" :label="s.supplierName" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="付款日期" prop="paymentDate">
          <el-date-picker v-model="form.paymentDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
        </el-form-item>
        <el-form-item label="付款金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0.01" :precision="2" style="width:100%" />
        </el-form-item>
        <el-form-item label="付款方式">
          <el-select v-model="form.paymentType" style="width:100%">
            <el-option v-for="(label, key) in PAY_TYPE_MAP" :key="key" :label="label" :value="key" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
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
import { paymentPage, savePayment, updatePayment, deletePayment, auditPayment, supplierList } from '@/api/payable'

const PAY_TYPE_MAP = { BANK: '银行转账', CASH: '现金', CHECK: '支票', DRAFT: '汇票', ALIPAY: '支付宝', WECHAT: '微信支付', OTHER: '其他' }

const loading = ref(false)
const saving = ref(false)
const formRef = ref()
const suppliers = ref([])
const data = reactive({ list: [], total: 0 })
const search = reactive({ pageNum: 1, pageSize: 10, billNo: '', supplierId: null })
const dialog = reactive({ visible: false, title: '新增付款单' })

const emptyForm = () => ({
  id: null, supplierId: null, paymentDate: new Date().toISOString().slice(0, 10),
  amount: 0, paymentType: 'BANK', remark: ''
})
const form = reactive(emptyForm())

const rules = {
  supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
  paymentDate: [{ required: true, message: '请选择付款日期', trigger: 'change' }],
  amount: [{ required: true, message: '请输入付款金额', trigger: 'blur' }]
}

const fmt = (v) => Number(v || 0).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
const statusText = (s) => ({ DRAFT: '草稿', D: '草稿', SUBMITTED: '待审核', APPROVING: '审核中', APPROVED: '已审核', A: '已审核', POSTED: '已过账', P: '已过账', REJECTED: '已驳回', R: '已驳回' })[s] || s || '草稿'
const statusTag = (s) => ({ DRAFT: 'info', D: 'info', SUBMITTED: 'warning', APPROVING: 'warning', APPROVED: 'success', A: 'success', POSTED: 'success', P: 'success', REJECTED: 'danger', R: 'danger' })[s] || 'info'
const isAudited = (row) => ['APPROVED', 'A', 'POSTED', 'P'].includes(row.status)

const loadData = async () => {
  loading.value = true
  try {
    const res = await paymentPage({
      pageNum: search.pageNum, pageSize: search.pageSize,
      billNo: search.billNo || undefined, supplierId: search.supplierId || undefined
    })
    data.list = res.data.records || []
    data.total = res.data.total || 0
  } finally { loading.value = false }
}

const onSearch = () => { search.pageNum = 1; loadData() }
const onAdd = () => {
  Object.assign(form, emptyForm())
  dialog.title = '新增付款单'
  dialog.visible = true
}
const onEdit = (row) => {
  Object.assign(form, emptyForm(), {
    id: row.id, supplierId: row.supplierId, paymentDate: row.paymentDate,
    amount: row.amount, paymentType: row.paymentType, remark: row.remark
  })
  dialog.title = '编辑付款单'
  dialog.visible = true
}
const onAudit = async (row) => {
  await ElMessageBox.confirm(`确定审核付款单「${row.billNo}」吗？`, '提示', { type: 'warning' })
  await auditPayment(row.id)
  ElMessage.success('审核成功')
  loadData()
}
const onDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除付款单「${row.billNo}」吗？`, '提示', { type: 'warning' })
  await deletePayment(row.id)
  ElMessage.success('删除成功')
  loadData()
}
const onSave = async () => {
  await formRef.value.validate()
  saving.value = true
  try {
    if (form.id) await updatePayment(form)
    else await savePayment(form)
    ElMessage.success('保存成功')
    dialog.visible = false
    loadData()
  } finally { saving.value = false }
}

onMounted(async () => {
  const res = await supplierList()
  suppliers.value = res.data || []
  loadData()
})
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.header-actions { display: flex; gap: 10px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
