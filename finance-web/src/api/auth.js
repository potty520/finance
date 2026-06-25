import request from '@/utils/request'

export const login = (data) => request({ url: '/auth/login', method: 'post', data })
export const logout = () => request({ url: '/auth/logout', method: 'post' })
export const getInfo = () => request({ url: '/auth/me', method: 'get' })
