# -*- coding: utf-8 -*-
"""从 MyBatis-Plus 实体类自动生成 CREATE TABLE 建表 SQL"""
import os, re, glob, sys

BASE = r"C:\Users\37848\WorkBuddy\2026-08-20-10-15-34\finance\finance-app\src\main\java"
OUT = r"C:\Users\37848\WorkBuddy\2026-08-20-10-15-34\finance\sql\schema.sql"

TYPE_MAP = {
    'Long': 'BIGINT', 'Integer': 'INT', 'int': 'INT',
    'String': 'VARCHAR(255)',
    'BigDecimal': 'DECIMAL(18,2)', 'Double': 'DECIMAL(18,4)', 'Float': 'DECIMAL(18,4)',
    'LocalDateTime': 'DATETIME', 'LocalDate': 'DATE', 'LocalTime': 'TIME',
    'Boolean': 'TINYINT(1)', 'byte[]': 'LONGBLOB', 'BigInteger': 'BIGINT',
    'Date': 'DATETIME',
}

TEXT_KEYS = ('content', 'remark', 'description', 'text', 'attachment', 'address',
             'reason', 'opinion', 'note', 'summary', 'detail', 'memo', 'path', 'url')
VARCHAR_LEN = {
    'username': 64, 'password': 100, 'phone': 32, 'email': 128, 'avatar': 255,
    'dept_name': 128, 'real_name': 64, 'company_name': 255, 'company_code': 64,
    'tax_no': 64, 'legal_person': 64, 'short_name': 128, 'ancestors': 255,
    'dict_label': 128, 'dict_value': 128, 'menu_name': 128, 'menu_path': 255,
    'component': 255, 'perms': 128, 'role_name': 128, 'role_key': 128,
    'config_name': 128, 'config_key': 128, 'config_value': 255,
    'item_name': 128, 'item_code': 64, 'subject_code': 64, 'subject_name': 255,
    'voucher_no': 64, 'bill_no': 64, 'invoice_no': 64, 'code': 64, 'name': 128,
    'category_name': 128, 'category_code': 64, 'warehouse_name': 128,
    'currency_code': 16, 'unit': 32, 'spec': 128, 'model': 128, 'serial_no': 64,
    'ip': 64, 'token': 255, 'user_agent': 255, 'browser': 64, 'os': 64,
    'status': 'TINYINT', 'type': 'VARCHAR(32)', 'deleted': 'TINYINT(1)',
}

def camel2snake(name):
    s = re.sub(r'(?<!^)(?=[A-Z])', '_', name).lower()
    return s

def parse_entity(path):
    """解析单个实体类，返回 (表名, [(列名, sql类型, 是否主键自增, 注释), ...])"""
    text = open(path, encoding='utf-8').read()
    m = re.search(r'@TableName\("([^"]+)"\)', text)
    if not m:
        return None
    table = m.group(1)

    # 找类体
    cm = re.search(r'public class \w+[^{]*\{', text)
    if not cm:
        return None
    body = text[cm.end():]
    # 截取到类结束（简单处理：找到最后一个 '}'）
    depth = 1
    end = 0
    for i, ch in enumerate(body):
        if ch == '{': depth += 1
        elif ch == '}':
            depth -= 1
            if depth == 0:
                end = i
                break
    body = body[:end]

    fields = []
    # 匹配字段块：注解 + private 声明
    pattern = re.compile(
        r'((?:@\w+(?:\([^)]*\))?\s*)+)?private\s+([\w<>\[\].]+)\s+(\w+)\s*(?:=\s*[^;]+)?;',
        re.S)
    for fm in pattern.finditer(body):
        annos, jtype, fname = fm.group(1) or '', fm.group(2), fm.group(3)
        if fname == 'serialVersionUID':
            continue
        if 'exist = false' in annos or 'exist=false' in annos.replace(' ', ''):
            continue
        # 主键
        is_pk = '@TableId' in annos
        auto = 'IdType.AUTO' in annos
        # 列名
        colm = re.search(r'@TableField\("([^"]+)"\)', annos)
        col = colm.group(1) if colm else camel2snake(fname)

        base_type = jtype.replace('java.lang.', '').strip()
        # 泛型剥离
        base_type = base_type.split('<')[0].strip()

        if is_pk:
            sql = 'BIGINT NOT NULL AUTO_INCREMENT COMMENT \'主键ID\''
            if not auto:
                sql = 'BIGINT NOT NULL COMMENT \'主键ID\''
            fields.append((col, sql, True))
            continue

        if base_type == 'String':
            if col in VARCHAR_LEN and isinstance(VARCHAR_LEN[col], int):
                sql = f'VARCHAR({VARCHAR_LEN[col]})'
            elif any(k in col for k in TEXT_KEYS):
                sql = 'TEXT'
            else:
                sql = 'VARCHAR(255)'
        elif base_type in TYPE_MAP:
            sql = TYPE_MAP[base_type]
            # 金额/数量精度
            if base_type == 'BigDecimal':
                if any(k in col for k in ('qty', 'quantity', 'num', 'count', 'rate', 'price', 'cost', 'unit_price')):
                    sql = 'DECIMAL(18,4)'
                else:
                    sql = 'DECIMAL(18,2)'
            elif col == 'status':
                sql = 'TINYINT'
        else:
            sql = 'VARCHAR(255)'  # 兜底

        if 'deleted' in col:
            sql = 'TINYINT(1) DEFAULT 0'

        # 默认值
        if col in ('create_time', 'update_time'):
            sql += ' DEFAULT CURRENT_TIMESTAMP'
        if col == 'create_time':
            pass
        if col == 'status':
            sql += ' DEFAULT 1'

        fields.append((col, sql, False))
    return (table, fields)

def main():
    files = []
    for root, dirs, fnames in os.walk(BASE):
        for f in fnames:
            if f.endswith('.java'):
                files.append(os.path.join(root, f))

    tables = []
    for f in files:
        r = parse_entity(f)
        if r:
            tables.append(r)

    # 按表名排序，避免重复
    seen = {}
    for t, fields in tables:
        seen[t] = fields

    lines = []
    lines.append('-- 自动生成建表 SQL（从实体类解析）')
    lines.append('SET NAMES utf8mb4;')
    lines.append('SET FOREIGN_KEY_CHECKS = 0;')
    lines.append('')
    for t in sorted(seen):
        fields = seen[t]
        lines.append(f'DROP TABLE IF EXISTS `{t}`;')
        lines.append(f'CREATE TABLE `{t}` (')
        cols = []
        pk_cols = [c for c, s, pk in fields if pk]
        for col, sql, is_pk in fields:
            cols.append(f'  `{col}` {sql}')
        # 主键约束
        if pk_cols:
            cols.append(f'  PRIMARY KEY (`{pk_cols[0]}`)')
        lines.append(',\n'.join(cols))
        lines.append(f') ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT=\'表 {t}\';')
        lines.append('')
    lines.append('SET FOREIGN_KEY_CHECKS = 1;')
    open(OUT, 'w', encoding='utf-8').write('\n'.join(lines))
    print(f'生成 {len(seen)} 张表 -> {OUT}')

if __name__ == '__main__':
    main()
