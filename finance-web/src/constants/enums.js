// 财务系统枚举中文映射 - 统一常量定义

// 通用状态
export const STATUS_MAP = {
  ACTIVE: '启用', INACTIVE: '停用', FROZEN: '冻结'
}
export const AUDIT_STATUS_MAP = {
  DRAFT: '草稿', PENDING: '待审核', AUDITED: '已审核', APPROVED: '已审核',
  POSTED: '已过账', VOIDED: '已作废', PAID: '已付款', RECEIVED: '已收款',
  CANCELED: '已取消', FINISHED: '已完成'
}
export const STATUS_TAG_TYPE = {
  DRAFT: 'info', PENDING: 'warning', AUDITED: '', APPROVED: 'success',
  POSTED: '', PAID: 'success', RECEIVED: 'success', VOIDED: 'danger',
  CANCELED: 'danger', FINISHED: 'success', ACTIVE: 'success', INACTIVE: 'info',
  FROZEN: 'warning'
}

// 常用业务类型
export const IO_TYPE_MAP = { IN: '收', OUT: '付' }
export const IO_TYPE_MAP2 = { IN: '入库', OUT: '出库', TRANSFER: '调拨', INCOME: '收入', EXPENSE: '支出' }
export const DIRECTION_MAP = { IN: '流入', OUT: '流出' }
export const CONTRACT_TYPE_MAP = { SALES: '销售', PURCHASE: '采购', SERVICE: '服务' }
export const BUDGET_TYPE_MAP = { COST: '成本预算', REVENUE: '收入预算', CAPITAL: '资本预算' }
export const ELEMENT_TYPE_MAP = { DIRECT_MATERIAL: '直接材料', DIRECT_LABOR: '直接人工', MANUFACTURE: '制造费用' }
export const CENTER_TYPE_MAP = { COST: '成本中心', PROFIT: '利润中心', INVESTMENT: '投资中心' }
export const GROUP_TYPE_MAP = { HQ: '总部', SUB: '子公司', BR: '分公司' }
export const WAREHOUSE_TYPE_MAP = { OWN: '自有', RENT: '租赁', PUBLIC: '公共' }
export const BILL_TYPE_MAP = { CHEQUE: '支票', PROMISSORY: '本票', ACCEPTANCE: '汇票' }
export const BILL_STATUS_MAP = { HOLD: '持有', USE: '已使用', ENDORSE: '已背书', DISCOUNT: '已贴现', RECEIVE: '已收款', CANCEL: '已作废' }
export const DISPOSAL_TYPE_MAP = { SELL: '出售', SCRAP: '报废', LOSS: '盘亏', DONATE: '捐赠' }
export const APPLY_TYPE_MAP = { EXPENSE: '费用', TRAVEL: '差旅' }
export const AUX_TYPE_MAP = { CUSTOMER: '客户', DEPT: '部门', EMPLOYEE: '员工', PRODUCT: '产品', PROJECT: '项目', SUPPLIER: '供应商' }
export const FLOW_TYPE_MAP = { MAIN: '主表', SUB: '补充资料' }
export const REPORT_TYPE_MAP = { CUSTOM: '自定义', BALANCE: '资产负债表', INCOME: '利润表', CASHFLOW: '现金流量表' }
export const MENU_TYPE_MAP = { M: '菜单', C: '目录', F: '按钮' }
export const SUBJECT_TYPE_MAP = { ASSET: '资产', LIAB: '负债', EQUITY: '权益', COST: '成本', INCOME: '收入', EXPENSE: '费用', COMMON: '共同' }
export const BUSINESS_TYPE_MAP = { EXPENSE: '费用', INCOME: '收入', LABOR: '人工', MATERIAL: '材料', contract: '合同', PAYMENT: '付款', RECEIPT: '收款', VOUCHER: '凭证' }
export const OFFSET_TYPE_MAP = { INV: '存货', AR: '往来', INCOME: '内部交易', DIV: '股利', FIXED: '固定资产' }
export const LOGIN_STATUS_MAP = { SUCCESS: '成功', FAIL: '失败' }
export const PERIOD_STATUS_MAP = { OPEN: '已开启', CLOSED: '已关闭' }
export const VOUCHER_STATUS_MAP = { DRAFT: '草稿', POSTED: '已过账', VOIDED: '已作废' }

// 工具函数
export function enumText(map, value, fallback) {
  return map[value] || fallback || value || '-'
}
