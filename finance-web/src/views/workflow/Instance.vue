<template>
  <div class="page-container">
    <h2 style="margin: 0 0 16px 0;">审批记录</h2>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="flowCode" label="流程号" width="180" />
      <el-table-column prop="flowName" label="流程名称" width="160" />
      <el-table-column prop="businessType" label="业务类型" width="120" />
      <el-table-column prop="businessNo" label="业务单号" width="180" />
      <el-table-column prop="initiatorName" label="发起人" width="120" />
      <el-table-column prop="currentNode" label="当前节点" width="160" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="{ '0': 'warning', '1': 'success', '2': 'danger' }[row.status]">
            {{ { '0': '审批中', '1': '已通过', '2': '已驳回' }[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发起时间" width="180" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button type="primary" link @click="onDetail(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="detailDialog.visible" title="流程详情" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="流程号">{{ detail.instance?.flowCode }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="{ '0': 'warning', '1': 'success', '2': 'danger' }[detail.instance?.status]">
            {{ { '0': '审批中', '1': '已通过', '2': '已驳回' }[detail.instance?.status] }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="发起人">{{ detail.instance?.initiatorName }}</el-descriptions-item>
        <el-descriptions-item label="发起时间">{{ detail.instance?.createTime }}</el-descriptions-item>
        <el-descriptions-item label="当前节点" :span="2">{{ detail.instance?.currentNode }}</el-descriptions-item>
      </el-descriptions>
      <h4 style="margin: 16px 0 8px;">审批节点</h4>
      <el-timeline>
        <el-timeline-item v-for="t in detail.tasks" :key="t.id" :type="t.status === '1' ? 'success' : t.status === '2' ? 'danger' : 'primary'" :timestamp="t.handleTime || t.createTime">
          <strong>{{ t.nodeName }}</strong> ({{ t.assigneeName }})<br />
          <span v-if="t.status === '0'">待审批</span>
          <span v-else-if="t.status === '1'">通过 - {{ t.opinion }}</span>
          <span v-else-if="t.status === '2'">驳回 - {{ t.opinion }}</span>
          <span v-else>转交 - {{ t.opinion }}</span>
        </el-timeline-item>
      </el-timeline>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { BUSINESS_TYPE_MAP, STATUS_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })
const detailDialog = reactive({ visible: false })
const detail = reactive({ instance: null, tasks: [] })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/workflow/instance/page', method: 'get', params: { pageNum: 1, pageSize: 50 } })
    data.list = res.data.records
  } finally { loading.value = false }
}

const onDetail = async (row) => {
  const res = await request({ url: `/workflow/detail/${row.id}`, method: 'get' })
  detail.instance = res.data.instance
  detail.tasks = res.data.tasks
  detailDialog.visible = true
}

onMounted(loadData)
</script>
