<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">应收核销</h2>
    </div>
    <div class="search-bar">
      <el-form :inline="true" @submit.prevent>
        <el-form-item label="客户">
          <el-select v-model="customerId" filterable clearable placeholder="选择客户" style="width:220px" @change="loadData">
            <el-option v-for="c in customers" :key="c.id" :label="c.customerName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="never" header="① 选择收款单（款项来源）" class="wo-card">
          <el-table :data="receipts" v-loading="loadingReceipt" border highlight-current-row size="small"
                    @current-change="r => { selectedReceipt = r; autoFillAmount() }">
            <el-table-column label="" width="40">
              <template #default="{ row }"><el-radio :model-value="selectedReceipt && selectedReceipt.id" :value="row.id" @click.prevent="selectedReceipt = row; autoFillAmount()">&nbsp;</el-radio></template>
            </el-table-column>
            <el-table-column prop="billNo" label="单号" width="150" />
            <el-table-column prop="receiptDate" label="日期" width="100" />
            <el-table-column prop="amount" label="金额" align="right" width="100" />
            <el-table-column prop="unappliedAmount" label="未核销" align="right" width="90">
              <template #default="{ row }"><span style="color:#e6a23c;font-weight:600;">{{ fmt(row.unappliedAmount) }}</span></template>
            </el-table-column>
          </el-table>
          <el-empty v-if="!loadingReceipt && receipts.length === 0" description="暂无待核销收款单" :image-size="60" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never" header="② 选择应收发票（款项去向）" class="wo-card">
          <el-table :data="invoices" v-loading="loadingInvoice" border highlight-current-row size="small"
                    @current-change="r => { selectedInvoice = r; autoFillAmount() }">
            <el-table-column label="" width="40">
              <template #default="{ row }"><el-radio :model-value="selectedInvoice && selectedInvoice.id" :value="row.id" @click.prevent="selectedInvoice = row; autoFillAmount()">&nbsp;</el-radio></template>
            </el-table-column>
            <el-table-column prop="billNo" label="发票号" width="150" />
            <el-table-column prop="invoiceDate" label="日期" width="100" />
            <el-table-column prop="totalAmount" label="价税合计" align="right" width="100" />
            <el-table-column prop="uncollectedAmount" label="未收" align="right" width="90">
              <template #default="{ row }"><span style="color:#f56c6c;font-weight:600;">{{ fmt(row.uncollectedAmount) }}</span></template>
            </el-table-column>
          </el-table>
          <el-empty v-if="!loadingInvoice && invoices.length === 0" description="暂无待核销发票" :image-size="60" />
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" header="③ 核销金额" class="wo-card" style="margin-top:16px;">
      <el-form :inline="true">
        <el-form-item label="核销金额">
          <el-input-number v-model="amount" :precision="2" :min="0" :max="maxAmount" :step="100" style="width:200px" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="remark" placeholder="可选" style="width:300px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Check" :loading="submitting" :disabled="!canWriteoff" @click="doWriteoff">确认核销</el-button>
        </el-form-item>
      </el-form>
      <div v-if="selectedReceipt || selectedInvoice" class="wo-tip">
        <template v-if="selectedReceipt">收款单 <b>{{ selectedReceipt.billNo }}</b> 未核销 <b>{{ fmt(selectedReceipt.unappliedAmount) }}</b></template>
        <template v-if="selectedReceipt && selectedInvoice"> · </template>
        <template v-if="selectedInvoice">发票 <b>{{ selectedInvoice.billNo }}</b> 未收 <b>{{ fmt(selectedInvoice.uncollectedAmount) }}</b></template>
      </div>
    </el-card>

    <!-- 已核销明细 -->
    <el-card shadow="never" header="已核销记录" class="wo-card" style="margin-top:16px;">
      <el-table :data="writeoffs" v-loading="loadingWo" border size="small">
        <el-table-column prop="receiptNo" label="收款单号" width="160" />
        <el-table-column prop="invoiceNo" label="发票号" width="160" />
        <el-table-column prop="writeoffAmount" label="核销金额" align="right" width="120" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="createTime" label="核销时间" width="170" />
      </el-table>
      <el-empty v-if="!loadingWo && writeoffs.length === 0" description="暂无核销记录" :image-size="60" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Check } from '@element-plus/icons-vue'
import request from '@/utils/request'

const customers = ref([])
const customerId = ref(null)
const receipts = ref([])
const invoices = ref([])
const writeoffs = ref([])
const loadingReceipt = ref(false)
const loadingInvoice = ref(false)
const loadingWo = ref(false)
const submitting = ref(false)
const selectedReceipt = ref(null)
const selectedInvoice = ref(null)
const amount = ref(0)
const remark = ref('')

const maxAmount = computed(() => {
  const a = selectedReceipt.value ? Number(selectedReceipt.value.unappliedAmount || 0) : 0
  const b = selectedInvoice.value ? Number(selectedInvoice.value.uncollectedAmount || 0) : 0
  return Math.max(0, Math.min(a, b))
})
const canWriteoff = computed(() => !!(selectedReceipt.value && selectedInvoice.value && amount.value > 0 && amount.value <= maxAmount.value))

const fmt = v => Number(v || 0).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })

const loadCustomers = async () => {
  try {
    const res = await request({ url: '/receivable/customer/list', method: 'get' })
    customers.value = res.data || []
  } catch (e) { /* ignore */ }
}

const loadData = async () => {
  receipts.value = []
  invoices.value = []
  selectedReceipt.value = null
  selectedInvoice.value = null
  amount.value = 0
  writeoffs.value = []
  if (!customerId.value) return
  loadingReceipt.value = true
  loadingInvoice.value = true
  try {
    const params = { pageNum: 1, pageSize: 200, customerId: customerId.value }
    const [r, i] = await Promise.all([
      request({ url: '/receivable/receipt/page', method: 'get', params }),
      request({ url: '/receivable/invoice/page', method: 'get', params })
    ])
    receipts.value = (r.data.records || []).filter(x => Number(x.unappliedAmount || 0) > 0 && x.status === 'A')
    invoices.value = (i.data.records || []).filter(x => Number(x.uncollectedAmount || 0) > 0 && x.status === 'A')
    loadWriteoffs()
  } finally {
    loadingReceipt.value = false
    loadingInvoice.value = false
  }
}

const loadWriteoffs = async () => {
  if (!customerId.value) return
  loadingWo.value = true
  try {
    // 取该客户所有核销记录（遍历收款单查询）
    const res = await request({ url: '/receivable/receipt/page', method: 'get', params: { pageNum: 1, pageSize: 200, customerId: customerId.value } })
    const list = res.data.records || []
    const all = []
    for (const r of list.slice(0, 20)) {
      try {
        const w = await request({ url: `/receivable/writeoff/receipt/${r.id}`, method: 'get' })
        if (w.data && w.data.length) all.push(...w.data.map(x => ({ ...x, receiptNo: r.billNo })))
      } catch (e) { /* ignore */ }
    }
    writeoffs.value = all.reverse()
  } finally {
    loadingWo.value = false
  }
}

const autoFillAmount = () => {
  if (maxAmount.value > 0 && amount.value === 0) amount.value = maxAmount.value
  if (amount.value > maxAmount.value) amount.value = maxAmount.value
}

const doWriteoff = async () => {
  await ElMessageBox.confirm(
    `确认将收款单【${selectedReceipt.value.billNo}】核销到发票【${selectedInvoice.value.billNo}】，金额 ${fmt(amount.value)}？`,
    '核销确认', { type: 'warning' })
  submitting.value = true
  try {
    await request({
      url: '/receivable/writeoff', method: 'post',
      data: { receiptId: selectedReceipt.value.id, invoiceId: selectedInvoice.value.id, amount: amount.value, remark: remark.value || null }
    })
    ElMessage.success('核销成功')
    amount.value = 0
    remark.value = ''
    loadData()
  } finally {
    submitting.value = false
  }
}

const reset = () => {
  customerId.value = null
  loadData()
}

onMounted(() => { loadCustomers() })
</script>

<style scoped>
.wo-card :deep(.el-card__header) { font-weight: 600; }
.wo-tip { color: #909399; font-size: 13px; margin-top: 4px; }
.wo-tip b { color: #303133; }
</style>
