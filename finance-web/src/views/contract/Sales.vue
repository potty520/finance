<template>

  <div class="page-container">

    <div class="page-header">

      <h2 style="margin:0;">销售合同</h2>

      <div class="header-actions">

        <el-button type="success" :icon="Upload" @click="openImport">导入电子合同</el-button>

        <el-button type="primary" :icon="Plus" @click="onAdd">新增合同</el-button>

      </div>

    </div>

    <el-table :data="data.list" v-loading="loading" border>

      <el-table-column prop="contractNo" label="合同号" width="180" />

      <el-table-column prop="contractName" label="合同名称" />

      <el-table-column prop="partyName" label="客户" width="160" />

      <el-table-column prop="amount" label="金额" align="right" />

      <el-table-column prop="signDate" label="签订日期" width="120" />

      <el-table-column label="电子合同" width="120" align="center">
        <template #default="{ row }">
          <template v-if="hasAttachment(row.attachment)">
            <el-button type="warning" link size="small" @click="onPreviewAttachment(row)">预览</el-button>
            <el-button type="success" link size="small" @click="onDownloadAttachment(row)">下载</el-button>
          </template>

          <span v-else class="text-muted">—</span>

        </template>

      </el-table-column>

      <el-table-column prop="status" label="状态" width="100">

        <template #default="{ row }">

          <el-tag :type="statusTagType(row.status)">{{ statusText(row.status) }}</el-tag>

        </template>

      </el-table-column>

      <el-table-column label="操作" width="140" fixed="right">

        <template #default="{ row }">

          <el-button link class="action-edit" @click="onEdit(row)">编辑</el-button>

          <el-button type="danger" link @click="onTerminate(row)" v-if="row.status === '1'">终止</el-button>

        </template>

      </el-table-column>

    </el-table>



    <el-dialog v-model="dialog.visible" :title="dialog.title" width="700px" destroy-on-close>

      <el-form :model="form" label-width="100px">

        <el-row :gutter="16">

          <el-col :span="12">

            <el-form-item label="合同号">

              <el-input v-model="form.contractNo" placeholder="留空则自动生成" />

            </el-form-item>

          </el-col>

          <el-col :span="12">

            <el-form-item label="合同名称">

              <el-input v-model="form.contractName" />

            </el-form-item>

          </el-col>

          <el-col :span="12">

            <el-form-item label="客户">

              <el-input v-model="form.partyName" />

            </el-form-item>

          </el-col>

          <el-col :span="12">

            <el-form-item label="金额">

              <el-input-number v-model="form.amount" :precision="2" :min="0" style="width:100%" />

            </el-form-item>

          </el-col>

          <el-col :span="12">

            <el-form-item label="签订日">

              <el-date-picker v-model="form.signDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />

            </el-form-item>

          </el-col>

          <el-col :span="12">

            <el-form-item label="起始日">

              <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />

            </el-form-item>

          </el-col>

          <el-col :span="12">

            <el-form-item label="到期日">

              <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />

            </el-form-item>

          </el-col>

          <el-col :span="24">

            <el-form-item label="电子合同">

              <ContractAttachmentUpload v-model="form.attachment" />

            </el-form-item>

          </el-col>

          <el-col :span="24">

            <el-form-item label="备注">

              <el-input v-model="form.remark" type="textarea" :rows="3" />

            </el-form-item>

          </el-col>

        </el-row>

      </el-form>

      <template #footer>

        <el-button @click="dialog.visible = false">取消</el-button>

        <el-button type="primary" @click="onSave">保存</el-button>

      </template>

    </el-dialog>



    <el-dialog v-model="importDialog.visible" title="导入电子合同" width="520px" destroy-on-close>

      <el-form label-width="90px">

        <el-form-item label="合同名称">

          <el-input v-model="importDialog.contractName" placeholder="可选，默认取文件名" />

        </el-form-item>

        <el-form-item label="选择文件">

          <el-upload

            drag

            :auto-upload="false"

            :limit="1"

            :file-list="importDialog.fileList"

            accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.wps,.txt,.rtf"

            @change="onImportFileChange"

            @remove="onImportFileRemove"

          >

            <el-icon class="upload-icon"><UploadFilled /></el-icon>

            <div class="el-upload__text">拖拽或点击选择 PDF、Word 等文档</div>

          </el-upload>

        </el-form-item>

      </el-form>

      <template #footer>

        <el-button @click="importDialog.visible = false">取消</el-button>

        <el-button type="primary" :loading="importDialog.loading" @click="onImportSubmit">导入</el-button>

      </template>

    </el-dialog>

  </div>

</template>



<script setup>

import { ref, reactive, onMounted } from 'vue'

import { ElMessage, ElMessageBox } from 'element-plus'

import { Plus, Upload, UploadFilled } from '@element-plus/icons-vue'

import request from '@/utils/request'
import { CONTRACT_TYPE_MAP, STATUS_MAP } from '@/constants/enums'

import { importContract, downloadFile, previewFile } from '@/api/file'

import { hasAttachment, parseAttachment } from '@/utils/contractAttachment'

import ContractAttachmentUpload from '@/components/ContractAttachmentUpload.vue'



const CONTRACT_TYPE = 'SELL'

const loading = ref(false)

const data = reactive({ list: [] })

const dialog = reactive({ visible: false, title: '新增销售合同' })

const importDialog = reactive({

  visible: false,

  loading: false,

  contractName: '',

  file: null,

  fileList: []

})

const defaultForm = () => ({

  id: null,

  contractNo: '',

  contractName: '',

  contractType: CONTRACT_TYPE,

  partyName: '',

  amount: 0,

  signDate: new Date().toISOString().slice(0, 10),

  startDate: '',

  endDate: '',

  remark: '',

  attachment: '',

  ownerId: 1,

  ownerName: '当前用户',

  status: '0'

})

const form = reactive(defaultForm())



const statusText = (status) => ({

  '0': '待审', '1': '生效', '2': '终止', '3': '完成',

  DRAFT: '待审', SIGNED: '生效', EXECUTING: '生效', CANCEL: '终止', COMPLETED: '完成'

}[status] || status)



const statusTagType = (status) => ({

  '0': 'warning', '1': 'success', '2': 'info', '3': 'primary',

  DRAFT: 'warning', SIGNED: 'success', EXECUTING: 'success', CANCEL: 'info', COMPLETED: 'primary'

}[status] || 'info')



const loadData = async () => {

  loading.value = true

  try {

    const res = await request({

      url: '/contract/page',

      method: 'get',

      params: { contractType: CONTRACT_TYPE, pageNum: 1, pageSize: 50 }

    })

    data.list = res.data.records || []

  } finally {

    loading.value = false

  }

}



const onAdd = () => {

  Object.assign(form, defaultForm())

  dialog.title = '新增销售合同'

  dialog.visible = true

}



const onEdit = (row) => {

  Object.assign(form, defaultForm(), row, { contractType: CONTRACT_TYPE })

  dialog.title = '编辑销售合同'

  dialog.visible = true

}



const onSave = async () => {

  if (!form.contractName?.trim()) {

    ElMessage.warning('请输入合同名称')

    return

  }

  if (!form.signDate) {

    ElMessage.warning('请选择签订日期')

    return

  }

  await request({ url: '/contract', method: 'post', data: { ...form, contractType: CONTRACT_TYPE } })

  ElMessage.success('保存成功')

  dialog.visible = false

  loadData()

}



const onTerminate = async (row) => {

  await ElMessageBox.confirm('确认终止合同？', '提示', { type: 'warning' })

  await request({

    url: `/contract/terminate/${row.id}`,

    method: 'post',

    params: { contractType: CONTRACT_TYPE }

  })

  ElMessage.success('已终止')

  loadData()

}



const openImport = () => {

  importDialog.contractName = ''

  importDialog.file = null

  importDialog.fileList = []

  importDialog.visible = true

}



const onImportFileChange = (upload) => {

  importDialog.file = upload?.raw || null

  importDialog.fileList = upload?.raw ? [upload] : []

}



const onImportFileRemove = () => {

  importDialog.file = null

  importDialog.fileList = []

}



const onImportSubmit = async () => {

  if (!importDialog.file) {

    ElMessage.warning('请选择要导入的文件')

    return

  }

  importDialog.loading = true

  try {

    await importContract(importDialog.file, CONTRACT_TYPE, importDialog.contractName || undefined)

    ElMessage.success('导入成功')

    importDialog.visible = false

    loadData()

  } finally {

    importDialog.loading = false

  }

}



const onPreviewAttachment = (row) => {

  const info = parseAttachment(row.attachment)

  if (info?.path) previewFile(info.path)

}



const onDownloadAttachment = (row) => {

  const info = parseAttachment(row.attachment)

  if (info?.path) downloadFile(info.path, info.fileName)

}



onMounted(loadData)

</script>



<style scoped>

.page-header {

  display: flex;

  align-items: center;

  justify-content: space-between;

  margin-bottom: 16px;

}

.header-actions {

  display: flex;

  gap: 8px;

}

.action-edit {

  color: #d48806 !important;

  font-weight: 500;

}

.text-muted {

  color: #999;

}

.upload-icon {

  font-size: 48px;

  color: var(--el-color-primary);

  margin-bottom: 8px;

}

</style>

