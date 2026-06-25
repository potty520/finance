<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">{{ pageTitle }}</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增资产</el-button>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="资产编码"><el-input v-model="query.cardCode" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="query.cardName" /></el-form-item>
        <el-form-item label="使用状态">
          <el-select v-model="query.useStatus" clearable>
            <el-option label="在用" value="1" />
            <el-option label="闲置" value="2" />
            <el-option label="已处置" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="cardCode" label="资产编码" width="160" />
      <el-table-column prop="cardName" label="资产名称" />
      <el-table-column prop="categoryName" label="类别" width="120" />
      <el-table-column prop="originalValue" label="原值" align="right" />
      <el-table-column prop="netValue" label="净值" align="right" />
      <el-table-column prop="useStatus" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.useStatus === '1' ? 'success' : row.useStatus === '2' ? 'warning' : 'info'">
            {{ { '1': '在用', '2': '闲置', '3': '已处置' }[row.useStatus] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="deptName" label="使用部门" width="120" />
      <el-table-column label="操作" width="240">
        <template #default="{ row }">
          <el-button type="primary" link @click="onEdit(row)">编辑</el-button>
          <el-button type="warning" link @click="onDepreciate(row)">计提折旧</el-button>
          <el-button type="danger" link @click="onDisposal(row)" v-if="row.useStatus !== '3'">处置</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="700px">
      <el-form :model="form" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="资产编码"><el-input v-model="form.cardCode" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="资产名称"><el-input v-model="form.cardName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="类别"><el-input v-model="form.categoryName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="规格"><el-input v-model="form.spec" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="原值"><el-input-number v-model="form.originalValue" :precision="2" :min="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="残值率(%)"><el-input-number v-model="form.residualRate" :precision="2" :min="0" :max="100" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="使用年限(月)"><el-input-number v-model="form.useLifeMonth" :min="1" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="使用部门"><el-input v-model="form.deptName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="购入日期"><el-date-picker v-model="form.purchaseDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="使用状态">
            <el-select v-model="form.useStatus">
              <el-option label="在用" value="1" />
              <el-option label="闲置" value="2" />
            </el-select>
          </el-form-item></el-col>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const route = useRoute()
const pageTitle = computed(() => route.meta.title || '固定资产')

const loading = ref(false)
const data = reactive({ list: [] })
const query = reactive({ cardCode: '', cardName: '', useStatus: '' })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, cardCode: '', cardName: '', categoryName: '', spec: '', originalValue: 0, residualRate: 5, useLifeMonth: 60, deptName: '', purchaseDate: '', useStatus: '1' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/asset/card/page', method: 'get', params: { ...query, pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, cardCode: 'FA-' + Date.now() }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  if (form.id) await request({ url: '/asset/card', method: 'put', data: form })
  else await request({ url: '/asset/card', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDepreciate = async (row) => {
  await ElMessageBox.confirm(`对 [${row.cardName}] 计提本月折旧？`, '提示', { type: 'warning' })
  await request({ url: '/asset/depreciation/single', method: 'post', data: { cardId: row.id, fiscalYear: new Date().getFullYear().toString(), fiscalPeriod: new Date().getMonth() + 1 } })
  ElMessage.success('已计提')
  loadData()
}
const onDisposal = async (row) => {
  await ElMessageBox.confirm(`确认处置 [${row.cardName}]？`, '提示', { type: 'warning' })
  await request({ url: '/asset/disposal', method: 'post', data: { cardId: row.id, disposalType: '1', disposalDate: new Date().toISOString().substr(0, 10) } })
  ElMessage.success('已处置')
  loadData()
}

onMounted(loadData)
</script>
