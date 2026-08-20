// ========================================
// 业务模块功能色映射
// 按菜单名匹配模块，返回 CSS 变量与 RGB 值
// （RGB 值用于 rgba() 半透明背景，如激活态）
// ========================================

const MODULES = [
  { keys: ['总账', '报表', '税务', '财务'], color: 'var(--mod-ledger)', rgb: 'var(--mod-ledger-rgb)' },
  { keys: ['出纳', '费用', '资金', '银行'], color: 'var(--mod-cashier)', rgb: 'var(--mod-cashier-rgb)' },
  { keys: ['应收'], color: 'var(--mod-receivable)', rgb: 'var(--mod-receivable-rgb)' },
  { keys: ['应付', '合同', '采购'], color: 'var(--mod-payable)', rgb: 'var(--mod-payable-rgb)' },
  { keys: ['资产', '存货', '库存'], color: 'var(--mod-asset)', rgb: 'var(--mod-asset-rgb)' },
  { keys: ['预算', '成本', '项目'], color: 'var(--mod-budget)', rgb: 'var(--mod-budget-rgb)' },
  { keys: ['合并', '审批', '流程'], color: 'var(--mod-consol)', rgb: 'var(--mod-consol-rgb)' },
  { keys: ['系统', '设置'], color: 'var(--mod-system)', rgb: 'var(--mod-system-rgb)' }
]

export function getModuleColor(name = '') {
  const hit = MODULES.find(m => m.keys.some(k => name.includes(k)))
  if (hit) return { color: hit.color, rgb: hit.rgb }
  return { color: 'var(--mod-dashboard)', rgb: 'var(--mod-dashboard-rgb)' }
}

// 由菜单项计算模块色（支持顶级目录名或子菜单路径）
export function moduleColorOf(menu, parentName = '') {
  const name = menu?.menuName || menu?.title || parentName || ''
  return getModuleColor(name)
}
