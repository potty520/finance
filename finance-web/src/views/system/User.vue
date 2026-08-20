<template>
  <div class="page-container">
    <div class="page-header">
      <h2 style="margin:0;">用户管理</h2>
      <el-button type="primary" :icon="Plus" @click="onAdd">新增用户</el-button>
    </div>
    <div class="search-bar">
      <el-form :inline="true" @submit.prevent>
        <el-form-item label="用户名/姓名">
          <el-input v-model="query.keyword" clearable placeholder="模糊搜索" style="width:200px" @keyup.enter="onSearch" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable placeholder="全部" style="width:120px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSearch">查询</el-button>
          <el-button @click="onReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="data.list" v-loading="loading" border>
      <el-table-column prop="username" label="用户名" width="130" />
      <el-table-column prop="realName" label="姓名" width="110" />
      <el-table-column prop="deptName" label="部门" width="150" />
      <el-table-column prop="phone" label="手机" width="130" />
      <el-table-column prop="email" label="邮箱" min-width="160" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="240" fixed="right">
        <template #default="{ row }">
          <el-button type="warning" link @click="onEdit(row)">编辑</el-button>
          <el-button link @click="onResetPwd(row)">重置密码</el-button>
          <el-button :type="row.status === 1 ? 'info' : 'success'" link @click="onToggleStatus(row)">
            {{ row.status === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button type="danger" link @click="onDel(row)" v-if="row.id !== 1">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pager.pageNum"
      v-model:page-size="pager.pageSize"
      :total="data.total"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50, 100]"
      @current-change="loadData"
      @size-change="loadData"
      style="margin-top:16px; justify-content: flex-end;"
    />

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="600px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="!!form.id" placeholder="登录账号" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="真实姓名" />
        </el-form-item>
        <el-form-item label="部门">
          <el-tree-select
            v-model="form.deptId"
            :data="deptTree"
            :props="{ label: 'deptName', value: 'id', children: 'children' }"
            check-strictly
            clearable
            filterable
            placeholder="选择部门"
            style="width:100%"
          />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.roleIds" multiple clearable placeholder="分配角色" style="width:100%">
            <el-option v-for="r in roles" :key="r.id" :label="r.roleName" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="form.phone" placeholder="联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="邮箱（可选）" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
            <el-radio :label="0">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
        </el-form-item>
        <el-form-item label="初始密码" prop="password" v-if="!form.id">
          <el-input v-model="form.password" type="password" show-password placeholder="默认 123456" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="pwdDialog.visible" title="重置密码" width="420px" destroy-on-close>
      <el-form label-width="100px">
        <el-form-item label="用户">
          <b>{{ pwdDialog.realName || pwdDialog.username }}</b>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdDialog.password" type="password" show-password placeholder="请输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="onSavePwd">确认重置</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const data = reactive({ list: [], total: 0 })
const pager = reactive({ pageNum: 1, pageSize: 10 })
const query = reactive({ keyword: '', status: null })
const dialog = reactive({ visible: false, title: '' })
const pwdDialog = reactive({ visible: false, id: null, username: '', realName: '', password: '' })
const deptTree = ref([])
const roles = ref([])
const formRef = ref()
const form = reactive({ id: null, username: '', realName: '', deptId: null, roleIds: [], phone: '', email: '', gender: 0, status: 1, password: '123456' })

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 30, message: '长度 3-30 个字符', trigger: 'blur' }
  ],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const params = { pageNum: pager.pageNum, pageSize: pager.pageSize }
    if (query.keyword) {
      params.username = query.keyword
      params.realName = query.keyword
    }
    if (query.status !== null && query.status !== '') params.status = query.status
    const res = await request({ url: '/system/user/page', method: 'get', params })
    data.list = res.data.records
    data.total = res.data.total || 0
  } finally { loading.value = false }
}

const loadDeptTree = async () => {
  try {
    const res = await request({ url: '/system/dept/tree', method: 'get' })
    deptTree.value = res.data || []
  } catch (e) { /* ignore */ }
}

const loadRoles = async () => {
  try {
    const res = await request({ url: '/system/role/all', method: 'get' })
    roles.value = res.data || []
  } catch (e) { /* ignore */ }
}

const onSearch = () => { pager.pageNum = 1; loadData() }
const onReset = () => { query.keyword = ''; query.status = null; pager.pageNum = 1; loadData() }

const onAdd = () => {
  Object.assign(form, { id: null, username: '', realName: '', deptId: null, roleIds: [], phone: '', email: '', gender: 0, status: 1, password: '123456' })
  dialog.visible = true
  dialog.title = '新增用户'
}

const onEdit = async (row) => {
  Object.assign(form, { id: row.id, username: row.username, realName: row.realName, deptId: row.deptId, phone: row.phone || '', email: row.email || '', gender: row.gender ?? 0, status: row.status ?? 1, password: '' })
  dialog.visible = true
  dialog.title = '编辑用户'
  // 回显角色
  form.roleIds = []
  try {
    const res = await request({ url: `/system/user/${row.id}/roles`, method: 'get' })
    form.roleIds = res.data || []
  } catch (e) { /* ignore */ }
}

const onSave = async () => {
  await formRef.value.validate()
  const payload = {
    id: form.id, username: form.username, realName: form.realName, deptId: form.deptId,
    roleIds: form.roleIds, phone: form.phone, email: form.email, gender: form.gender, status: form.status
  }
  if (!form.id) payload.password = form.password || '123456'
  if (form.id) await request({ url: '/system/user', method: 'put', data: payload })
  else await request({ url: '/system/user', method: 'post', data: payload })
  ElMessage.success('保存成功')
  dialog.visible = false
  loadData()
}

const onDel = async (row) => {
  await ElMessageBox.confirm(`确认删除用户 [${row.username}]？`, '提示', { type: 'warning' })
  await request({ url: `/system/user/${row.id}`, method: 'delete' })
  ElMessage.success('已删除')
  loadData()
}

const onResetPwd = (row) => {
  Object.assign(pwdDialog, { visible: true, id: row.id, username: row.username, realName: row.realName, password: '' })
}

const onSavePwd = async () => {
  if (!pwdDialog.password) {
    ElMessage.warning('请输入新密码')
    return
  }
  if (pwdDialog.password.length < 6) {
    ElMessage.warning('密码至少 6 位')
    return
  }
  await request({ url: '/system/user/resetPwd', method: 'post', data: { id: pwdDialog.id, password: pwdDialog.password } })
  ElMessage.success('密码已重置')
  pwdDialog.visible = false
}

const onToggleStatus = async (row) => {
  const target = row.status === 1 ? 0 : 1
  await ElMessageBox.confirm(`确认${target === 1 ? '启用' : '禁用'}用户 [${row.username}]？`, '提示', { type: 'warning' })
  await request({ url: '/system/user', method: 'put', data: { id: row.id, status: target } })
  ElMessage.success('操作成功')
  loadData()
}

onMounted(() => { loadData(); loadDeptTree(); loadRoles() })
</script>
