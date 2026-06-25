import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/login', name: 'Login', component: () => import('@/views/login/Login.vue'), meta: { public: true } },
  {
    path: '/',
    component: () => import('@/layout/Index.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/Index.vue'), meta: { title: '工作台', icon: 'HomeFilled' } },
      // 总账
      { path: 'ledger/voucher', name: 'Voucher', component: () => import('@/views/ledger/Voucher.vue'), meta: { title: '凭证录入' } },
      { path: 'ledger/voucher-list', name: 'VoucherList', component: () => import('@/views/ledger/Voucher.vue'), meta: { title: '凭证管理' } },
      { path: 'ledger/subject', name: 'Subject', component: () => import('@/views/ledger/Subject.vue'), meta: { title: '科目管理' } },
      { path: 'ledger/period', name: 'Period', component: () => import('@/views/ledger/Period.vue'), meta: { title: '会计期间' } },
      { path: 'ledger/book', name: 'Book', component: () => import('@/views/ledger/Book.vue'), meta: { title: '账簿查询' } },
      { path: 'ledger/cashflow', name: 'LedgerCashflow', component: () => import('@/views/ledger/Cashflow.vue'), meta: { title: '现金流量' } },
      { path: 'ledger/closing', name: 'LedgerClosing', component: () => import('@/views/ledger/Period.vue'), meta: { title: '结账管理' } },
      { path: 'ledger/auxiliary', name: 'LedgerAuxiliary', component: () => import('@/views/ledger/Auxiliary.vue'), meta: { title: '辅助核算' } },
      // 出纳
      { path: 'cashier/account', name: 'CashAccount', component: () => import('@/views/cashier/Account.vue'), meta: { title: '资金账户' } },
      { path: 'cashier/daily', name: 'CashDaily', component: () => import('@/views/cashier/Daily.vue'), meta: { title: '出纳日记账' } },
      { path: 'cashier/bank', name: 'CashBank', component: () => import('@/views/cashier/Bank.vue'), meta: { title: '银行对账' } },
      { path: 'cashier/bill', name: 'CashBill', component: () => import('@/views/cashier/Bill.vue'), meta: { title: '票据管理' } },
      // 应收
      { path: 'receivable/list', name: 'Receivable', component: () => import('@/views/receivable/Index.vue'), meta: { title: '应收账款' } },
      { path: 'receivable/customer', name: 'ARCustomer', component: () => import('@/views/receivable/Customer.vue'), meta: { title: '客户档案' } },
      { path: 'receivable/invoice', name: 'ARInvoice', component: () => import('@/views/receivable/Invoice.vue'), meta: { title: '销售发票' } },
      { path: 'receivable/receipt', name: 'ARReceipt', component: () => import('@/views/receivable/Receipt.vue'), meta: { title: '收款单' } },
      { path: 'receivable/writeoff', name: 'ARWriteoff', component: () => import('@/views/receivable/Invoice.vue'), meta: { title: '核销管理' } },
      { path: 'receivable/bill', name: 'ARBill', component: () => import('@/views/receivable/Bill.vue'), meta: { title: '应收票据' } },
      { path: 'receivable/aging', name: 'ARAging', component: () => import('@/views/receivable/Aging.vue'), meta: { title: '账龄分析' } },
      // 应付
      { path: 'payable/list', name: 'Payable', component: () => import('@/views/payable/Index.vue'), meta: { title: '应付账款' } },
      { path: 'payable/supplier', name: 'APSupplier', component: () => import('@/views/payable/Supplier.vue'), meta: { title: '供应商档案' } },
      { path: 'payable/invoice', name: 'APInvoice', component: () => import('@/views/payable/Invoice.vue'), meta: { title: '采购发票' } },
      { path: 'payable/payment', name: 'APPayment', component: () => import('@/views/payable/Payment.vue'), meta: { title: '付款单' } },
      { path: 'payable/writeoff', name: 'APWriteoff', component: () => import('@/views/payable/Invoice.vue'), meta: { title: '核销管理' } },
      { path: 'payable/bill', name: 'APBill', component: () => import('@/views/payable/Bill.vue'), meta: { title: '应付票据' } },
      { path: 'payable/aging', name: 'APAging', component: () => import('@/views/payable/Aging.vue'), meta: { title: '账龄分析' } },
      // 资产
      { path: 'asset/list', name: 'Asset', component: () => import('@/views/asset/Index.vue'), meta: { title: '固定资产' } },
      { path: 'asset/card', name: 'AssetCard', component: () => import('@/views/asset/Index.vue'), meta: { title: '资产卡片' } },
      { path: 'asset/category', name: 'AssetCategory', component: () => import('@/views/asset/Category.vue'), meta: { title: '资产类别' } },
      { path: 'asset/disposal', name: 'AssetDisposal', component: () => import('@/views/asset/Disposal.vue'), meta: { title: '资产处置' } },
      { path: 'asset/inventory', name: 'AssetInventory', component: () => import('@/views/asset/Inventory.vue'), meta: { title: '资产盘点' } },
      { path: 'asset/depreciation', name: 'AssetDep', component: () => import('@/views/asset/Depreciation.vue'), meta: { title: '折旧管理' } },
      // 存货
      { path: 'stock/goods', name: 'StockGoods', component: () => import('@/views/stock/Goods.vue'), meta: { title: '物料档案' } },
      { path: 'stock/warehouse', name: 'StockWarehouse', component: () => import('@/views/stock/Warehouse.vue'), meta: { title: '仓库管理' } },
      { path: 'stock/io', name: 'StockIo', component: () => import('@/views/stock/Io.vue'), meta: { title: '出入库管理' } },
      { path: 'stock/balance', name: 'StockBalance', component: () => import('@/views/stock/Balance.vue'), meta: { title: '库存余额' } },
      { path: 'stock/adjust', name: 'StockAdjust', component: () => import('@/views/stock/Adjust.vue'), meta: { title: '成本调整' } },
      // 成本
      { path: 'cost/center', name: 'CostCenter', component: () => import('@/views/cost/Center.vue'), meta: { title: '成本中心' } },
      { path: 'cost/element', name: 'CostElement', component: () => import('@/views/cost/Element.vue'), meta: { title: '成本要素' } },
      { path: 'cost/allocation', name: 'CostAllocation', component: () => import('@/views/cost/Allocation.vue'), meta: { title: '费用归集' } },
      { path: 'cost/product', name: 'CostProduct', component: () => import('@/views/cost/Product.vue'), meta: { title: '成本计算' } },
      // 预算
      { path: 'budget/list', name: 'Budget', component: () => import('@/views/budget/Index.vue'), meta: { title: '预算编制' } },
      { path: 'budget/exec', name: 'BudgetExec', component: () => import('@/views/budget/Exec.vue'), meta: { title: '预算执行' } },
      { path: 'budget/analysis', name: 'BudgetAnalysis', component: () => import('@/views/budget/Analysis.vue'), meta: { title: '预算分析' } },
      // 合并报表
      { path: 'consol/group', name: 'ConsolGroup', component: () => import('@/views/consol/Group.vue'), meta: { title: '集团架构' } },
      { path: 'consol/offset', name: 'ConsolOffset', component: () => import('@/views/consol/Offset.vue'), meta: { title: '抵消分录' } },
      { path: 'consol/worksheet', name: 'ConsolWorksheet', component: () => import('@/views/consol/Worksheet.vue'), meta: { title: '合并底稿' } },
      // 审批流
      { path: 'workflow/mine', name: 'WfMine', component: () => import('@/views/workflow/Mine.vue'), meta: { title: '我的待办' } },
      { path: 'workflow/instance', name: 'WfInstance', component: () => import('@/views/workflow/Instance.vue'), meta: { title: '审批记录' } },
      // 报表
      { path: 'report/balance', name: 'RptBalance', component: () => import('@/views/report/Balance.vue'), meta: { title: '资产负债表' } },
      { path: 'report/income', name: 'RptIncome', component: () => import('@/views/report/Income.vue'), meta: { title: '利润表' } },
      { path: 'report/cashflow', name: 'RptCashflow', component: () => import('@/views/report/Cashflow.vue'), meta: { title: '现金流量表' } },
      { path: 'report/custom', name: 'RptCustom', component: () => import('@/views/report/Custom.vue'), meta: { title: '自定义报表' } },
      // 费用
      { path: 'expense/apply', name: 'ExpApply', component: () => import('@/views/expense/Apply.vue'), meta: { title: '我的申请' } },
      { path: 'expense/repay', name: 'ExpRepay', component: () => import('@/views/expense/Repay.vue'), meta: { title: '我的报销' } },
      { path: 'expense/loan', name: 'ExpLoan', component: () => import('@/views/expense/Loan.vue'), meta: { title: '我的借款' } },
      // 合同/项目
      { path: 'contract/sales', name: 'ContractSales', component: () => import('@/views/contract/Sales.vue'), meta: { title: '销售合同' } },
      { path: 'contract/purchase', name: 'ContractPurchase', component: () => import('@/views/contract/Purchase.vue'), meta: { title: '采购合同' } },
      { path: 'project/list', name: 'Project', component: () => import('@/views/project/Index.vue'), meta: { title: '项目列表' } },
      { path: 'project/budget', name: 'ProjectBudget', component: () => import('@/views/project/Budget.vue'), meta: { title: '项目预算' } },
      { path: 'project/ledger', name: 'ProjectLedger', component: () => import('@/views/project/Ledger.vue'), meta: { title: '项目台账' } },
      { path: 'project/cost', name: 'ProjectCost', component: () => import('@/views/project/Cost.vue'), meta: { title: '项目成本' } },
      // 系统
      { path: 'system/user', name: 'SysUser', component: () => import('@/views/system/User.vue'), meta: { title: '用户管理' } },
      { path: 'system/role', name: 'SysRole', component: () => import('@/views/system/Role.vue'), meta: { title: '角色管理' } },
      { path: 'system/menu', name: 'SysMenu', component: () => import('@/views/system/Menu.vue'), meta: { title: '菜单管理' } },
      { path: 'system/dept', name: 'SysDept', component: () => import('@/views/system/Dept.vue'), meta: { title: '部门管理' } },
      { path: 'system/dict', name: 'SysDict', component: () => import('@/views/system/Dict.vue'), meta: { title: '数据字典' } },
      { path: 'system/log/login', name: 'SysLoginLog', component: () => import('@/views/system/LoginLog.vue'), meta: { title: '登录日志' } },
      { path: 'system/log/operation', name: 'SysOpLog', component: () => import('@/views/system/OpLog.vue'), meta: { title: '操作日志' } },
      { path: 'system/company', name: 'SysCompany', component: () => import('@/views/system/Company.vue'), meta: { title: '公司主体' } },
      { path: 'system/config', name: 'SysConfig', component: () => import('@/views/system/Config.vue'), meta: { title: '系统参数' } }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.public) return next()
  const token = localStorage.getItem('token')
  if (!token) return next('/login')
  next()
})

export default router
