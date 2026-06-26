<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">出入库管理</h2>
      <div>
        <el-button type="success" :icon="Plus" @click="onIn">入库</el-button>
        <el-button type="warning" :icon="Minus" @click="onOut">出库</el-button>
      </div>
    </div>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="类型">
          <el-select v-model="query.ioType" clearable style="width:140px">
            <el-option label="采购入库" value="1" />
            <el-option label="销售出库" value="2" />
            <el-option label="领料出库" value="3" />
            <el-option label="退料入库" value="4" />
            <el-option label="调拨" value="5" />
            <el-option label="盘点" value="6" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="billNo" label="单号" width="180" />
      <el-table-column prop="ioType" label="类型" width="100">
        <template #default="{ row }">
          <el-tag :type="['1','4'].includes(row.ioType) ? 'success' : 'warning'">
            {{ { '1': '采购入库', '2': '销售出库', '3': '领料出库', '4': '退料入库', '5': '调拨', '6': '盘点' }[row.ioType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="ioDate" label="日期" width="120" />
      <el-table-column prop="goodsName" label="物料" />
      <el-table-column prop="quantity" label="数量" align="right" />
      <el-table-column prop="price" label="单价" align="right" />
      <el-table-column prop="amount" label="金额" align="right" />
      <el-table-column prop="totalCost" label="总成本" align="right" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button type="danger" link @click="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="单号"><el-input v-model="form.billNo" disabled /></el-form-item>
        <el-form-item label="日期"><el-date-picker v-model="form.ioDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="物料">
          <el-select v-model="form.goodsId" filterable>
            <el-option v-for="g in goods" :key="g.id" :label="g.goodsName" :value="g.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库"><el-input v-model="form.warehouseName" /></el-form-item>
        <el-form-item label="数量"><el-input-number v-model="form.quantity" :precision="2" :min="0" /></el-form-item>
        <el-form-item label="单价" v-if="dialog.isIn"><el-input-number v-model="form.price" :precision="2" :min="0" /></el-form-item>
        <el-form-item label="金额" v-if="dialog.isIn"><el-input-number v-model="form.amount" :precision="2" :min="0" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item>
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
import { Plus, Minus } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { IO_TYPE_MAP2 } from '@/constants/enums'

const loading = ref(false)
const goods = ref([])
const data = reactive({ list: [] })
const query = reactive({ ioType: '' })
const dialog = reactive({ visible: false, title: '', isIn: true })
const form = reactive({ id: null, billNo: '', ioDate: '', ioType: '1', goodsId: null, warehouseName: '', quantity: 0, price: 0, amount: 0, remark: '' })

const loadGoods = async () => {
  const res = await request({ url: '/stock/goods/list', method: 'get' })
  goods.value = res.data
}
const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/stock/io/page', method: 'get', params: { ...query, pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}
const onIn = () => { Object.assign(form, { id: null, billNo: 'IN-' + Date.now(), ioDate: new Date().toISOString().substr(0, 10), ioType: '1' }); dialog.isIn = true; dialog.title = '入库'; dialog.visible = true }
const onOut = () => { Object.assign(form, { id: null, billNo: 'OUT-' + Date.now(), ioDate: new Date().toISOString().substr(0, 10), ioType: '2' }); dialog.isIn = false; dialog.title = '出库'; dialog.visible = true }
const onSave = async () => {
  const url = form.ioType === '2' ? '/stock/out' : '/stock/in'
  await request({ url, method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  ElMessage.info('演示环境，未对接删除接口')
  loadData()
}

onMounted(() => { loadGoods(); loadData() })
</script>
