import request from '@/utils/request'

export const page = (params) => request({ url: '/ledger/voucher/page', method: 'get', params })
export const detail = (id) => request({ url: `/ledger/voucher/${id}`, method: 'get' })
export const save = (data) => request({ url: '/ledger/voucher', method: 'post', data })
export const update = (data) => request({ url: '/ledger/voucher', method: 'put', data })
export const del = (id) => request({ url: `/ledger/voucher/${id}`, method: 'delete' })
export const submit = (id) => request({ url: `/ledger/voucher/submit/${id}`, method: 'post' })
export const audit = (data) => request({ url: '/ledger/voucher/audit', method: 'post', data })
export const approve = (id) => request({ url: `/ledger/voucher/approve/${id}`, method: 'post' })
export const post = (id) => request({ url: `/ledger/voucher/post/${id}`, method: 'post' })
export const listSubjects = () => request({ url: '/ledger/subject/list', method: 'get' })
export const nextVoucherNo = (params) => request({ url: '/ledger/voucher/nextNo', method: 'get', params })
