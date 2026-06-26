<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">合并底稿</h2>
      <el-button type="success" :loading="generating" @click="onGenerate">生成底稿</el-button>
    </div>
    <el-row :gutter="16" style="margin-bottom:16px;">
      <el-col :span="6">
        <el-input v-model="query.fiscalYear" placeholder="年度" />
      </el-col>
      <el-col :span="6">
        <el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" style="width:100%" />
      </el-col>
      <el-col :span="6">
        <el-select v-model="query.reportCode" placeholder="报表类型" style="width:100%">
          <el-option label="资产负债表" value="BS" />
          <el-option label="利润表" value="IS" />
          <el-option label="现金流量表" value="CF" />
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="loadData">查询</el-button>
      </el-col>
    </el-row>

    <el-table :data="data.list" v-loading="loading" border show-summary :summary-method="getSummary">
      <el-table-column prop="companyName" label="公司" width="140" />
      <el-table-column prop="rowCode" label="行次" width="100" />
      <el-table-column prop="rowName" label="项目" min-width="180" />
      <el-table-column prop="offsetType" label="抵消类型" width="110">
        <template #default="{ row }">
          <el-tag v-if="row.offsetType" size="small">
            {{ { INV: '存货', AR: '往来', INCOME: '内部交易', DIV: '股利', FIXED: '固定资产' }[row.offsetType] || row.offsetType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="个别报表" align="right" width="120" />
      <el-table-column prop="adjAmount" label="调整数" align="right" width="120" />
      <el-table-column prop="elimAmount" label="抵消数" align="right" width="120" />
      <el-table-column prop="finalAmount" label="合并数" align="right" width="120" />
      <el-table-column prop="summary" label="摘要" min-width="160" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { OFFSET_TYPE_MAP } from '@/constants/enums'

const loading = ref(false)
const generating = ref(false)
const data = reactive({ list: [] })
const query = reactive({
  fiscalYear: new Date().getFullYear().toString(),
  fiscalPeriod: new Date().getMonth() + 1,
  reportCode: 'BS'
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({
      url: '/consol/analysis',
      method: 'get',
      params: {
        fiscalYear: query.fiscalYear,
        fiscalPeriod: query.fiscalPeriod,
        reportCode: query.reportCode
      }
    })
    data.list = res.data || []
  } finally {
    loading.value = false
  }
}

const onGenerate = async () => {
  await ElMessageBox.confirm('根据当前期间抵消分录生成合并底稿？', '提示', { type: 'warning' })
  generating.value = true
  try {
    await request({
      url: '/consol/generate',
      method: 'post',
      data: {
        fiscalYear: query.fiscalYear,
        fiscalPeriod: query.fiscalPeriod,
        reportCode: query.reportCode
      }
    })
    ElMessage.success('底稿已生成')
    loadData()
  } finally {
    generating.value = false
  }
}

const getSummary = ({ columns, data: rows }) => {
  const sums = []
  columns.forEach((col, index) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    if (['amount', 'adjAmount', 'elimAmount', 'finalAmount'].includes(col.property)) {
      sums[index] = rows.reduce((acc, row) => acc + Number(row[col.property] || 0), 0).toFixed(2)
    } else {
      sums[index] = ''
    }
  })
  return sums
}

onMounted(loadData)
</script>
