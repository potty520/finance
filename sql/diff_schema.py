# -*- coding: utf-8 -*-
"""对比 data.sql INSERT 列与 schema.sql 建表列，生成缺失列的 ALTER TABLE 语句"""
import re

DATA = r"C:\Users\37848\WorkBuddy\2026-08-20-10-15-34\finance\sql\data.sql"
SCHEMA = r"C:\Users\37848\WorkBuddy\2026-08-20-10-15-34\finance\sql\schema.sql"
OUT = r"C:\Users\37848\WorkBuddy\2026-08-20-10-15-34\finance\sql\fix_columns.sql"

def guess_type(col):
    if col in ('id',):
        return 'BIGINT'
    if col.endswith('_id'):
        return 'BIGINT'
    if col in ('deleted', 'status_flag', 'is_enabled', 'enabled'):
        return 'TINYINT(1) DEFAULT 0' if 'deleted' in col else 'TINYINT DEFAULT 1'
    if any(k in col for k in ('qty', 'quantity', 'num', 'count', 'rate', 'weight')):
        return 'DECIMAL(18,4)'
    if any(k in col for k in ('amount', 'price', 'cost', 'total', 'fee', 'money', 'balance', 'tax', 'premium', 'debit', 'credit', 'interest', 'principal', 'discount', 'value')):
        return 'DECIMAL(18,2)'
    if col.endswith('_date') or col in ('sign_date', 'start_date', 'end_date', 'birth_date', 'settle_date', 'due_date', 'trans_date', 'voucher_date'):
        return 'DATE'
    if col.endswith('_time') or col in ('create_time', 'update_time', 'approve_time', 'audit_time', 'post_time', 'pay_time', 'receive_time', 'last_login', 'operate_time'):
        return 'DATETIME DEFAULT CURRENT_TIMESTAMP' if col in ('create_time', 'update_time') else 'DATETIME'
    if col in ('status', 'type', 'direction', 'balance_direction', 'trans_type', 'source', 'asset_type', 'depreciation_method', 'pay_type', 'user_type', 'gender', 'period_code', 'currency_code', 'unit', 'voucher_no', 'bill_no', 'invoice_no', 'contract_no', 'code', 'category_code', 'subject_code', 'item_code', 'type_code', 'group_code', 'dict_type', 'dict_label', 'dict_value', 'role_key', 'menu_type', 'perms', 'remark'):
        return 'VARCHAR(255)'
    return 'VARCHAR(255)'

# 解析 data.sql 各表 INSERT 列
data_cols = {}  # 表名 -> set(列)
for line in open(DATA, encoding='utf-8'):
    m = re.match(r'INSERT INTO `(\w+)` \(([^)]+)\) VALUES', line)
    if m:
        tbl = m.group(1)
        cols = [c.strip().strip('`') for c in m.group(2).split(',')]
        data_cols.setdefault(tbl, set()).update(cols)

# 解析 schema.sql 各表列
schema_cols = {}  # 表名 -> set(列)
cur_tbl = None
for line in open(SCHEMA, encoding='utf-8'):
    m = re.match(r'CREATE TABLE `(\w+)`', line)
    if m:
        cur_tbl = m.group(1)
        schema_cols.setdefault(cur_tbl, set())
        continue
    if cur_tbl:
        cm = re.match(r'^\s*`(\w+)`', line)
        if cm:
            schema_cols[cur_tbl].add(cm.group(1))
        elif line.strip().startswith(')'):
            cur_tbl = None

# 对比
missing = {}
for tbl, cols in data_cols.items():
    if tbl not in schema_cols:
        print(f'!! 表 {tbl} 在 schema 中不存在')
        continue
    diff = cols - schema_cols[tbl]
    if diff:
        missing[tbl] = sorted(diff)

lines = []
lines.append('-- 自动生成的缺失列补充语句')
for tbl in sorted(missing):
    for col in missing[tbl]:
        t = guess_type(col)
        lines.append(f'ALTER TABLE `{tbl}` ADD COLUMN `{col}` {t} COMMENT \'补充列\';')
        print(f'{tbl}: +{col} {t}')

open(OUT, 'w', encoding='utf-8').write('\n'.join(lines) + '\n')
print(f'\n共 {len(missing)} 张表需要补列 -> {OUT}')
