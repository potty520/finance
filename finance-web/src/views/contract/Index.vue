<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">合同管理</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增合同</el-button>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="合同类型">
          <el-select v-model="query.contractType" clearable>
            <el-option label="采购" value="BUY" />
            <el-option label="销售" value="SELL" />
            <el-option label="服务" value="SERVICE" />
            <el-option label="租赁" value="LEASE" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable>
            <el-option label="待审" value="0" />
            <el-option label="生效" value="1" />
            <el-option label="终止" value="2" />
            <el-option label="完成" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="contractNo" label="合同号" width="180" />
      <el-table-column prop="contractName" label="合同名称" />
      <el-table-column prop="contractType" label="类型" width="100">
        <template #default="{ row }">
          <el-tag>{{ { 'BUY': '采购', 'SELL': '销售', 'SERVICE': '服务', 'LEASE': '租赁' }[row.contractType] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="partyName" label="对方" width="160" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="signDate" label="签订日期" width="120" />
      <el-table-column prop="endDate" label="到期日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="{ '0': 'warning', '1': 'success', '2': 'info', '3': 'primary' }[row.status]">
            {{ { '0': '待审', '1': '生效', '2': '终止', '3': '完成' }[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="onTerminate(row)" v-if="row.status === '1'">终止</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="700px">
      <el-form :model="form" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="合同号"><el-input v-model="form.contractNo" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="合同名"><el-input v-model="form.contractName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="类型">
            <el-select v-model="form.contractType">
              <el-option label="采购" value="BUY" />
              <el-option label="销售" value="SELL" />
              <el-option label="服务" value="SERVICE" />
              <el-option label="租赁" value="LEASE" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="对方"><el-input v-model="form.partyName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="金额"><el-input-number v-model="form.amount" :precision="2" :min="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="签订日"><el-date-picker v-model="form.signDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="起始日"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="到期日"><el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="内容"><el-input v-model="form.content" type="textarea" /></el-form-item></el-col>
        </el-row>
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
import { CONTRACT_TYPE_MAP, STATUS_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })
const query = reactive({ contractType: '', status: '' })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, contractNo: '', contractName: '', contractType: 'BUY', partyName: '', amount: 0, signDate: '', startDate: '', endDate: '', content: '', ownerId: 1, ownerName: '当前用户', status: '0' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/contract/page', method: 'get', params: { ...query, pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, contractNo: 'CT-' + Date.now(), signDate: new Date().toISOString().substr(0, 10) }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  await request({ url: '/contract', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onTerminate = async (row) => {
  await ElMessageBox.confirm('确认终止合同？', '提示', { type: 'warning' })
  await request({ url: `/contract/terminate/${row.id}`, method: 'post', params: { contractType: row.contractType } })
  ElMessage.success('已终止'); loadData()
}

onMounted(loadData)
</script>
