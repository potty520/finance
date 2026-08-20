import request from '@/utils/request'

// 供应商
export const supplierPage = (params) => request({ url: '/payable/supplier/page', method: 'get', params })
export const supplierList = () => request({ url: '/payable/supplier/list', method: 'get' })
export const saveSupplier = (data) => request({ url: '/payable/supplier', method: 'post', data })
export const updateSupplier = (data) => request({ url: '/payable/supplier', method: 'put', data })
export const deleteSupplier = (id) => request({ url: `/payable/supplier/${id}`, method: 'delete' })

// 采购发票
export const apInvoicePage = (params) => request({ url: '/payable/invoice/page', method: 'get', params })
export const apInvoiceDetail = (id) => request({ url: `/payable/invoice/${id}`, method: 'get' })
export const saveApInvoice = (data) => request({ url: '/payable/invoice', method: 'post', data })
export const updateApInvoice = (data) => request({ url: '/payable/invoice', method: 'put', data })
export const deleteApInvoice = (id) => request({ url: `/payable/invoice/${id}`, method: 'delete' })
export const auditApInvoice = (id) => request({ url: `/payable/invoice/audit/${id}`, method: 'post' })

// 付款单
export const paymentPage = (params) => request({ url: '/payable/payment/page', method: 'get', params })
export const savePayment = (data) => request({ url: '/payable/payment', method: 'post', data })
export const updatePayment = (data) => request({ url: '/payable/payment', method: 'put', data })
export const deletePayment = (id) => request({ url: `/payable/payment/${id}`, method: 'delete' })
export const auditPayment = (id) => request({ url: `/payable/payment/audit/${id}`, method: 'post' })

// 核销
export const doApWriteoff = (data) => request({ url: '/payable/writeoff', method: 'post', data })
export const writeoffByPayment = (paymentId) => request({ url: `/payable/writeoff/payment/${paymentId}`, method: 'get' })
export const writeoffByApInvoice = (invoiceId) => request({ url: `/payable/writeoff/invoice/${invoiceId}`, method: 'get' })

// 供应商余额
export const supplierUnpaid = (id) => request({ url: `/payable/supplier/${id}/unpaid`, method: 'get' })
export const supplierUnapplied = (id) => request({ url: `/payable/supplier/${id}/unapplied`, method: 'get' })
