<template>
  <div class="page-container">
      <h2 style="margin:0;">成本计算</h2>
    <el-row :gutter="16">
      <el-col :span="8">
        <el-card>
          <h3>计算</h3>
          <el-form label-width="100px">
            <el-form-item label="年度"><el-input v-model="form.fiscalYear" /></el-form-item>
            <el-form-item label="期间"><el-input-number v-model="form.fiscalPeriod" :min="1" :max="12" /></el-form-item>
            <el-button type="primary" @click="onCalculate" :loading="loading">开始计算</el-button>
          </el-form>
          <div v-if="result.totalCost" style="margin-top:16px;">
            <p>总成本：<strong>¥ {{ result.totalCost }}</strong></p>
            <p>总产量：<strong>{{ result.totalQuantity }}</strong></p>
            <p>产品数：<strong>{{ result.count }}</strong></p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <h3>产品成本汇总</h3>
          <el-table :data="data.list" border>
            <el-table-column prop="productCode" label="产品编码" width="160" />
            <el-table-column prop="productName" label="产品名称" />
            <el-table-column prop="outputQuantity" label="产量" align="right" />
            <el-table-column prop="directMaterial" label="直接材料" align="right" />
            <el-table-column prop="directLabor" label="直接人工" align="right" />
            <el-table-column prop="totalCost" label="总成本" align="right" />
            <el-table-column prop="unitCost" label="单位成本" align="right" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const form = reactive({ fiscalYear: new Date().getFullYear().toString(), fiscalPeriod: new Date().getMonth() + 1 })
const result = reactive({ totalCost: 0, totalQuantity: 0, count: 0 })
const data = reactive({ list: [] })

const onCalculate = async () => {
  await ElMessageBox.confirm('将按品种法重新计算产品成本，是否继续？', '提示', { type: 'warning' })
  loading.value = true
  try {
    const res = await request({ url: '/cost/product/calculate', method: 'post', data: form })
    Object.assign(result, res.data)
    ElMessage.success('计算完成')
    loadData()
  } finally { loading.value = false }
}

const loadData = async () => {
  const res = await request({ url: '/cost/product/summary', method: 'get', params: form })
  data.list = res.data
}

onMounted(loadData)
</script>
