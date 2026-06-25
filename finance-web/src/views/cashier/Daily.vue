<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">出纳日记账</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="账户"><el-select v-model="query.accountId" clearable><el-option v-for="a in accounts" :key="a.id" :label="a.accountName" :value="a.id" /></el-select></el-form-item>
        <el-form-item label="日期"><el-date-picker v-model="query.date" type="date" /></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="单号" width="180" />
      <el-table-column prop="transDate" label="日期" width="120" />
      <el-table-column prop="accountName" label="账户" width="160" />
      <el-table-column prop="ioType" label="收/付" width="80">
        <template #default="{ row }">
          <el-tag :type="row.ioType === '1' ? 'success' : 'danger'">{{ row.ioType === '1' ? '收入' : '支出' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="summary" label="摘要" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="balance" label="余额" align="right" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="单号"><el-input v-model="form.billNo" /></el-form-item>
        <el-form-item label="日期"><el-date-picker v-model="form.transDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="账户">
          <el-select v-model="form.accountId">
            <el-option v-for="a in accounts" :key="a.id" :label="a.accountName" :value="a.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="收/付">
          <el-radio-group v-model="form.ioType">
            <el-radio value="1">收入</el-radio>
            <el-radio value="2">支出</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="金额"><el-input-number v-model="form.amount" :precision="2" :min="0" /></el-form-item>
        <el-form-item label="摘要"><el-input v-model="form.summary" /></el-form-item>
        <el-form-item label="对方科目"><el-input v-model="form.oppositeSubject" /></el-form-item>
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
const accounts = ref([])
const data = reactive({ list: [] })
const query = reactive({ accountId: null, date: '' })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, billNo: '', transDate: '', accountId: null, accountName: '', ioType: '1', amount: 0, summary: '', oppositeSubject: '' })

const loadAccounts = async () => {
  const res = await request({ url: '/cashier/account/list', method: 'get' })
  accounts.value = res.data
}
const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/cashier/daily/page', method: 'get', params: { ...query, pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, billNo: 'CASH-' + Date.now() }); dialog.title = '新增'; dialog.visible = true }
const onEdit = (row) => { Object.assign(form, row); dialog.title = '编辑'; dialog.visible = true }
const onSave = async () => {
  if (form.id) await request({ url: '/cashier/daily', method: 'put', data: form })
  else await request({ url: '/cashier/daily', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/cashier/daily/${row.id}`, method: 'delete' })
  loadData()
}

onMounted(() => { loadAccounts(); loadData() })
</script>
