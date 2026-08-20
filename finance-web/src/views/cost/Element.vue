<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">成本要素</h2>
      <div class="header-actions">
        <el-input v-model="keyword" placeholder="编码/名称" clearable style="width:200px" @keyup.enter="loadData" @clear="loadData" />
        <el-button type="primary" :icon="Plus" @click="onAdd">新增</el-button>
      </div>
    </div>

    <el-table :data="filtered" v-loading="loading" border stripe>
      <el-table-column prop="elementCode" label="要素编码" width="140" />
      <el-table-column prop="elementName" label="要素名称" min-width="150" />
      <el-table-column prop="elementType" label="要素类型" width="120">
        <template #default="{ row }">{{ TYPE_MAP[row.elementType] || row.elementType || '-' }}</template>
      </el-table-column>
      <el-table-column prop="subjectCode" label="对应科目" width="110" />
      <el-table-column prop="itemCode" label="项目编码" width="120" />
      <el-table-column prop="itemName" label="项目名称" min-width="120" />
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="onEdit(row)">编辑</el-button>
          <el-button link type="danger" size="small" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="560px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="要素编码" prop="elementCode">
          <el-input v-model="form.elementCode" placeholder="如 DM-01" />
        </el-form-item>
        <el-form-item label="要素名称" prop="elementName">
          <el-input v-model="form.elementName" />
        </el-form-item>
        <el-form-item label="要素类型">
          <el-select v-model="form.elementType" style="width:100%">
            <el-option v-for="(label, key) in TYPE_MAP" :key="key" :label="label" :value="key" />
          </el-select>
        </el-form-item>
        <el-form-item label="对应科目">
          <el-input v-model="form.subjectCode" placeholder="如 5001" />
        </el-form-item>
        <el-form-item label="项目编码">
          <el-input v-model="form.itemCode" />
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input v-model="form.itemName" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const TYPE_MAP = { DIRECT_MATERIAL: '直接材料', DIRECT_LABOR: '直接人工', MANUFACTURE: '制造费用', OTHER: '其他' }

const loading = ref(false)
const saving = ref(false)
const formRef = ref()
const keyword = ref('')
const data = reactive({ list: [] })
const dialog = reactive({ visible: false, title: '新增成本要素' })

const emptyForm = () => ({
  id: null, elementCode: '', elementName: '', elementType: 'DIRECT_MATERIAL',
  subjectCode: '', status: 1, itemCode: '', itemName: '', itemType: '', remark: ''
})
const form = reactive(emptyForm())

const rules = {
  elementCode: [{ required: true, message: '请输入要素编码', trigger: 'blur' }],
  elementName: [{ required: true, message: '请输入要素名称', trigger: 'blur' }]
}

const filtered = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return data.list
  return data.list.filter(x =>
    (x.elementCode || '').toLowerCase().includes(kw) || (x.elementName || '').toLowerCase().includes(kw))
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/cost/item/list', method: 'get' })
    data.list = res.data || []
  } finally { loading.value = false }
}

const onAdd = () => {
  Object.assign(form, emptyForm())
  dialog.title = '新增成本要素'
  dialog.visible = true
}
const onEdit = (row) => {
  Object.assign(form, emptyForm(), JSON.parse(JSON.stringify(row)))
  dialog.title = '编辑成本要素'
  dialog.visible = true
}
const onDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除成本要素「${row.elementName}」吗？`, '提示', { type: 'warning' })
  await request({ url: `/cost/item/${row.id}`, method: 'delete' })
  ElMessage.success('删除成功')
  loadData()
}
const onSave = async () => {
  await formRef.value.validate()
  saving.value = true
  try {
    await request({ url: '/cost/item', method: form.id ? 'put' : 'post', data: form })
    ElMessage.success('保存成功')
    dialog.visible = false
    loadData()
  } finally { saving.value = false }
}

onMounted(loadData)
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.header-actions { display: flex; gap: 10px; }
</style>
