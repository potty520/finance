<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">客户档案</h2>
      <div class="header-actions">
        <el-input v-model="search.keyword" placeholder="客户编码/名称" clearable style="width:200px" @keyup.enter="onSearch" @clear="onSearch" />
        <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
      </div>
    </div>

    <el-table :data="data.list" v-loading="loading" border stripe>
      <el-table-column prop="customerCode" label="客户编码" width="130" />
      <el-table-column prop="customerName" label="客户名称" min-width="160" />
      <el-table-column prop="customerType" label="客户类型" width="100">
        <template #default="{ row }">{{ row.customerType || '-' }}</template>
      </el-table-column>
      <el-table-column prop="contact" label="联系人" width="100" />
      <el-table-column prop="phone" label="电话" width="130" />
      <el-table-column prop="creditLimit" label="信用额度" width="120" align="right">
        <template #default="{ row }">¥{{ fmt(row.creditLimit) }}</template>
      </el-table-column>
      <el-table-column prop="creditDays" label="账期(天)" width="90" align="center" />
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="onEdit(row)">编辑</el-button>
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
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="640px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="客户编码" prop="customerCode">
              <el-input v-model="form.customerCode" placeholder="如 C001" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="form.customerName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户类型">
              <el-select v-model="form.customerType" clearable placeholder="请选择" style="width:100%">
                <el-option label="一般客户" value="普通" />
                <el-option label="重点客户" value="重点" />
                <el-option label="关联方" value="关联方" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="税号">
              <el-input v-model="form.taxNo" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人">
              <el-input v-model="form.contact" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话">
              <el-input v-model="form.phone" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开户行">
              <el-input v-model="form.bankName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行账号">
              <el-input v-model="form.bankAccount" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="信用额度">
              <el-input-number v-model="form.creditLimit" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="信用账期(天)">
              <el-input-number v-model="form.creditDays" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="地址">
              <el-input v-model="form.address" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="2" />
            </el-form-item>
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
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { customerPage, saveCustomer, updateCustomer, deleteCustomer } from '@/api/receivable'

const loading = ref(false)
const saving = ref(false)
const formRef = ref()
const data = reactive({ list: [], total: 0 })
const search = reactive({ pageNum: 1, pageSize: 10, keyword: '' })
const dialog = reactive({ visible: false, title: '新增客户' })

const emptyForm = () => ({
  id: null, customerCode: '', customerName: '', customerType: '普通', taxNo: '',
  bankName: '', bankAccount: '', contact: '', phone: '', address: '',
  creditLimit: 0, creditDays: 0, status: 1, remark: ''
})
const form = reactive(emptyForm())

const rules = {
  customerCode: [{ required: true, message: '请输入客户编码', trigger: 'blur' }],
  customerName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }]
}

const fmt = (v) => Number(v || 0).toLocaleString('zh-CN', { minimumFractionDigits: 2 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await customerPage({ pageNum: search.pageNum, pageSize: search.pageSize, keyword: search.keyword || undefined })
    data.list = res.data.records || []
    data.total = res.data.total || 0
  } finally { loading.value = false }
}

const onSearch = () => { search.pageNum = 1; loadData() }
const onAdd = () => {
  Object.assign(form, emptyForm())
  dialog.title = '新增客户'
  dialog.visible = true
}
const onEdit = (row) => {
  Object.assign(form, emptyForm(), JSON.parse(JSON.stringify(row)))
  dialog.title = '编辑客户'
  dialog.visible = true
}
const onDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除客户「${row.customerName}」吗？`, '提示', { type: 'warning' })
  await deleteCustomer(row.id)
  ElMessage.success('删除成功')
  loadData()
}
const onSave = async () => {
  await formRef.value.validate()
  saving.value = true
  try {
    if (form.id) await updateCustomer(form)
    else await saveCustomer(form)
    ElMessage.success('保存成功')
    dialog.visible = false
    loadData()
  } finally { saving.value = false }
}

onMounted(loadData)
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.header-actions { display: flex; gap: 10px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
