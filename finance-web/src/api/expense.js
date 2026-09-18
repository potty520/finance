import request from '@/utils/request'

// 费用申请/报销
export const applyPage = (params) => request({ url: '/expense/apply/page', method: 'get', params })
export const saveApply = (data) => request({ url: '/expense/apply', method: 'post', data })
/** 标记已付款（须先审批通过） */
export const payApply = (id) => request({ url: '/expense/apply/pay/' + id, method: 'post' })
/** 业财一体：费用报销单生成记账凭证 */
export const applyVoucher = (id) => request({ url: '/expense/apply/' + id + '/voucher', method: 'post' })
export const applyAnalysis = (params) => request({ url: '/expense/apply/analysis', method: 'get', params })

// 借款
export const loanList = (params) => request({ url: '/expense/loan/list', method: 'get', params })
export const saveLoan = (data) => request({ url: '/expense/loan', method: 'post', data })
export const repayLoan = (data) => request({ url: '/expense/loan/repay', method: 'post', data })