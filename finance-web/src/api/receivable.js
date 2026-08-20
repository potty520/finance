import request from '@/utils/request'

// 客户
export const customerPage = (params) => request({ url: '/receivable/customer/page', method: 'get', params })
export const customerList = () => request({ url: '/receivable/customer/list', method: 'get' })
export const saveCustomer = (data) => request({ url: '/receivable/customer', method: 'post', data })
export const updateCustomer = (data) => request({ url: '/receivable/customer', method: 'put', data })
export const deleteCustomer = (id) => request({ url: `/receivable/customer/${id}`, method: 'delete' })

// 应收发票
export const invoicePage = (params) => request({ url: '/receivable/invoice/page', method: 'get', params })
export const invoiceDetail = (id) => request({ url: `/receivable/invoice/${id}`, method: 'get' })
export const saveInvoice = (data) => request({ url: '/receivable/invoice', method: 'post', data })
export const updateInvoice = (data) => request({ url: '/receivable/invoice', method: 'put', data })
export const deleteInvoice = (id) => request({ url: `/receivable/invoice/${id}`, method: 'delete' })
export const auditInvoice = (id) => request({ url: `/receivable/invoice/audit/${id}`, method: 'post' })

// 收款单
export const receiptPage = (params) => request({ url: '/receivable/receipt/page', method: 'get', params })
export const saveReceipt = (data) => request({ url: '/receivable/receipt', method: 'post', data })
export const updateReceipt = (data) => request({ url: '/receivable/receipt', method: 'put', data })
export const deleteReceipt = (id) => request({ url: `/receivable/receipt/${id}`, method: 'delete' })
export const auditReceipt = (id) => request({ url: `/receivable/receipt/audit/${id}`, method: 'post' })

// 核销
export const doWriteoff = (data) => request({ url: '/receivable/writeoff', method: 'post', data })
export const writeoffByReceipt = (receiptId) => request({ url: `/receivable/writeoff/receipt/${receiptId}`, method: 'get' })
export const writeoffByInvoice = (invoiceId) => request({ url: `/receivable/writeoff/invoice/${invoiceId}`, method: 'get' })

// 客户余额
export const customerUncollected = (id) => request({ url: `/receivable/customer/${id}/uncollected`, method: 'get' })
export const customerUnapplied = (id) => request({ url: `/receivable/customer/${id}/unapplied`, method: 'get' })
export const customerAging = (id) => request({ url: `/receivable/aging/${id}`, method: 'get' })
