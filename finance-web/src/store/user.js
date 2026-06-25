import { defineStore } from 'pinia'
import { login as apiLogin, getInfo, logout as apiLogout } from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || 'null'),
    roles: [],
    permissions: [],
    menus: JSON.parse(localStorage.getItem('menus') || '[]')
  }),
  actions: {
    async login(form) {
      const { data } = await apiLogin(form)
      this.token = data.token
      localStorage.setItem('token', data.token)
      return data
    },
    async loadInfo() {
      const { data } = await getInfo()
      this.userInfo = data
      this.roles = data.roles || []
      this.permissions = data.permissions || []
      this.menus = data.menus || []
      localStorage.setItem('userInfo', JSON.stringify(data))
      localStorage.setItem('menus', JSON.stringify(this.menus))
      return data
    },
    async logout() {
      try { await apiLogout() } catch (e) {}
      this.token = ''
      this.userInfo = null
      this.menus = []
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('menus')
    }
  }
})
