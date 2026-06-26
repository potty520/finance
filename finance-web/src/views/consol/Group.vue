<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">集团架构</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增公司</el-button>
    </div>
    <el-table :data="data.list" v-loading="loading" border default-expand-all row-key="id" :tree-props="{ children: 'children' }">
      <el-table-column prop="groupCode" label="编码" width="160" />
      <el-table-column prop="groupName" label="名称" />
      <el-table-column label="类型" width="100">
        <template #default="{ row }">
          <el-tag size="small">{{ { 'HQ': '总部', 'SUB': '子公司', 'BR': '分公司' }[row.groupType] || row.groupType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="ownershipRatio" label="持股比例" width="120">
        <template #default="{ row }">{{ row.ownershipRatio }}%</template>
      </el-table-column>
      <el-table-column prop="currencyCode" label="本位币" width="100" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="onDel(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="编码"><el-input v-model="form.groupCode" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="form.groupName" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.groupType" style="width:100%" placeholder="请选择">
            <el-option label="总部" value="HQ" />
            <el-option label="子公司" value="SUB" />
            <el-option label="分公司" value="BR" />
          </el-select>
        </el-form-item>
        <el-form-item label="持股比例(%)"><el-input-number v-model="form.ownershipRatio" :min="0" :max="100" /></el-form-item>
        <el-form-item label="本位币"><el-input v-model="form.currencyCode" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { GROUP_TYPE_MAP, STATUS_MAP } from '@/constants/enums'

const loading = ref(false)
const data = reactive({ list: [] })
const dialog = reactive({ visible: false, title: '' })
const form = reactive({ id: null, groupCode: '', groupName: '', groupType: 'SUB', ownershipRatio: 100, currencyCode: 'CNY', status: 1 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request({ url: '/consol/group/list', method: 'get' })
    data.list = res.data
  } finally { loading.value = false }
}
const onAdd = () => { Object.assign(form, { id: null, groupCode: 'G-' + Date.now() }); dialog.visible = true; dialog.title = '新增' }
const onEdit = (row) => { Object.assign(form, row); dialog.visible = true; dialog.title = '编辑' }
const onSave = async () => {
  if (form.id) await request({ url: '/consol/group', method: 'put', data: form })
  else await request({ url: '/consol/group', method: 'post', data: form })
  ElMessage.success('保存成功'); dialog.visible = false; loadData()
}
const onDel = async (row) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request({ url: `/consol/group/${row.id}`, method: 'delete' })
  loadData()
}

onMounted(loadData)
</script>
