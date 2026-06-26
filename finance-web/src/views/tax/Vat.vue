<template>
  <div class="page-container" v-loading="loading">
    <h2 style="margin: 0 0 16px 0;">增值税申报底稿</h2>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="年度"><el-input v-model="query.fiscalYear" /></el-form-item>
        <el-form-item label="期间"><el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onQuery">查询</el-button>
          <el-button @click="onExportExcel">导出Excel</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 摘要卡片 -->
    <el-row :gutter="16" style="margin-bottom: 16px;">
      <el-col :span="4">
        <el-card shadow="hover"><template #header>销项税额</template>
          <div style="font-size:20px;font-weight:bold;color:#67C23A;">{{ fmt(data.outputTax) }}</div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover"><template #header>进项税额</template>
          <div style="font-size:20px;font-weight:bold;color:#E6A23C;">{{ fmt(data.inputTax) }}</div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover"><template #header>上期留抵</template>
          <div style="font-size:20px;font-weight:bold;color:#909399;">{{ fmt(data.prevCredit) }}</div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover"><template #header>应纳税额</template>
          <div style="font-size:20px;font-weight:bold;" :style="{color: data.finalTax > 0 ? '#F56C6C' : '#909399'}">{{ fmt(data.finalTax) }}</div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover"><template #header>附加税费</template>
          <div style="font-size:20px;font-weight:bold;color:#F56C6C;">{{ fmt(data.totalSurtax) }}</div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover"><template #header>合计应缴</template>
          <div style="font-size:22px;font-weight:bold;color:#F56C6C;">{{ fmt(data.totalPayable) }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 明细 -->
    <el-table :data="data.details" border stripe size="small">
      <el-table-column prop="name" label="项目" width="180" />
      <el-table-column prop="desc" label="说明" min-width="200" />
      <el-table-column label="计税依据" width="140" align="right">
        <template #default="{row}">{{ row.taxBase > 0 ? fmt(row.taxBase) : '' }}</template>
      </el-table-column>
      <el-table-column label="税额" width="140" align="right">
        <template #default="{row}">
          <span :style="{fontWeight: row.name.includes('合计') || row.name === '应纳税额' ? 'bold' : 'normal', color: row.amount < 0 ? '#F56C6C' : '#303133'}">
            {{ fmt(row.amount) }}
          </span>
        </template>
      </el-table-column>
</el-table>

    <!-- 公式说明 -->
    <el-alert type="info" :closable="false" style="margin-top:12px;">
      <template #title>
        计算公式：应纳税额 = 销项税额 - (进项税额 + 上期留抵 - 进项税额转出)
        &nbsp;|&nbsp; 若应纳税额 ≤ 0 则结转下期留抵
        &nbsp;|&nbsp; 附加税费 = 应纳税额 × 12%（城建7%+教育3%+地方2%）
      </template>
    </el-alert>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const query = reactive({ fiscalYear: '2026', fiscalPeriod: 6 })
const data = reactive({
  outputTax: 0, inputTax: 0, prevCredit: 0,
  totalDeductible: 0, finalTax: 0, finalCredit: 0,
  cityTax: 0, eduTax: 0, localEduTax: 0,
  totalSurtax: 0, totalPayable: 0,
  details: []
})

const onQuery = async () => {
  try {
    loading.value = true
    const res = await request({ url: '/tax/vat/draft', method: 'get', params: query })
    if (res.data) {
      Object.assign(data, res.data)
    } else {
      ElMessage.warning('未获取到增值税申报数据')
    }
  } catch (e) {
    console.error('增值税申报查询失败', e)
    ElMessage.error('查询失败: ' + (e.message || '请检查网络'))
  } finally {
    loading.value = false
  }
}

const fmt = (v) => {
  if (v == null || v === 0) return '0.00'
  return Number(v).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const onExportExcel = async () => {
  try {
    const token = localStorage.getItem('token')
    const resp = await fetch(`/api/tax/vat/draft/excel?fiscalYear=${query.fiscalYear}&fiscalPeriod=${query.fiscalPeriod}`, {
      headers: { 'Authorization': '***' + token }
    })
    const blob = await resp.blob()
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a'); a.href = url; a.download = `增值税申报底稿_${query.fiscalYear}_${query.fiscalPeriod}.xlsx`
    document.body.appendChild(a); a.click(); document.body.removeChild(a)
    URL.revokeObjectURL(url); ElMessage.success('导出成功')
  } catch(e) { ElMessage.error('导出失败') }
}

onQuery()
</script>
