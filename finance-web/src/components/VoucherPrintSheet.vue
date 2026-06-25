<template>
  <div class="voucher-preview-wrap" :style="previewFrameStyle">
    <div class="paper-label">{{ paperLabel }}</div>
    <div class="voucher-print-sheet">
    <div class="voucher-company">{{ companyName }}</div>
    <h1 class="voucher-title">记 账 凭 证</h1>
    <div class="voucher-meta">
      <div class="meta-left">
        <span>凭证号：{{ voucher?.voucherNo || '-' }}</span>
        <span>日期：{{ voucher?.voucherDate || '-' }}</span>
        <span>期间：{{ periodText }}</span>
      </div>
      <div class="meta-right">
        <span>附单据：{{ voucher?.attachCount ?? 0 }} 张</span>
        <span>状态：{{ statusText }}</span>
      </div>
    </div>

    <table class="voucher-table">
      <thead>
        <tr>
          <th style="width:5%">序号</th>
          <th style="width:22%">摘要</th>
          <th style="width:12%">科目编码</th>
          <th style="width:22%">科目名称</th>
          <th style="width:19%">借方金额</th>
          <th style="width:20%">贷方金额</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="row in displayEntries" :key="row.entryNo">
          <td class="center">{{ row.entryNo }}</td>
          <td>{{ row.summary }}</td>
          <td class="center">{{ row.subjectCode }}</td>
          <td>{{ row.subjectName }}</td>
          <td class="num">{{ row.debitAmount ? formatMoney(row.debitAmount) : '' }}</td>
          <td class="num">{{ row.creditAmount ? formatMoney(row.creditAmount) : '' }}</td>
        </tr>
        <tr class="total-row">
          <td colspan="4" class="center">合　计</td>
          <td class="num">{{ formatMoney(voucher?.totalDebit) }}</td>
          <td class="num">{{ formatMoney(voucher?.totalCredit) }}</td>
        </tr>
      </tbody>
    </table>

    <div class="total-cn">金额大写：{{ amountCn }}</div>
    <div class="voucher-footer">
      <span>制单：{{ voucher?.createByName || voucher?.creatorName || '-' }}</span>
      <span>审核：{{ voucher?.auditByName || voucher?.auditorName || '-' }}</span>
      <span>记账：{{ voucher?.postBy ? (voucher?.posterName || '已记账') : '-' }}</span>
      <span>出纳：-</span>
    </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import {
  formatMoney,
  parsePeriod,
  statusLabel,
  amountToChinese,
  padEntries,
  resolvePaperSize,
  paperSizeText,
  DEFAULT_PAPER_OPTIONS
} from '@/utils/voucherPrint'

const props = defineProps({
  voucher: { type: Object, default: () => ({}) },
  companyName: { type: String, default: '财务管理系统' },
  paperOptions: { type: Object, default: () => ({ ...DEFAULT_PAPER_OPTIONS }) }
})

const displayEntries = computed(() => padEntries(props.voucher?.entries))
const periodText = computed(() => {
  const { fiscalYear, fiscalPeriod } = parsePeriod(props.voucher)
  return `${fiscalYear}年 第${fiscalPeriod}期`
})
const statusText = computed(() => statusLabel(props.voucher?.status))
const amountCn = computed(() => amountToChinese(props.voucher?.totalDebit))
const paperLabel = computed(() => paperSizeText(props.paperOptions))
const previewFrameStyle = computed(() => {
  const { width, height } = resolvePaperSize(props.paperOptions)
  return {
    maxWidth: '100%',
    aspectRatio: `${width} / ${height}`
  }
})
</script>

<style scoped lang="scss">
.voucher-preview-wrap {
  position: relative;
  width: 100%;
  max-width: 860px;
  margin: 0 auto;
  background: #e8e8e8;
  border: 1px dashed #909399;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.paper-label {
  position: absolute;
  top: 6px;
  right: 8px;
  z-index: 1;
  font-size: 11px;
  color: #909399;
  background: rgba(255, 255, 255, 0.85);
  padding: 2px 6px;
  border-radius: 2px;
}

.voucher-print-sheet {
  position: absolute;
  inset: 0;
  overflow: auto;
  padding: 8px 12px 16px;
  background: #fff;
  color: #000;
  font-family: 'SimSun', 'Songti SC', 'Microsoft YaHei', serif;
}

.voucher-company {
  text-align: center;
  font-size: 14px;
  margin-bottom: 8px;
}

.voucher-title {
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  letter-spacing: 8px;
  margin: 0 0 12px;
}

.voucher-meta {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  margin-bottom: 10px;
  gap: 16px;
  flex-wrap: wrap;

  span {
    margin-right: 20px;
  }
}

.voucher-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 12px;

  th,
  td {
    border: 1px solid #000;
    padding: 7px 5px;
    word-break: break-all;
  }

  th {
    text-align: center;
    font-weight: bold;
    background: #fafafa;
  }

  .center {
    text-align: center;
  }

  .num {
    text-align: right;
    font-family: 'Courier New', monospace;
  }

  .total-row td {
    font-weight: bold;
  }
}

.total-cn {
  margin-top: 10px;
  font-size: 13px;
}

.voucher-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  font-size: 13px;

  span {
    min-width: 18%;
  }
}
</style>
