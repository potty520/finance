<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">{{ pageTitle }}</h2>
      <div>
        <el-button type="primary" :icon="Plus" @click="onAdd">新增凭证</el-button>
        <el-button :icon="Refresh" @click="loadData">刷新</el-button>
      </div>
    </div>
    <div class="search-bar">
      <el-form :inline="true" :model="search">
        <el-form-item label="凭证号"><el-input v-model="search.voucherNo" clearable /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="search.status" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="待审核" value="APPROVING" />
            <el-option label="已审核" value="APPROVED" />
            <el-option label="已过账" value="POSTED" />
            <el-option label="已驳回" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="期间">
          <el-date-picker v-model="search.periodMonth" type="month" placeholder="选择月份" value-format="YYYY-MM" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="search = {}">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border stripe>
      <el-table-column prop="voucherNo" label="凭证号" width="180" />
      <el-table-column prop="voucherDate" label="制单日期" width="120" />
      <el-table-column prop="fiscalYear" label="年度" width="80" />
      <el-table-column prop="fiscalPeriod" label="期间" width="80" />
      <el-table-column prop="summary" label="摘要" />
      <el-table-column prop="totalDebit" label="借方金额" width="120" align="right" />
      <el-table-column prop="totalCredit" label="贷方金额" width="120" align="right" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ statusName(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300" fixed="right">
        <template #default="{ row }">
          <el-button type="info" link @click.stop="onPreview(row)">预览</el-button>
          <el-button type="primary" link @click.stop="onPrint(row)">打印</el-button>
          <el-button type="primary" link @click.stop="onEdit(row)" v-if="canEdit(row)">编辑</el-button>
          <el-button type="success" link @click.stop="onApprove(row)" v-if="canApprove(row)">审核</el-button>
          <el-button type="warning" link @click.stop="onPost(row)" v-if="canPost(row)">过账</el-button>
          <el-button type="danger" link @click.stop="onDel(row)" v-if="canDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="search.pageNum"
      v-model:page-size="search.pageSize"
      :total="data.total"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50, 100]"
      @current-change="loadData"
      @size-change="loadData"
      style="margin-top:16px; justify-content: flex-end;"
    />

    <el-dialog v-model="preview.visible" title="凭证预览与打印" width="960px" destroy-on-close class="voucher-preview-dialog">
      <VoucherPrintSettings v-model="paperSettings" />
      <VoucherPrintSheet
        v-if="preview.voucher"
        :voucher="preview.voucher"
        :company-name="preview.companyName"
        :paper-options="paperSettings"
      />
      <template #footer>
        <el-button @click="preview.visible = false">关闭</el-button>
        <el-button type="primary" :icon="Printer" @click="doPrint">打印</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="printDialog.visible" title="打印凭证" width="720px" destroy-on-close>
      <VoucherPrintSettings v-model="paperSettings" />
      <div class="print-dialog-tip">已加载凭证：{{ printDialog.voucherNo || '-' }}</div>
      <template #footer>
        <el-button @click="printDialog.visible = false">取消</el-button>
        <el-button type="primary" :icon="Printer" @click="confirmPrint">确认打印</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="920px" destroy-on-close @open="ensureSubjects">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="凭证号">
              <el-input
                v-model="form.voucherNo"
                disabled
                :placeholder="form.id ? '' : '保存时自动生成'"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="制单日期" prop="voucherDate">
              <el-date-picker v-model="form.voucherDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年度" prop="fiscalYear">
              <el-input v-model="form.fiscalYear" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="期间" prop="fiscalPeriod">
              <el-input-number v-model="form.fiscalPeriod" :min="1" :max="12" />
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="摘要" prop="summary">
              <el-input v-model="form.summary" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider>分录</el-divider>
        <el-table :data="form.entries" border>
          <el-table-column label="科目编码" width="180">
            <template #default="{ row }">
              <el-select
                v-model="row.subjectCode"
                filterable
                clearable
                placeholder="选择编码"
                style="width:100%"
                :loading="subjectLoading"
                @change="(code) => onSubjectSelect(row, code)"
              >
                <el-option
                  v-for="s in leafSubjectOptions"
                  :key="s.subjectCode"
                  :label="`${s.subjectCode} ${s.subjectName}`"
                  :value="s.subjectCode"
                >
                  <span>{{ s.subjectCode }}</span>
                  <span class="subject-option-name">{{ s.subjectName }}</span>
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="科目名称" min-width="200">
            <template #default="{ row }">
              <el-select
                v-model="row.subjectCode"
                filterable
                clearable
                placeholder="选择名称"
                style="width:100%"
                :loading="subjectLoading"
                @change="(code) => onSubjectSelect(row, code)"
              >
                <el-option
                  v-for="s in leafSubjectOptions"
                  :key="'n-' + s.subjectCode"
                  :label="`${s.subjectName} ${s.subjectCode}`"
                  :value="s.subjectCode"
                >
                  <span class="subject-option-code">{{ s.subjectCode }}</span>
                  <span>{{ s.subjectName }}</span>
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="摘要" width="200">
            <template #default="{ row }">
              <el-input v-model="row.summary" />
            </template>
          </el-table-column>
          <el-table-column label="借方" width="140">
            <template #default="{ row }">
              <el-input-number v-model="row.debitAmount" :precision="2" :min="0" :controls="false" style="width:100%" />
            </template>
          </el-table-column>
          <el-table-column label="贷方" width="140">
            <template #default="{ row }">
              <el-input-number v-model="row.creditAmount" :precision="2" :min="0" :controls="false" style="width:100%" />
            </template>
          </el-table-column>
          <el-table-column label="" width="60">
            <template #default="{ $index }">
              <el-button type="danger" link @click="form.entries.splice($index, 1)">删</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button type="primary" link @click="addEntry" style="margin-top:8px;">+ 添加分录</el-button>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Printer } from '@element-plus/icons-vue'
import { page, detail, save, update, del, post, approve, listSubjects, nextVoucherNo } from '@/api/ledger'
import request from '@/utils/request'
import VoucherPrintSheet from '@/components/VoucherPrintSheet.vue'
import VoucherPrintSettings from '@/components/VoucherPrintSettings.vue'
import { buildVoucherHtml, printVoucherHtml, loadPaperSettings, statusLabel } from '@/utils/voucherPrint'

const route = useRoute()
const pageTitle = computed(() => route.meta.title || '凭证管理')
const loading = ref(false)
const data = reactive({ list: [], total: 0 })
const search = reactive({ pageNum: 1, pageSize: 10, voucherNo: '', status: '', periodMonth: null })
const dialog = reactive({ visible: false, title: '新增凭证' })
const preview = reactive({ visible: false, voucher: null, companyName: '财务管理系统' })
const printDialog = reactive({ visible: false, voucher: null, voucherNo: '' })
const paperSettings = reactive(loadPaperSettings())
const formRef = ref()
const subjectLoading = ref(false)
const subjectOptions = ref([])
const form = reactive({ id: null, voucherNo: '', voucherDate: '', fiscalYear: '', fiscalPeriod: 1, summary: '', entries: [] })

const leafSubjectOptions = computed(() => {
  const list = subjectOptions.value
  if (!list.length) return []
  const idsWithChildren = new Set()
  list.forEach((s) => {
    if (s.parentId && s.parentId > 0) idsWithChildren.add(s.parentId)
  })
  return list.filter((s) => !idsWithChildren.has(s.id))
})
const rules = {
  voucherDate: [{ required: true, message: '请选择日期' }],
  fiscalYear: [{ required: true, message: '请输入年度' }]
}

const statusName = (s) => statusLabel(s)
const statusType = (s) => {
  if (['POSTED', 'P'].includes(s)) return 'success'
  if (['APPROVED', 'A'].includes(s)) return 'success'
  if (s === 'APPROVING') return 'warning'
  if (['REJECTED', 'R'].includes(s)) return 'danger'
  return 'info'
}

const canEdit = (row) => ['DRAFT', 'REJECTED', 'D', 'R'].includes(row.status)
const canApprove = (row) => ['DRAFT', 'REJECTED', 'APPROVING', 'D', 'R'].includes(row.status)
const canPost = (row) => ['APPROVED', 'A'].includes(row.status)
const canDelete = (row) => !['POSTED', 'P'].includes(row.status)

const buildQuery = () => {
  const params = {
    pageNum: search.pageNum,
    pageSize: search.pageSize,
    voucherNo: search.voucherNo || undefined,
    status: search.status || undefined
  }
  if (search.periodMonth) {
    const [year, month] = search.periodMonth.split('-')
    params.fiscalYear = year
    params.fiscalPeriod = Number(month)
  }
  return params
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await page(buildQuery())
    data.list = res.data.records
    data.total = res.data.total
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  const now = new Date()
  Object.assign(form, {
    id: null,
    voucherNo: '',
    voucherDate: now.toISOString().slice(0, 10),
    fiscalYear: String(now.getFullYear()),
    fiscalPeriod: now.getMonth() + 1,
    summary: '',
    entries: []
  })
}

const refreshVoucherNoPreview = async () => {
  if (form.id) return
  if (!form.fiscalYear || !form.fiscalPeriod) return
  try {
    const res = await nextVoucherNo({
      fiscalYear: form.fiscalYear,
      fiscalPeriod: form.fiscalPeriod
    })
    form.voucherNo = res.data || ''
  } catch (e) {
    form.voucherNo = ''
  }
}

const ensureSubjects = async () => {
  if (subjectOptions.value.length) return
  subjectLoading.value = true
  try {
    const res = await listSubjects()
    subjectOptions.value = res.data || []
  } catch (e) {
    ElMessage.error('加载科目列表失败')
  } finally {
    subjectLoading.value = false
  }
}

const onSubjectSelect = (row, code) => {
  const subject = leafSubjectOptions.value.find((s) => s.subjectCode === code)
  row.subjectCode = code || ''
  row.subjectName = subject?.subjectName || ''
}

const onAdd = async () => {
  resetForm()
  dialog.title = '新增凭证'
  dialog.visible = true
  await ensureSubjects()
  await refreshVoucherNoPreview()
}

const onEdit = async (row) => {
  loading.value = true
  try {
    const voucher = await loadVoucherDetail(row)
    Object.assign(form, voucher)
    if (!form.entries?.length) form.entries = []
    dialog.title = '编辑凭证'
    dialog.visible = true
    await ensureSubjects()
  } catch (e) {
    ElMessage.error('加载凭证详情失败')
  } finally {
    loading.value = false
  }
}

const addEntry = () => {
  form.entries.push({ subjectCode: '', subjectName: '', summary: '', debitAmount: 0, creditAmount: 0 })
}

const onSave = async () => {
  await formRef.value.validate()
  const sumDebit = form.entries.reduce((a, b) => a + Number(b.debitAmount || 0), 0)
  const sumCredit = form.entries.reduce((a, b) => a + Number(b.creditAmount || 0), 0)
  if (Math.abs(sumDebit - sumCredit) > 0.01) {
    ElMessage.error('借贷不平衡：' + sumDebit.toFixed(2) + ' vs ' + sumCredit.toFixed(2))
    return
  }
  form.totalDebit = sumDebit
  form.totalCredit = sumCredit
  const payload = { ...form, entries: [...form.entries] }
  if (!form.id) payload.voucherNo = ''
  if (form.id) await update(payload)
  else await save(payload)
  ElMessage.success('保存成功')
  dialog.visible = false
  loadData()
}

const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await del(row.id)
  ElMessage.success('已删除')
  loadData()
}

const onApprove = async (row) => {
  await ElMessageBox.confirm('确认审核？', '提示', { type: 'warning' })
  await approve(row.id)
  ElMessage.success('已审核')
  loadData()
}

const onPost = async (row) => {
  await ElMessageBox.confirm('确认过账？', '提示', { type: 'warning' })
  await post(row.id)
  ElMessage.success('已过账')
  loadData()
}

const loadCompanyName = async () => {
  try {
    const res = await request({ url: '/system/company/list', method: 'get' })
    const list = res.data || []
    const enabled = list.find(c => c.status === 1) || list[0]
    if (enabled?.companyName) preview.companyName = enabled.companyName
  } catch (e) {}
}

const loadVoucherDetail = async (row) => {
  const res = await detail(row.id)
  return res.data
}

const onPreview = async (row) => {
  try {
    loading.value = true
    preview.voucher = await loadVoucherDetail(row)
    await loadCompanyName()
    preview.visible = true
  } catch (e) {
    ElMessage.error('加载凭证详情失败')
  } finally {
    loading.value = false
  }
}

const onPrint = async (row) => {
  try {
    loading.value = true
    const voucher = await loadVoucherDetail(row)
    await loadCompanyName()
    printDialog.voucher = voucher
    printDialog.voucherNo = voucher.voucherNo
    printDialog.visible = true
  } catch (e) {
    ElMessage.error('加载凭证详情失败')
  } finally {
    loading.value = false
  }
}

const doPrint = () => {
  if (!preview.voucher) return
  try {
    const html = buildVoucherHtml(preview.voucher, preview.companyName)
    printVoucherHtml(html, `凭证-${preview.voucher.voucherNo || preview.voucher.id}`, paperSettings)
  } catch (e) {
    ElMessage.error(e.message || '打印失败')
  }
}

const confirmPrint = () => {
  if (!printDialog.voucher) return
  try {
    const html = buildVoucherHtml(printDialog.voucher, preview.companyName)
    printVoucherHtml(html, `凭证-${printDialog.voucher.voucherNo || printDialog.voucher.id}`, paperSettings)
    printDialog.visible = false
  } catch (e) {
    ElMessage.error(e.message || '打印失败')
  }
}

onMounted(loadData)

watch(
  () => [form.fiscalYear, form.fiscalPeriod, form.id],
  () => {
    if (!form.id && dialog.visible) refreshVoucherNoPreview()
  }
)
</script>

<style scoped lang="scss">
:deep(.voucher-preview-dialog .el-dialog__body) {
  padding-top: 8px;
  background: #f5f5f5;
}

.print-dialog-tip {
  margin-top: 8px;
  font-size: 13px;
  color: #606266;
}

.subject-option-name {
  margin-left: 8px;
  color: #909399;
  font-size: 12px;
}

.subject-option-code {
  display: inline-block;
  min-width: 56px;
  margin-right: 8px;
  color: #606266;
  font-family: Consolas, monospace;
}
</style>
