import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { useUserStore } from '@/store/user'

const service = axios.create({
  baseURL: '/api',
  timeout: 30000
})

service.interceptors.request.use(config => {
  const userStore = useUserStore()
  if (userStore.token) {
    config.headers['Authorization'] = 'Bearer ' + userStore.token
  }
  return config
})

function toLogin(message) {
  const userStore = useUserStore()
  userStore.logout()
  if (router.currentRoute.value.path !== '/login') {
    ElMessage.error(message)
    router.push('/login')
  }
}

service.interceptors.response.use(
  response => {
    // blob 类型响应（文件下载）直接返回
    if (response.config.responseType === 'blob') {
      return response.data
    }
    const res = response.data
    if (res.code === 200) {
      return res
    }
    if (res.code === 401) {
      toLogin('登录已过期，请重新登录')
      return Promise.reject(new Error(res.message))
    }
    if (res.code === 403) {
      ElMessage.error(res.message || '没有操作权限')
      return Promise.reject(new Error(res.message || '没有操作权限'))
    }
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  error => {
    const status = error.response && error.response.status
    // 后端未认证/越权时返回标准 HTTP 状态码，响应体仍是统一 JSON 结构
    let message = (error.response && error.response.data && error.response.data.message) || ''
    if (status === 401) {
      toLogin(message || '登录已过期，请重新登录')
    } else if (status === 403) {
      ElMessage.error(message || '没有操作权限')
    } else if (status === 404) {
      ElMessage.error(message || '接口不存在')
    } else if (status >= 500) {
      ElMessage.error(message || '服务器异常，请稍后重试')
    } else {
      ElMessage.error(message || error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default service
