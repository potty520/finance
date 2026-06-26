# SQL Scripts

## 文件说明
- `00-full-database.sql` — 全量数据库导出（含结构和数据），用于全新部署
- `25-upgrade-data.sql` — 增量升级数据（sys_menu/sys_role_menu/gl_account_subject/凭证数据等）

## 导入方式

### 全新部署
```bash
mysql -h <host> -u <user> -p finance_db < 00-full-database.sql
```

### 增量升级
```bash
mysql -h <host> -u <user> -p finance_db < 25-upgrade-data.sql
```

## 变更记录
- 25-upgrade: 增值税子科目、sys_menu 权限扩充(148条)、品牌升级、国标报表科目
- 24-contract: 合同附件表 (见 .bak/)
