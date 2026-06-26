<template>
  <div class="contract-attachment">
    <el-upload
      v-if="!fileInfo"
      drag
      :auto-upload="false"
      :show-file-list="false"
      :disabled="uploading"
      accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.wps,.et,.dps,.txt,.rtf,.odt,.ods,.odp"
      @change="onFileChange"
    >
      <el-icon class="upload-icon"><UploadFilled /></el-icon>
      <div class="el-upload__text">拖拽或点击上传 PDF、Word 等电子合同</div>
      <template #tip>
        <div class="el-upload__tip">支持 pdf / doc / docx / xls / xlsx / ppt / pptx 等，最大 50MB</div>
      </template>
    </el-upload>
    <div v-else class="file-row">
      <el-icon><Document /></el-icon>
      <span class="file-name">{{ fileInfo.fileName }}</span>
      <el-button link type="primary" @click="onPreview">预览</el-button>
      <el-button link type="primary" @click="onDownload">下载</el-button>
      <el-button link type="danger" @click="onRemove">移除</el-button>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Document, UploadFilled } from '@element-plus/icons-vue'
import { uploadFile as uploadFileApi, downloadFile, previewFile } from '@/api/file'
import { parseAttachment } from '@/utils/contractAttachment'

const props = defineProps({
  modelValue: { type: String, default: '' }
})
const emit = defineEmits(['update:modelValue'])

const uploading = ref(false)
const fileInfo = computed(() => parseAttachment(props.modelValue))

const onFileChange = async (upload) => {
  const raw = upload?.raw
  if (!raw) return
  uploading.value = true
  try {
    const res = await uploadFileApi(raw, 'contract')
    emit('update:modelValue', JSON.stringify(res.data))
    ElMessage.success('上传成功')
  } finally {
    uploading.value = false
  }
}

const onPreview = () => {
  if (fileInfo.value?.path) previewFile(fileInfo.value.path)
}

const onDownload = () => {
  if (fileInfo.value?.path) {
    downloadFile(fileInfo.value.path, fileInfo.value.fileName)
  }
}

const onRemove = () => emit('update:modelValue', '')
</script>

<style scoped>
.contract-attachment {
  width: 100%;
}
.upload-icon {
  font-size: 48px;
  color: var(--el-color-primary);
  margin-bottom: 8px;
}
.file-row {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border: 1px dashed var(--el-border-color);
  border-radius: 4px;
}
.file-name {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
