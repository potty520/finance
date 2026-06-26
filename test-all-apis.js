/**
 * 全量接口测试脚本
 * 登录 → 遍历 controller 端点 → 测试 GET/POST → 输出报告
 */
const http = require('http');
const fs = require('fs');
const path = require('path');

const BASE = process.env.API_BASE || 'http://127.0.0.1:18080';

function request(method, urlPath, body, token) {
  return new Promise((resolve) => {
    const data = body ? JSON.stringify(body) : null;
    const headers = { 'Content-Type': 'application/json' };
    if (data) headers['Content-Length'] = Buffer.byteLength(data);
    if (token) headers['Authorization'] = 'Bearer ' + token;
    const url = new URL(BASE + urlPath);
    const req = http.request(
      { hostname: url.hostname, port: url.port, path: url.pathname + url.search, method, headers },
      (res) => {
        let chunks = [];
        res.on('data', (c) => chunks.push(c));
        res.on('end', () => {
          const text = Buffer.concat(chunks).toString('utf8');
          let json = null;
          try { json = JSON.parse(text); } catch (e) {}
          resolve({ status: res.statusCode, body: text, json });
        });
      }
    );
    req.on('error', (e) => resolve({ status: 0, error: e.message }));
    if (data) req.write(data);
    req.end();
  });
}

// 待测试接口清单（按模块分类）
const modules = {
  '认证模块': [
    { method: 'POST', path: '/api/auth/login', body: { username: 'admin', password: '123456' }, isLogin: true, tryPasswords: ['123456', 'admin123'] },
    { method: 'GET', path: '/api/auth/me', auth: true, desc: '当前用户信息' },
  ],
  '系统-用户': [
    { method: 'GET', path: '/api/system/user/page?pageNum=1&pageSize=5', auth: true, desc: '用户分页' },
    { method: 'GET', path: '/api/system/user/list', auth: true, desc: '用户列表' },
    { method: 'GET', path: '/api/system/user/1', auth: true, desc: '用户详情' },
  ],
  '系统-角色': [
    { method: 'GET', path: '/api/system/role/list', auth: true, desc: '角色列表' },
    { method: 'GET', path: '/api/system/role/all', auth: true, desc: '全部角色' },
  ],
  '系统-菜单': [
    { method: 'GET', path: '/api/system/menu/list', auth: true, desc: '菜单列表' },
    { method: 'GET', path: '/api/system/menu/tree', auth: true, desc: '菜单树' },
    { method: 'GET', path: '/api/system/menu/userTree/1', auth: true, desc: '用户菜单树' },
  ],
  '系统-部门': [
    { method: 'GET', path: '/api/system/dept/list', auth: true, desc: '部门列表' },
    { method: 'GET', path: '/api/system/dept/tree', auth: true, desc: '部门树' },
  ],
  '系统-参数': [
    { method: 'GET', path: '/api/system/config/list', auth: true, desc: '系统参数' },
  ],
  '系统-公司': [
    { method: 'GET', path: '/api/system/company/list', auth: true, desc: '公司主体列表' },
  ],
  '系统-字典': [
    { method: 'GET', path: '/api/system/dict/type/list', auth: true, desc: '字典类型列表' },
    { method: 'GET', path: '/api/system/dict/data/list', auth: true, desc: '字典数据列表' },
  ],
  '系统-日志': [
    { method: 'GET', path: '/api/system/log/login/page?pageNum=1&pageSize=5', auth: true, desc: '登录日志' },
    { method: 'GET', path: '/api/system/log/operation/page?pageNum=1&pageSize=5', auth: true, desc: '操作日志' },
  ],
  '总账-科目': [
    { method: 'GET', path: '/api/ledger/subject/list', auth: true, desc: '科目列表' },
  ],
  '总账-辅助核算': [
    { method: 'GET', path: '/api/ledger/auxiliary/list', auth: true, desc: '辅助核算列表' },
  ],
  '总账-凭证': [
    { method: 'GET', path: '/api/ledger/voucher/page?pageNum=1&pageSize=5', auth: true, desc: '凭证分页' },
    { method: 'GET', path: '/api/ledger/voucher/list', auth: true, desc: '凭证列表' },
  ],
  '总账-账簿': [
    { method: 'GET', path: '/api/ledger/balance/list?fiscalYear=2026&fiscalPeriod=6', auth: true, desc: '余额表' },
  ],
  '总账-现金流量': [
    { method: 'GET', path: '/api/ledger/cashFlow/list', auth: true, desc: '现金流量' },
  ],
  '总账-会计期间': [
    { method: 'GET', path: '/api/ledger/period/list', auth: true, desc: '会计期间列表' },
    { method: 'GET', path: '/api/ledger/period/page?pageNum=1&pageSize=12', auth: true, desc: '会计期间分页' },
  ],
  '总账-科目树': [
    { method: 'GET', path: '/api/ledger/subject/tree', auth: true, desc: '科目树' },
  ],
  '出纳-账户': [
    { method: 'GET', path: '/api/cashier/account/list', auth: true, desc: '账户列表' },
  ],
  '出纳-日记账': [
    { method: 'GET', path: '/api/cashier/daily/page?pageNum=1&pageSize=5', auth: true, desc: '日记账分页' },
  ],
  '出纳-对账': [
    { method: 'GET', path: '/api/cashier/reconciliation/page?pageNum=1&pageSize=5', auth: true, desc: '对账分页' },
  ],
  '出纳-票据': [
    { method: 'GET', path: '/api/cashier/bill/page?pageNum=1&pageSize=5', auth: true, desc: '票据分页' },
  ],
  '应收': [
    { method: 'GET', path: '/api/receivable/invoice/page?pageNum=1&pageSize=5', auth: true, desc: '应收发票' },
    { method: 'GET', path: '/api/receivable/receipt/page?pageNum=1&pageSize=5', auth: true, desc: '收款单' },
    { method: 'GET', path: '/api/receivable/customer/list', auth: true, desc: '客户列表' },
  ],
  '应付': [
    { method: 'GET', path: '/api/payable/invoice/page?pageNum=1&pageSize=5', auth: true, desc: '应付发票' },
    { method: 'GET', path: '/api/payable/supplier/list', auth: true, desc: '供应商列表' },
    { method: 'GET', path: '/api/payable/payment/page?pageNum=1&pageSize=5', auth: true, desc: '付款单' },
  ],
  '存货': [
    { method: 'GET', path: '/api/stock/goods/list', auth: true, desc: '存货商品列表' },
    { method: 'GET', path: '/api/stock/goods/page?pageNum=1&pageSize=5', auth: true, desc: '存货商品分页' },
    { method: 'GET', path: '/api/stock/summary', auth: true, desc: '库存余额汇总' },
    { method: 'GET', path: '/api/stock/io/page?pageNum=1&pageSize=5', auth: true, desc: '出入库分页' },
  ],
  '固定资产': [
    { method: 'GET', path: '/api/asset/card/page?pageNum=1&pageSize=5', auth: true, desc: '资产卡片' },
    { method: 'GET', path: '/api/asset/category/list', auth: true, desc: '资产类别' },
    { method: 'GET', path: '/api/asset/depreciation/page?pageNum=1&pageSize=5', auth: true, desc: '折旧分页' },
    { method: 'GET', path: '/api/asset/report/byCategory', auth: true, desc: '资产分类汇总' },
  ],
  '合同': [
    { method: 'GET', path: '/api/contract/page?pageNum=1&pageSize=5', auth: true, desc: '合同分页' },
    { method: 'GET', path: '/api/contract/stats', auth: true, desc: '合同统计' },
  ],
  '项目': [
    { method: 'GET', path: '/api/project/page?pageNum=1&pageSize=5', auth: true, desc: '项目列表' },
    { method: 'GET', path: '/api/project/ledger', auth: true, desc: '项目台账' },
    { method: 'GET', path: '/api/project/budget/list', auth: true, desc: '项目预算' },
    { method: 'GET', path: '/api/project/stats', auth: true, desc: '项目统计' },
  ],
  '预算': [
    { method: 'GET', path: '/api/budget/page?pageNum=1&pageSize=5', auth: true, desc: '预算编制' },
    { method: 'GET', path: '/api/budget/analysis?fiscalYear=2026&fiscalPeriod=6', auth: true, desc: '预算分析' },
  ],
  '费用': [
    { method: 'GET', path: '/api/expense/apply/page?pageNum=1&pageSize=5', auth: true, desc: '费用申请' },
    { method: 'GET', path: '/api/expense/loan/list', auth: true, desc: '备用金' },
  ],
  '成本': [
    { method: 'GET', path: '/api/cost/center/list', auth: true, desc: '成本中心' },
    { method: 'GET', path: '/api/cost/item/list', auth: true, desc: '成本要素' },
    { method: 'GET', path: '/api/cost/allocation/page?pageNum=1&pageSize=5', auth: true, desc: '费用归集' },
    { method: 'GET', path: '/api/cost/allocation/summary?fiscalYear=2026&fiscalPeriod=6', auth: true, desc: '归集汇总' },
    { method: 'GET', path: '/api/cost/product/summary?fiscalYear=2026&fiscalPeriod=6', auth: true, desc: '成本计算' },
  ],
  '合并报表': [
    { method: 'GET', path: '/api/consol/group/list', auth: true, desc: '合并集团' },
    { method: 'GET', path: '/api/consol/offset/page?pageNum=1&pageSize=5', auth: true, desc: '抵消分录' },
    { method: 'GET', path: '/api/consol/analysis?fiscalYear=2026&fiscalPeriod=6', auth: true, desc: '合并分析' },
  ],
  '报表': [
    { method: 'POST', path: '/api/report/balanceSheet', body: { fiscalYear: '2026', fiscalPeriod: 6 }, auth: true, desc: '资产负债表' },
    { method: 'POST', path: '/api/report/incomeStatement', body: { fiscalYear: '2026', fiscalPeriod: 6 }, auth: true, desc: '利润表' },
    { method: 'POST', path: '/api/report/cashFlow', body: { fiscalYear: '2026', fiscalPeriod: 6 }, auth: true, desc: '现金流量表' },
    { method: 'GET', path: '/api/report/custom/list', auth: true, desc: '自定义报表' },
  ],
  '工作流': [
    { method: 'GET', path: '/api/workflow/instance/page?pageNum=1&pageSize=5', auth: true, desc: '流程分页' },
    { method: 'GET', path: '/api/workflow/myTasks?userId=1', auth: true, desc: '我的任务' },
  ],
};

(async () => {
  const results = [];
  let token = null;
  let loginPassword = '123456';
  const startTime = Date.now();

  for (const [module, endpoints] of Object.entries(modules)) {
    for (const ep of endpoints) {
      const t0 = Date.now();
      let r;
      if (ep.isLogin && ep.tryPasswords) {
        r = { status: 0, json: null };
        for (const pwd of ep.tryPasswords) {
          r = await request(ep.method, ep.path, { ...ep.body, password: pwd }, null);
          if (r.json && r.json.code === 200) { loginPassword = pwd; break; }
        }
      } else {
        const tok = ep.isLogin ? null : (ep.auth ? token : null);
        r = await request(ep.method, ep.path, ep.body, tok);
      }
      const ms = Date.now() - t0;
      let code = null, msg = '', count = null, sample = null;
      if (r.error) msg = r.error;
      if (r.json) {
        code = r.json.code;
        msg = r.json.message || msg || '';
        if (r.json.data) {
          if (Array.isArray(r.json.data)) {
            count = r.json.data.length;
            sample = r.json.data[0] ? Object.keys(r.json.data[0]).slice(0, 4) : [];
          } else if (r.json.data.records) {
            count = r.json.data.records.length;
            sample = r.json.data.records[0] ? Object.keys(r.json.data.records[0]).slice(0, 4) : [];
          } else if (r.json.data.total !== undefined) {
            count = r.json.data.total;
          } else {
            sample = Object.keys(r.json.data).slice(0, 4);
          }
        }
      }
      const status = ep.isLogin ? (r.json && r.json.code === 200) : (code === 200);
      if (ep.isLogin && r.json && r.json.data) token = r.json.data.token;
      results.push({
        module, desc: ep.desc, method: ep.method, path: ep.path,
        http: r.status, code, msg, count, sample, ms, status
      });
      const flag = status ? 'OK' : 'FAIL';
      process.stdout.write(`  [${flag}] ${ep.method} ${ep.path}  ${code||'-'}  ${count!==null?count+'条':''}  ${ms}ms\n`);
    }
    process.stdout.write('\n');
  }

  const total = results.length;
  const ok = results.filter(r => r.status).length;
  const fail = total - ok;
  const dur = ((Date.now() - startTime) / 1000).toFixed(1);

  // 生成 Markdown 报告
  const today = new Date().toISOString().slice(0, 10);
  let md = `# 财务系统全量接口测试报告\n\n`;
  md += `**测试时间**：${today}\n`;
  md += `**测试地址**：${BASE}\n`;
  md += `**测试账号**：admin / ${loginPassword}\n`;
  md += `**接口总数**：${total}  |  **通过**：${ok}  |  **失败**：${fail}  |  **耗时**：${dur}s\n\n`;
  md += `**通过率**：${((ok / total) * 100).toFixed(1)}%\n\n`;
  md += `---\n\n`;

  let curModule = '';
  for (const r of results) {
    if (r.module !== curModule) {
      md += `\n## ${r.module}\n\n`;
      md += `| # | 方法 | 路径 | 说明 | HTTP | 业务码 | 数据量 | 耗时 | 状态 |\n`;
      md += `|---|------|------|------|------|--------|--------|------|------|\n`;
      curModule = r.module;
    }
    const flag = r.status ? '✅' : '❌';
    md += `| - | ${r.method} | \`${r.path}\` | ${r.desc||''} | ${r.http} | ${r.code ?? '-'} | ${r.count !== null ? r.count : '-'} | ${r.ms}ms | ${flag} |\n`;
  }

  md += `\n---\n\n## 失败详情\n\n`;
  const fails = results.filter(r => !r.status);
  if (fails.length === 0) {
    md += `🎉 全部通过，无失败接口。\n\n`;
  } else {
    md += `| 模块 | 接口 | 错误信息 |\n|------|------|----------|\n`;
    for (const f of fails) {
      md += `| ${f.module} | \`${f.method} ${f.path}\` | HTTP ${f.http}, code=${f.code}, msg=${f.msg} |\n`;
    }
  }

  md += `\n---\n\n## 模块覆盖率\n\n`;
  md += `| 模块 | 接口数 | 通过 | 失败 |\n|------|--------|------|------|\n`;
  const moduleStat = {};
  for (const r of results) {
    if (!moduleStat[r.module]) moduleStat[r.module] = { total: 0, ok: 0 };
    moduleStat[r.module].total++;
    if (r.status) moduleStat[r.module].ok++;
  }
  for (const [m, s] of Object.entries(moduleStat)) {
    md += `| ${m} | ${s.total} | ${s.ok} | ${s.total - s.ok} |\n`;
  }

  md += `\n---\n\n## 测试环境\n\n`;
  md += `- 后端：Spring Boot 2.7.18 (Java 1.8)\n`;
  md += `- 数据库：MySQL @ 103.236.96.82:13306 (finance_db)\n\n`;

  md += `## 500 接口专项\n\n`;
  const err500 = fails.filter(f => f.code === 500 || f.http === 500);
  if (err500.length === 0) {
    md += `无 500 错误接口。\n\n`;
  } else {
    md += `| 模块 | 接口 | 错误信息 |\n|------|------|----------|\n`;
    for (const f of err500) {
      md += `| ${f.module} | \`${f.method} ${f.path}\` | ${f.msg || '系统繁忙'} |\n`;
    }
    md += `\n`;
  }

  md += `## 404 接口专项\n\n`;
  const err404 = fails.filter(f => f.http === 404 || (!f.code && f.http !== 200));
  if (err404.length === 0) {
    md += `无 404 错误接口。\n\n`;
  } else {
    md += `| 模块 | 接口 |\n|------|------|\n`;
    for (const f of err404) {
      md += `| ${f.module} | \`${f.method} ${f.path}\` |\n`;
    }
    md += `\n`;
  }

  // 按失败原因分类
  const categories = { '404/URL 错误': [], '实体与 DB 字段不匹配 (500)': [], '权限不足 (403)': [], '其他 (500)': [], '参数错误 (400)': [] };
  for (const f of fails) {
    if (f.code === 500) categories['实体与 DB 字段不匹配 (500)'].push(f);
    else if (f.code === 403) categories['权限不足 (403)'].push(f);
    else if (f.code === 400) categories['参数错误 (400)'].push(f);
    else if (!f.code) categories['404/URL 错误'].push(f);
    else categories['其他 (500)'].push(f);
  }
  md += `## 失败原因分类\n\n`;
  md += `| 类型 | 数量 | 占比 |\n|------|------|------|\n`;
  for (const [cat, arr] of Object.entries(categories)) {
    if (arr.length === 0) continue;
    md += `| ${cat} | ${arr.length} | ${fail ? ((arr.length/fail.length)*100).toFixed(1) : 0}% |\n`;
  }

  const outPath = path.join(__dirname, `接口测试报告-${today}.md`);
  fs.writeFileSync(outPath, md, 'utf8');
  console.log(`\n报告已生成: ${outPath}`);
  console.log(`汇总: 总数=${total} 通过=${ok} 失败=${fail} 通过率=${((ok/total)*100).toFixed(1)}%`);
})();
