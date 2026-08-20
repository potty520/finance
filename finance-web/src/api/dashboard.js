import request from '@/utils/request'

export const getSummary = () => request({ url: '/dashboard/summary', method: 'get' })
export const getTrend = (months = 6) => request({ url: '/dashboard/trend', method: 'get', params: { months } })
export const getAlerts = () => request({ url: '/dashboard/alerts', method: 'get' })
export const getRecentVouchers = (limit = 8) => request({ url: '/dashboard/recentVouchers', method: 'get', params: { limit } })
