/**
 * 前端页面全功能静态检测 + 菜单路由检测
 * 用法: node test-frontend-pages.js
 */
const fs = require('fs')
const path = require('path')
const http = require('http')

const ROOT = __dirname
const WEB = path.join(ROOT, 'finance-web')
const ROUTER_FILE = path.join(WEB, 'src/router/index.js')
const BASE = process.env.API_BASE || 'http://127.0.0.1:18080'

function request(method, urlPath, body, token) {
  return new Promise((resolve) => {
    const data = body ? JSON.stringify(body) : null
    const headers = { 'Content-Type': 'application/json' }
    if (data) headers['Content-Length'] = Buffer.byteLength(data)
    if (token) headers['Authorization'] = 'Bearer ' + token
    const url = new URL(BASE + urlPath)
    const req = http.request(
      { hostname: url.hostname, port: url.port, path: url.pathname + url.search, method, headers },
      (res) => {
        let chunks = []
        res.on('data', (c) => chunks.push(c))
        res.on('end', () => {
          const text = Buffer.concat(chunks).toString('utf8')
          let json = null
          try { json = JSON.parse(text) } catch (e) {}
          resolve({ status: res.statusCode, json, text })
        })
      }
    )
    req.on('error', (e) => resolve({ status: 0, error: e.message }))
    if (data) req.write(data)
    req.end()
  })
}

function extractRoutes(routerContent) {
  const routes = []
  const re = /path:\s*'([^']+)',\s*name:\s*'([^']+)'[\s\S]*?import\('@\/views\/([^']+)'\)/g
  let m
  while ((m = re.exec(routerContent))) {
    const raw = m[1]
    const routePath = raw.startsWith('/') ? raw : '/' + raw
    routes.push({ path: routePath, name: m[2], view: m[3] })
  }
  return routes
}

const SKIP_LOAD_CHECK = new Set(['/login', '/dashboard'])

function hasDataLoader(content) {
  return /onMounted|loadData|onQuery/.test(content)
}

function buildApiProbeUrl(apiPath) {
  const full = '/api' + apiPath
  if (full.includes('?')) return full
  const params = new URLSearchParams()
  if (apiPath.includes('page')) {
    params.set('pageNum', '1')
    params.set('pageSize', '5')
  }
  if (apiPath.includes('/myTasks')) params.set('userId', '1')
  if (apiPath.includes('/analysis') || apiPath.includes('/summary') || apiPath.includes('/product/')) {
    params.set('fiscalYear', '2026')
    params.set('fiscalPeriod', '6')
  }
  const qs = params.toString()
  return qs ? `${full}?${qs}` : full
}

function walkMenu(menus, parentPath = '', acc = []) {
  for (const menu of (menus || []).filter(m => m.menuType !== 'F')) {
    if (menu.menuType === 'M' && menu.children?.length) {
      walkMenu(menu.children, menu.path || parentPath, acc)
    } else if (menu.menuType === 'C') {
      acc.push({ id: menu.id, name: menu.menuName, component: menu.component, parentPath })
    }
  }
  return acc
}

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
  'report/bs/index': '/report/balance',
  'report/is/index': '/report/income',
  'report/cf/index': '/report/cashflow',
  'report/custom/index': '/report/custom'
}

function resolveMenuRoute(menu) {
  if (menu.component && COMPONENT_ROUTE_MAP[menu.component]) {
    return COMPONENT_ROUTE_MAP[menu.component]
  }
  return null
}

async function main() {
  const today = new Date().toISOString().slice(0, 10)
  const routerContent = fs.readFileSync(ROUTER_FILE, 'utf8')
  const routes = extractRoutes(routerContent)
  const fails = []
  const results = { routes: [], menu: [], pages: [], duplicates: [] }

  for (const r of routes) {
    const viewPath = path.join(WEB, 'src/views', r.view)
    const ok = fs.existsSync(viewPath)
    results.routes.push({ ...r, ok })
    if (!ok) fails.push({ type: '页面文件缺失', detail: `${r.path} -> ${r.view}` })
  }

  const login = await request('POST', '/api/auth/login', { username: 'admin', password: '123456' })
  const token = login.json?.data?.token
  if (!token) {
    console.error('登录失败，无法继续菜单/API 检测')
    process.exit(1)
  }

  const menuResp = await request('GET', '/api/system/menu/userTree/1', null, token)
  const menus = menuResp.json?.data || []
  const flatMenus = walkMenu(menus)

  for (const m of flatMenus) {
    const route = resolveMenuRoute(m)
    const routeOk = route ? routes.some(r => r.path === route) : false
    results.menu.push({ ...m, route, routeOk })
    if (!routeOk) fails.push({ type: '菜单路由未匹配', detail: `${m.name} (${m.component})` })
  }

  const routeGroups = {}
  for (const m of results.menu.filter(x => x.route)) {
    if (!routeGroups[m.route]) routeGroups[m.route] = []
    routeGroups[m.route].push(m.name)
  }
  for (const [route, names] of Object.entries(routeGroups)) {
    if (names.length > 1) results.duplicates.push({ route, names })
  }

  for (const r of routes) {
    if (SKIP_LOAD_CHECK.has(r.path)) continue
    const viewPath = path.join(WEB, 'src/views', r.view)
    if (!fs.existsSync(viewPath)) continue
    const content = fs.readFileSync(viewPath, 'utf8')
    const issues = []
    if (!hasDataLoader(content) && !content.includes('defineProps')) {
      issues.push('缺少 onMounted/loadData')
    }
    if (content.includes("import { ref") === false && content.includes('ref(') && !content.includes("from 'vue'")) {
      issues.push('可能缺少 ref 导入')
    }
    const page = { path: r.path, view: r.view, issues, apiProbe: null }
    results.pages.push(page)
    if (issues.length) fails.push({ type: '页面静态问题', detail: `${r.path}: ${issues.join(', ')}` })

    const getApi = content.match(/url:\s*'(\/[^']+)',\s*method:\s*'get'/i)
    if (getApi) {
      const apiPath = buildApiProbeUrl(getApi[1])
      const resp = await request('GET', apiPath, null, token)
      const ok = resp.json?.code === 200
      page.apiProbe = { url: apiPath, ok, code: resp.json?.code, http: resp.status }
      if (!ok) fails.push({ type: '页面API失败', detail: `${r.path} -> ${apiPath} code=${resp.json?.code || resp.status}` })
    }
  }

  const routeOk = results.routes.filter(r => r.ok).length
  const menuOk = results.menu.filter(m => m.routeOk).length
  const pageIssues = results.pages.filter(p => p.issues.length).length
  const apiFails = results.pages.filter(p => p.apiProbe && !p.apiProbe.ok).length

  let md = `# 前端页面全功能测试报告\n\n`
  md += `**测试时间**：${today}\n`
  md += `**后端地址**：${BASE}\n\n`
  md += `| 指标 | 结果 |\n|------|------|\n`
  md += `| 路由总数 | ${routes.length} |\n`
  md += `| 路由页面文件 | ${routeOk}/${routes.length} |\n`
  md += `| 菜单项 | ${results.menu.length}，路由匹配 ${menuOk}/${results.menu.length} |\n`
  md += `| 重复菜单路由 | ${results.duplicates.length} 组 |\n`
  md += `| 页面静态问题 | ${pageIssues} |\n`
  md += `| 页面 API 探活失败 | ${apiFails} |\n`
  md += `| **总失败项** | **${fails.length}** |\n\n`

  if (results.duplicates.length) {
    md += `## 共用路由的菜单\n\n| 路由 | 菜单项 |\n|------|--------|\n`
    for (const d of results.duplicates) md += `| \`${d.route}\` | ${d.names.join('、')} |\n`
    md += `\n`
  }

  md += `## 路由清单\n\n| 路由 | 页面 | 状态 |\n|------|------|------|\n`
  for (const r of results.routes) md += `| \`${r.path}\` | ${r.view} | ${r.ok ? '✅' : '❌'} |\n`

  if (fails.length) {
    md += `\n## 失败详情\n\n| 类型 | 详情 |\n|------|------|\n`
    for (const f of fails) md += `| ${f.type} | ${f.detail} |\n`
  } else {
    md += `\n## 失败详情\n\n全部通过。\n`
  }

  const out = path.join(ROOT, `前端测试报告-${today}.md`)
  fs.writeFileSync(out, md, 'utf8')
  console.log(`\n报告已生成: ${out}`)
  console.log(`汇总: 路由=${routes.length} 菜单=${results.menu.length} 失败=${fails.length}`)
  process.exit(fails.length ? 1 : 0)
}

main().catch((e) => {
  console.error(e)
  process.exit(1)
})
