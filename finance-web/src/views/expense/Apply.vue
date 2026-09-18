<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">费用报销申请</h2>
      <div class="header-actions">
        <el-select v-model="search.status" placeholder="状态" clearable style="width:130px" @change="onSearch">
          <el-option v-for="(label, key) in STATUS_TEXT" :key="key" :label="label" :value="key" />
        </el-select>
        <el-button type="primary" :icon="Plus" @click="onAdd">新增申请</el-button>
      </div>
    </div>

    <el-table :data="data.list" v-loading="loading" border stripe>
      <el-table-column prop="billNo" label="单号" width="170" />
      <el-table-column label="类型" width="100">
        <template #default="{ row }">{{ APPLY_TYPE_MAP[row.applyType] || row.applyType || '-' }}</template>
      </el-table-column>
      <el-table-column label="费用科目" min-width="170">
        <template #default="{ row }">
          <span v-if="row.subjectCode">{{ row.subjectCode }} {{ row.subjectName }}</span>
          <span v-else>{{ row.subjectName || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="金额" align="right" width="130">
        <template #default="{ row }">{{ fmt(row.amount) }}</template>
      </el-table-column>
      <el-table-column prop="applyDate" label="申请日期" width="110" />
      <el-table-column prop="applicantName" label="申请人" width="100">
        <template #default="{ row }">{{ row.applicantName || '-' }}</template>
      </el-table-column>
      <el-table-column prop="deptName" label="部门" width="110">
        <template #default="{ row }">{{ row.deptName || '-' }}</template>
      </el-table-column>
      <el-table-column prop="reason" label="事由" min-width="160" show-overflow-tooltip />
      <el-table-column label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="STATUS_TAG[row.status] || 'info'" size="small">{{ STATUS_TEXT[row.status] || row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="voucherNo" label="凭证号" width="150">
        <template #default="{ row }">{{ row.voucherNo || '-' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status === '1'" link type="primary" size="small" @click="onPay(row)">付款</el-button>
          <el-button
            v-if="(row.status === '1' || row.status === '3') && !row.voucherNo"
            link type="success" size="small" @click="onVoucher(row)">生成凭证</el-button>
          <span v-if="row.status !== '1' && row.voucherNo" class="muted">已生成</span>
          <span v-if="row.status === '0'" class="muted">待审批</span>
          <span v-if="row.status === '2'" class="muted">已驳回</span>
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

    <el-dialog v-model="dialog.visible" title="新增费用申请" width="640px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="费用类型" prop="applyType">
              <el-select v-model="form.applyType" style="width:100%">
                <el-option v-for="(label, key) in APPLY_TYPE_MAP" :key="key" :label="label" :value="key" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请日期" prop="applyDate">
              <el-date-picker v-model="form.applyDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="费用科目" prop="subjectCode">
              <el-select v-model="form.subjectCode" filterable style="width:100%" @change="onSubjectChange">
                <el-option v-for="s in expenseSubjects" :key="s.subjectCode" :label="s.subjectCode + ' ' + s.subjectName" :value="s.subjectCode" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="金额" prop="amount">
              <el-input-number v-model="form.amount" :min="0.01" :precision="2" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="事由" prop="reason">
              <el-input v-model="form.reason" type="textarea" :rows="2" placeholder="请填写报销事由" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item>
          </el-col>
        </el-row>
        <el-alert type="info" :closable="false" show-icon
          title="申请人自动取当前登录用户。提交后进入审批流，审批通过后可标记付款并生成记账凭证（借：费用科目，贷：其他应付款/银行存款）。" />
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSave">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { applyPage, saveApply, payApply, applyVoucher } from '@/api/expense'
import { listSubjects } from '@/api/ledger'

const STATUS_TEXT = { '0': '待审批', '1': '已通过', '2': '已驳回', '3': '已付款' }
const STATUS_TAG = { '0': 'warning', '1': 'success', '2': 'danger', '3': '' }
const APPLY_TYPE_MAP = { EXPENSE: '费用', TRAVEL: '差旅', ENTERTAIN: '招待费', OFFICE: '办公费', COMM: '通讯费', OTHER: '其他' }

const loading = ref(false)
const saving = ref(false)
const formRef = ref()
const subjects = ref([])
const data = reactive({ list: [], total: 0 })
const search = reactive({ pageNum: 1, pageSize: 10, status: '' })
const dialog = reactive({ visible: false })

// 费用类明细科目（66xx）
const expenseSubjects = computed(() =>
  subjects.value.filter((s) => s.isLeaf === 1 && /^66/.test(s.subjectCode || ''))
)

const emptyForm = () => ({
  billNo: 'EX' + Date.now(), applyType: 'TRAVEL', applyDate: today(),
  subjectCode: '', subjectName: '', amount: 0, reason: '', remark: ''
})
const form = reactive(emptyForm())

const rules = {
  applyType: [{ required: true, message: '请选择费用类型', trigger: 'change' }],
  applyDate: [{ required: true, message: '请选择申请日期', trigger: 'change' }],
  subjectCode: [{ required: true, message: '请选择费用科目', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  reason: [{ required: true, message: '请填写事由', trigger: 'blur' }]
}

function today() { return new Date().toISOString().slice(0, 10) }
const fmt = (v) => Number(v || 0).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
const onSubjectChange = (code) => {
  const s = subjects.value.find((x) => x.subjectCode === code)
  form.subjectName = s ? s.subjectName : ''
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await applyPage({
      pageNum: search.pageNum, pageSize: search.pageSize,
      status: search.status || undefined
    })
    data.list = res.data.records || []
    data.total = res.data.total || 0
  } finally { loading.value = false }
}

const onSearch = () => { search.pageNum = 1; loadData() }
const onAdd = () => { Object.assign(form, emptyForm()); dialog.visible = true }

const onSave = async () => {
  await formRef.value.validate()
  onSubjectChange(form.subjectCode)
  saving.value = true
  try {
    await saveApply(form)
    ElMessage.success('已提交，等待审批')
    dialog.visible = false
    loadData()
  } finally { saving.value = false }
}

const onPay = async (row) => {
  await ElMessageBox.confirm('确认将报销单「' + row.billNo + '」标记为已付款？', '提示', { type: 'warning' })
  await payApply(row.id)
  ElMessage.success('已标记付款')
  loadData()
}

const onVoucher = async (row) => {
  await ElMessageBox.confirm(
    '将为报销单「' + row.billNo + '」生成记账凭证（借：费用科目，贷：银行存款/其他应付款），确定继续？',
    '生成凭证', { type: 'warning' }
  )
  const res = await applyVoucher(row.id)
  ElMessage.success('凭证已生成，ID: ' + res.data)
  loadData()
}

onMounted(async () => {
  const res = await listSubjects()
  subjects.value = res.data || []
  loadData()
})
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.header-actions { display: flex; gap: 10px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
.muted { color: #909399; font-size: 12px; }
</style>