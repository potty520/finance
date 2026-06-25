<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1>财务管理系统</h1>
        <p>Enterprise Financial Management</p>
      </div>
      <el-form ref="formRef" :model="form" :rules="rules" size="large" @keyup.enter="onLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="密码" :prefix-icon="Lock" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="form.remember">记住我</el-checkbox>
        </el-form-item>
        <el-button type="primary" :loading="loading" style="width:100%" @click="onLogin">登 录</el-button>
      </el-form>
      <div class="login-tip">默认账号: admin / 123456</div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: 'admin', password: '123456', remember: true })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const onLogin = async () => {
  await formRef.value.validate()
  try {
    loading.value = true
    await userStore.login({ username: form.username, password: form.password })
    await userStore.loadInfo()
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-container {
  height: 100vh; background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  display: flex; align-items: center; justify-content: center;
}
.login-box {
  width: 400px; padding: 40px; background: #fff; border-radius: 8px;
  box-shadow: 0 2px 20px rgba(0,0,0,0.15);
}
.login-header { text-align: center; margin-bottom: 30px; }
.login-header h1 { margin: 0 0 8px 0; color: #1e3c72; }
.login-header p { margin: 0; color: #999; font-size: 12px; }
.login-tip { text-align: center; color: #999; font-size: 12px; margin-top: 16px; }
</style>
