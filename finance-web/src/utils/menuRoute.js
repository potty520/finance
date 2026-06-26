/** 后端 component 路径 → 前端路由映射（每项独立路由，避免点击不跳转） */



const COMPONENT_ROUTE_MAP = {

  'system/user/index': '/system/user',

  'system/role/index': '/system/role',

  'system/menu/index': '/system/menu',

  'system/dept/index': '/system/dept',

  'system/dict/index': '/system/dict',

  'system/config/index': '/system/config',

  'system/company/index': '/system/company',

  'system/log/login': '/system/log/login',

  'system/log/operation': '/system/log/operation',



  'gl/subject/index': '/ledger/subject',

  'gl/auxiliary/index': '/ledger/auxiliary',

  'gl/period/index': '/ledger/period',

  'gl/voucher/index': '/ledger/voucher',

  'gl/voucher/list': '/ledger/voucher-list',

  'gl/ledger/index': '/ledger/book',

  'gl/cashflow/index': '/ledger/cashflow',

  'gl/closing/index': '/ledger/closing',



  'cash/account/index': '/cashier/account',

  'cash/journal/index': '/cashier/daily',

  'cash/reconcile/index': '/cashier/bank',

  'cash/bill/index': '/cashier/bill',



  'fa/category/index': '/asset/category',

  'fa/asset/index': '/asset/card',

  'fa/depreciation/index': '/asset/depreciation',

  'fa/disposal/index': '/asset/disposal',

  'fa/inventory/index': '/asset/inventory',



  'ar/customer/index': '/receivable/customer',

  'ar/invoice/index': '/receivable/invoice',

  'ar/receipt/index': '/receivable/receipt',

  'ar/writeoff/index': '/receivable/writeoff',

  'ar/bill/index': '/receivable/bill',

  'ar/aging/index': '/receivable/aging',



  'ap/supplier/index': '/payable/supplier',

  'ap/invoice/index': '/payable/invoice',

  'ap/payment/index': '/payable/payment',

  'ap/writeoff/index': '/payable/writeoff',

  'ap/bill/index': '/payable/bill',

  'ap/aging/index': '/payable/aging',



  'inventory/item/index': '/stock/goods',

  'inventory/warehouse/index': '/stock/warehouse',

  'inventory/transaction/index': '/stock/io',

  'inventory/balance/index': '/stock/balance',

  'inventory/adjust/index': '/stock/adjust',



  'cost/center/index': '/cost/center',

  'cost/element/index': '/cost/element',

  'cost/calc/index': '/cost/product',

  'cost/allocation/index': '/cost/allocation',



  'budget/master/index': '/budget/list',

  'budget/exec/index': '/budget/exec',

  'budget/analysis/index': '/budget/analysis',



  'expense/apply/index': '/expense/apply',

  'expense/loan/index': '/expense/loan',

  'expense/repay/index': '/expense/repay',



  'contract/sales/index': '/contract/sales',

  'contract/purchase/index': '/contract/purchase',



  'project/index': '/project/list',

  'project/budget/index': '/project/budget',

  'project/cost/index': '/project/cost',



  'consolidation/group/index': '/consol/group',

  'consolidation/elim/index': '/consol/offset',

  'consolidation/worksheet/index': '/consol/worksheet',



  'tax/Vat': '/tax/vat',


  'report/bs/index': '/report/balance',

  'report/is/index': '/report/income',

  'report/cf/index': '/report/cashflow',

  'report/custom/index': '/report/custom'

}



const PATH_PREFIX_ROUTE_MAP = {

  system: '/system',

  base: '/ledger',

  gl: '/ledger',

  cash: '/cashier',

  fa: '/asset',

  ar: '/receivable',

  ap: '/payable',

  inventory: '/stock',

  cost: '/cost',

  budget: '/budget',

  expense: '/expense',

  contract: '/contract',

  project: '/project',

  consolidation: '/consol',

  report: '/report',
  tax: '/tax'

}



export function resolveMenuRoute(menu, parentPath = '') {

  if (menu.component && COMPONENT_ROUTE_MAP[menu.component]) {

    return COMPONENT_ROUTE_MAP[menu.component]

  }



  if (menu.menuType === 'C' && menu.path) {

    const prefix = PATH_PREFIX_ROUTE_MAP[parentPath] || (parentPath ? `/${parentPath}` : '')

    if (prefix && menu.path) {

      return `${prefix}/${menu.path}`

    }

  }



  return null

}



export function filterRouteMenus(menus) {

  return (menus || []).filter(m => m.menuType !== 'F' && (m.visible == null || m.visible !== 0))

}



export function walkMenus(menus, parentPath = '', visitor) {

  for (const menu of filterRouteMenus(menus)) {

    if (menu.menuType === 'M' && menu.children && menu.children.length) {

      const modulePath = menu.path || parentPath

      walkMenus(menu.children, modulePath, visitor)

    } else if (menu.menuType === 'C') {

      const routePath = resolveMenuRoute(menu, parentPath)

      if (routePath) visitor(menu, routePath, parentPath)

    }

  }

}



export function findRouteByMenuId(menus, menuId) {

  let found = null

  walkMenus(menus, '', (menu, routePath) => {

    if (String(menu.id) === String(menuId)) found = routePath

  })

  return found

}



export function findActiveMenuId(menus, currentPath, menuIdQuery) {

  if (menuIdQuery) {

    let matched = null

    walkMenus(menus, '', (menu, routePath) => {

      if (String(menu.id) === String(menuIdQuery) && routePath === currentPath) {

        matched = String(menu.id)

      }

    })

    if (matched) return matched

  }



  let found = null

  walkMenus(menus, '', (menu, routePath) => {

    if (routePath === currentPath && !found) found = String(menu.id)

  })

  return found

}



export function buildMenuLocation(menus, menuId) {

  const path = findRouteByMenuId(menus, menuId)

  if (!path) return null

  return { path, query: { m: String(menuId) } }

}



export function listDuplicateRoutes(menus) {

  const map = {}

  walkMenus(menus, '', (menu, routePath) => {

    if (!map[routePath]) map[routePath] = []

    map[routePath].push({ id: menu.id, name: menu.menuName, component: menu.component })

  })

  return Object.entries(map)

    .filter(([, items]) => items.length > 1)

    .map(([route, items]) => ({ route, items }))

}


