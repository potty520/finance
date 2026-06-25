<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">预算分析</h2>
    <div class="search-bar">
      <el-form :inline="true">
        <el-form-item label="年度"><el-input v-model="query.fiscalYear" /></el-form-item>
        <el-form-item label="期间"><el-input-number v-model="query.fiscalPeriod" :min="1" :max="12" /></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
    </div>
    <v-chart :option="chartOption" autoresize style="height:400px;" />
    <el-table :data="data.list" border style="margin-top:16px;">
      <el-table-column prop="subjectName" label="科目" />
      <el-table-column prop="amount" label="预算金额" align="right" />
      <el-table-column prop="usedAmount" label="已用" align="right" />
      <el-table-column prop="availableAmount" label="可用" align="right" />
      <el-table-column label="执行率" width="120">
        <template #default="{ row }">
          <el-progress :percentage="row.amount > 0 ? Math.round(row.usedAmount * 100 / row.amount) : 0" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'
import request from '@/utils/request'

use([CanvasRenderer, BarChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent])

const data = reactive({ list: [] })
const query = reactive({ fiscalYear: new Date().getFullYear().toString(), fiscalPeriod: new Date().getMonth() + 1 })

const loadData = async () => {
  const res = await request({ url: '/budget/analysis', method: 'get', params: query })
  data.list = res.data
}

const chartOption = computed(() => ({
  title: { text: '预算执行对比', left: 'center' },
  tooltip: { trigger: 'axis' },
  legend: { data: ['预算金额', '已用金额'], top: 30 },
  grid: { top: 80, left: 60, right: 60, bottom: 60 },
  xAxis: { type: 'category', data: data.list.map(d => d.subjectName) },
  yAxis: { type: 'value' },
  series: [
    { name: '预算金额', type: 'bar', data: data.list.map(d => d.amount) },
    { name: '已用金额', type: 'bar', data: data.list.map(d => d.usedAmount) }
  ]
}))

onMounted(loadData)
</script>
