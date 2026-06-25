# 智能财务管理系统

> 企业级一体化财务管理平台，覆盖 16 大业务模块，支持多组织、多账套、全流程数字化。

## 业务模块

| 模块 | 子功能 |
|------|------|
| 总账 | 凭证管理、科目体系、会计期间、账簿查询 |
| 出纳 | 出纳日记账、银行对账 |
| 应收/应付 | 应收账款/应付账款、销售/采购发票 |
| 资产 | 固定资产卡片、折旧管理、处置 |
| 存货 | 物料档案、出入库、收发存汇总（加权平均成本） |
| 成本 | 成本中心、费用归集、品种法核算 |
| 预算 | 预算编制、审核、下达、执行分析 |
| 合并报表 | 集团架构、合并抵消、合并底稿 |
| 审批流 | 流程定义、多级审批、待办与记录 |
| 财务报表 | 资产负债表、利润表、现金流量表、自定义报表 |
| 费用 | 费用申请、借款与还款 |
| 合同/项目 | 合同管理、项目管理、项目台账 |
| 系统 | 用户、角色、菜单、字典、参数、公司主体 |

## 技术栈

**后端：** Spring Boot 2.7.x、MyBatis-Plus、MySQL 8.x、JWT、Spring Security、Maven、Java 8

**前端：** Vue 3、Vite、Element Plus、Vue Router、Pinia、ECharts

## 项目结构

```
财务/
├── finance-app/                  # 后端 Spring Boot 工程
│   ├── src/main/java/com/finance/
│   │   ├── common/               # 公共组件（响应、分页、异常、JWT、Security）
│   │   ├── module/
│   │   │   ├── system/           # 系统模块
│   │   │   ├── ledger/           # 总账
│   │   │   ├── cashier/          # 出纳
│   │   │   ├── receivable/       # 应收
│   │   │   ├── payable/          # 应付
│   │   │   ├── asset/            # 资产
│   │   │   ├── stock/            # 存货
│   │   │   ├── cost/             # 成本
│   │   │   ├── budget/           # 预算
│   │   │   ├── consol/           # 合并报表
│   │   │   ├── workflow/         # 审批流
│   │   │   ├── report/           # 财务报表
│   │   │   ├── expense/          # 费用
│   │   │   ├── contract/         # 合同
│   │   │   └── project/          # 项目
│   │   └── FinanceApplication.java
│   └── src/main/resources/
│       ├── application.yml
│       └── mapper/               # MyBatis XML
│
├── finance-web/                  # 前端 Vue 3 工程
│   ├── src/
│   │   ├── api/                  # 接口封装
│   │   ├── views/                # 业务页面
│   │   ├── layout/               # 主布局
│   │   ├── router/               # 路由
│   │   ├── store/                # Pinia 状态
│   │   └── utils/                # 工具
│   ├── vite.config.js
│   └── package.json
│
└── db/                           # 数据库脚本
    ├── 00-main.sql               # 主入口
    ├── 01-system.sql
    ├── 02-ledger.sql
    ├── 03-cashier.sql
    ├── 04-receivable.sql
    ├── 05-payable.sql
    ├── 06-asset.sql
    ├── 07-stock.sql
    ├── 08-cost.sql
    ├── 09-budget.sql
    ├── 10-consol.sql
    ├── 11-workflow.sql
    ├── 12-report.sql
    ├── 13-expense.sql
    ├── 14-contract.sql
    └── 15-project.sql
```

## 快速开始

### 1. 数据库初始化

```bash
mysql -u root -p < db/00-main.sql
```

依次执行 `db/` 目录下 `00-main.sql` 至 `15-project.sql`。

### 2. 启动后端

```bash
cd finance-app
mvn spring-boot:run
```

后端默认端口 `8080`。

### 3. 启动前端

```bash
cd finance-web
npm install
npm run dev
```

前端默认端口 `5173`，浏览器访问 `http://localhost:5173`。

### 4. 默认账号

| 账号 | 密码 | 角色 |
|------|------|------|
| admin | 123456 | 系统管理员 |

## 核心特性

- **多模块架构**：16 个财务业务模块独立部署，模块间通过标准 RESTful API 解耦
- **统一认证**：基于 JWT 的无状态鉴权
- **审批流引擎**：支持多级审批、驳回、转交
- **预算控制**：实时扣减、释放、超额预警
- **加权平均成本**：存货出库自动重算成本价
- **折旧算法**：平均年限法、工作量法
- **多维报表**：三张主表 + 自定义报表
- **合并抵消**：内部交易、往来、存货多维度抵消

## 开发规范

- 后端遵循阿里 Java 规范，统一异常处理、参数校验、日志格式
- 前端遵循 Vue 3 Composition API + Element Plus 设计语言
- 提交前执行 `mvn clean compile` 和 `npm run build`

## License

MIT
