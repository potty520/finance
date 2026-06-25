<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">物料档案</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增物料</el-button>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="关键词"><el-input v-model="query.keyword" /></el-form-item>
        <el-form-item label="类别"><el-input v-model="query.categoryCode" /></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="goodsCode" label="物料编码" width="160" />
      <el-table-column prop="goodsName" label="物料名称" />
      <el-table-column prop="categoryName" label="类别" width="120" />
      <el-table-column prop="spec" label="规格" width="120" />
      <el-table-column prop="unit" label="单位" width="80" />
      <el-table-column prop="avgPrice" label="加权均价" align="right" />
      <el-table-column prop="lastPrice" label="最近单价" align="right" />
      <el-table-column prop="pricingMethod" label="计价方式" width="100">
        <template #default="{ row }">
          {{ { '1': '加权平均', '2': '先进先出', '3': '后进先出', '4': '标准价' }[row.pricingMethod] }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="700px">
      <el-form :model="form" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="物料编码"><el-input v-model="form.goodsCode" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="物料名称"><el-input v-model="form.goodsName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="类别"><el-input v-model="form.categoryName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="规格"><el-input v-model="form.spec" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="单位"><el-input v-model="form.unit" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="计价方式">
            <el-select v-model="form.pricingMethod">
              <el-option label="加权平均" value="1" />
              <el-option label="先进先出" value="2" />
              <el-option label="后进先出" value="3" />
              <el-option label="标准价" value="4" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="库存科目"><el-input v-model="form.subjectCode" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="销售科目"><el-input v-model="form.salesSubjectCode" /></el-form-item></el-col>
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

const loading = ref(false)
const data = reactive({ list: [] })
const query = reactive({ keyword: '', categoryCode: '' })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, goodsCode: '', goodsName: '', categoryName: '', spec: '', unit: '', pricingMethod: '1', subjectCode: '', salesSubjectCode: '', status: 1 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/stock/goods/page', method: 'get', params: { ...query, pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, goodsCode: 'GD-' + Date.now() }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  if (form.id) await request({ url: '/stock/goods', method: 'put', data: form })
  else await request({ url: '/stock/goods', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/stock/goods/${row.id}`, method: 'delete' })
  loadData()
}

onMounted(loadData)
</script>
