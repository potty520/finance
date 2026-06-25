<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">折旧管理</h2>
    <el-row :gutter="16">
      <el-col :span="8">
        <el-card>
          <h3>批量计提</h3>
          <el-form label-width="100px">
            <el-form-item label="年度"><el-input v-model="form.fiscalYear" /></el-form-item>
            <el-form-item label="期间"><el-input-number v-model="form.fiscalPeriod" :min="1" :max="12" /></el-form-item>
            <el-button type="primary" @click="onCalculate" :loading="loading">计提本月折旧</el-button>
          </el-form>
          <div v-if="result.totalAmount" style="margin-top: 16px;">
            <p>共计提：<strong>¥ {{ result.totalAmount }}</strong></p>
            <p>影响资产：<strong>{{ result.count }}</strong> 项</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <h3>折旧历史</h3>
          <el-table :data="data.list" border>
            <el-table-column prop="cardCode" label="资产编码" width="160" />
            <el-table-column prop="cardName" label="资产名称" />
            <el-table-column prop="fiscalYear" label="年度" width="80" />
            <el-table-column prop="fiscalPeriod" label="期间" width="80" />
            <el-table-column prop="depreciationAmount" label="折旧额" align="right" />
            <el-table-column prop="accumulatedDepreciation" label="累计折旧" align="right" />
            <el-table-column prop="netValue" label="净值" align="right" />
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
const result = reactive({ totalAmount: 0, count: 0 })
const data = reactive({ list: [] })

const onCalculate = async () => {
  await ElMessageBox.confirm('确认计提本月所有资产折旧？', '提示', { type: 'warning' })
  loading.value = true
  try {
    const res = await request({ url: '/asset/depreciation/calculate', method: 'post', data: form })
    result.totalAmount = res.data.totalDepreciation ?? res.data.totalAmount
    result.count = res.data.count
    ElMessage.success('计提完成')
    loadData()
  } finally { loading.value = false }
}

const loadData = async () => {
  const res = await request({ url: '/asset/depreciation/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
  data.list = res.data.records
}

onMounted(loadData)
</script>
