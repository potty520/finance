<template>
  <div class="print-settings">
    <el-form :inline="true" label-width="72px" class="settings-form">
      <el-form-item label="纸张规格">
        <el-select v-model="local.preset" style="width: 200px" @change="onPresetChange">
          <el-option
            v-for="item in PAPER_PRESETS"
            :key="item.key"
            :label="item.label"
            :value="item.key"
          />
        </el-select>
      </el-form-item>

      <template v-if="local.preset === 'custom'">
        <el-form-item label="宽度">
          <el-input-number v-model="local.widthMm" :min="50" :max="600" :step="1" controls-position="right" />
          <span class="unit">mm</span>
        </el-form-item>
        <el-form-item label="高度">
          <el-input-number v-model="local.heightMm" :min="50" :max="600" :step="1" controls-position="right" />
          <span class="unit">mm</span>
        </el-form-item>
      </template>

      <el-form-item v-else label="尺寸">
        <span class="size-text">{{ presetSizeText }}</span>
      </el-form-item>

      <el-form-item label="页边距">
        <el-input-number v-model="local.marginTop" :min="0" :max="50" controls-position="right" style="width: 90px" />
        <span class="unit">mm</span>
        <el-tooltip content="上下左右统一边距，可在高级设置中分别调整" placement="top">
          <el-button link type="primary" @click="advancedVisible = !advancedVisible">
            {{ advancedVisible ? '收起' : '高级' }}
          </el-button>
        </el-tooltip>
      </el-form-item>

      <el-form-item label="缩放">
        <el-slider v-model="local.scale" :min="50" :max="150" :step="5" show-input style="width: 220px" />
      </el-form-item>
    </el-form>

    <el-form v-if="advancedVisible" :inline="true" label-width="72px" class="settings-form advanced">
      <el-form-item label="上边距"><el-input-number v-model="local.marginTop" :min="0" :max="50" controls-position="right" /><span class="unit">mm</span></el-form-item>
      <el-form-item label="下边距"><el-input-number v-model="local.marginBottom" :min="0" :max="50" controls-position="right" /><span class="unit">mm</span></el-form-item>
      <el-form-item label="左边距"><el-input-number v-model="local.marginLeft" :min="0" :max="50" controls-position="right" /><span class="unit">mm</span></el-form-item>
      <el-form-item label="右边距"><el-input-number v-model="local.marginRight" :min="0" :max="50" controls-position="right" /><span class="unit">mm</span></el-form-item>
    </el-form>

    <div class="settings-tip">
      当前纸张：{{ paperSizeText(local) }}，设置会自动保存，下次打印沿用
    </div>
  </div>
</template>

<script setup>
import { computed, reactive, ref, watch } from 'vue'
import {
  PAPER_PRESETS,
  DEFAULT_PAPER_OPTIONS,
  resolvePaperSize,
  paperSizeText,
  savePaperSettings
} from '@/utils/voucherPrint'

const props = defineProps({
  modelValue: { type: Object, default: () => ({ ...DEFAULT_PAPER_OPTIONS }) }
})

const emit = defineEmits(['update:modelValue'])

const advancedVisible = ref(false)
const local = reactive({ ...DEFAULT_PAPER_OPTIONS, ...props.modelValue })

watch(
  () => props.modelValue,
  (val) => Object.assign(local, DEFAULT_PAPER_OPTIONS, val || {}),
  { deep: true }
)

watch(
  local,
  (val) => {
    savePaperSettings(val)
    emit('update:modelValue', { ...val })
  },
  { deep: true }
)

const presetSizeText = computed(() => {
  const { width, height } = resolvePaperSize(local)
  return `${width} × ${height} mm`
})

const onPresetChange = (key) => {
  const preset = PAPER_PRESETS.find(p => p.key === key)
  if (preset && key !== 'custom') {
    local.widthMm = preset.width
    local.heightMm = preset.height
  }
}

watch(
  () => local.marginTop,
  (val) => {
    if (!advancedVisible.value) {
      local.marginRight = val
      local.marginBottom = val
      local.marginLeft = val
    }
  }
)
</script>

<style scoped lang="scss">
.print-settings {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 12px 16px 8px;
  margin-bottom: 12px;
}

.settings-form {
  margin-bottom: 0;

  :deep(.el-form-item) {
    margin-bottom: 10px;
    margin-right: 16px;
  }
}

.advanced {
  padding-top: 4px;
  border-top: 1px dashed #ebeef5;
}

.unit {
  margin-left: 4px;
  color: #909399;
  font-size: 12px;
}

.size-text {
  color: #606266;
  font-size: 13px;
}

.settings-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>
