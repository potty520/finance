-- ============================================================
-- 26-audit-fixes.sql  代码审计整改配套迁移
-- 可重复执行（幂等）：所有 DDL 均先查 information_schema 再执行
-- 适用库：finance_db
-- ============================================================

DROP PROCEDURE IF EXISTS qz_add_column;
DROP PROCEDURE IF EXISTS qz_add_index;

DELIMITER $$
CREATE PROCEDURE qz_add_column(IN tbl VARCHAR(64), IN col VARCHAR(64), IN ddl TEXT)
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.TABLES
             WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = tbl)
     AND NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS
                 WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = tbl AND COLUMN_NAME = col) THEN
    SET @s = CONCAT('ALTER TABLE `', tbl, '` ADD COLUMN ', ddl);
    PREPARE st FROM @s; EXECUTE st; DEALLOCATE PREPARE st;
  END IF;
END$$

CREATE PROCEDURE qz_add_index(IN tbl VARCHAR(64), IN idx VARCHAR(64), IN ddl TEXT)
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.TABLES
             WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = tbl)
     AND NOT EXISTS (SELECT 1 FROM information_schema.STATISTICS
                 WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = tbl AND INDEX_NAME = idx) THEN
    SET @s = CONCAT('ALTER TABLE `', tbl, '` ADD ', ddl);
    PREPARE st FROM @s; EXECUTE st; DEALLOCATE PREPARE st;
  END IF;
END$$

CREATE PROCEDURE qz_seed_config(IN ckey VARCHAR(64), IN cname VARCHAR(64), IN cval VARCHAR(255), IN ctype VARCHAR(32), IN cremark VARCHAR(255))
BEGIN
  IF NOT EXISTS (SELECT 1 FROM sys_config WHERE config_key = ckey AND deleted = 0) THEN
    INSERT INTO sys_config (config_name, config_key, config_value, config_type, remark, create_time, update_time, deleted)
    VALUES (cname, ckey, cval, ctype, cremark, NOW(), NOW(), 0);
  END IF;
END$$
DELIMITER ;

-- ---------- 1. 期间关账留痕字段 ----------
CALL qz_add_column('gl_period', 'closer',      '`closer` BIGINT NULL COMMENT ''关账人ID''');
CALL qz_add_column('gl_period', 'closer_name', '`closer_name` VARCHAR(64) NULL COMMENT ''关账人姓名''');
CALL qz_add_column('gl_period', 'close_time',  '`close_time` DATETIME NULL COMMENT ''关账时间''');

-- ---------- 2. 业财一体：业务单据回写凭证号 ----------
CALL qz_add_column('ar_invoice',    'voucher_id', '`voucher_id` BIGINT NULL COMMENT ''生成凭证ID''');
CALL qz_add_column('ar_invoice',    'voucher_no', '`voucher_no` VARCHAR(32) NULL COMMENT ''生成凭证号''');
CALL qz_add_column('ap_invoice',    'voucher_id', '`voucher_id` BIGINT NULL COMMENT ''生成凭证ID''');
CALL qz_add_column('ap_invoice',    'voucher_no', '`voucher_no` VARCHAR(32) NULL COMMENT ''生成凭证号''');
CALL qz_add_column('ap_payment',    'voucher_no', '`voucher_no` VARCHAR(32) NULL COMMENT ''生成凭证号''');
CALL qz_add_column('exp_application','voucher_id', '`voucher_id` BIGINT NULL COMMENT ''生成凭证ID''');
CALL qz_add_column('exp_application','voucher_no', '`voucher_no` VARCHAR(32) NULL COMMENT ''生成凭证号''');

-- ---------- 3. 查询性能索引 ----------
CALL qz_add_index('gl_voucher',          'idx_voucher_period_status',  'INDEX `idx_voucher_period_status` (`period_code`, `status`)');
CALL qz_add_index('gl_voucher',          'idx_voucher_source',         'INDEX `idx_voucher_source` (`source`, `source_id`)');
CALL qz_add_index('gl_voucher_entry',    'idx_entry_voucher',          'INDEX `idx_entry_voucher` (`voucher_id`)');
CALL qz_add_index('gl_voucher_entry',    'idx_entry_subject',          'INDEX `idx_entry_subject` (`subject_code`)');
CALL qz_add_index('gl_balance',          'idx_balance_period_subject', 'INDEX `idx_balance_period_subject` (`period_code`, `subject_code`)');
CALL qz_add_index('gl_balance',          'idx_balance_subject',        'INDEX `idx_balance_subject` (`subject_code`)');
CALL qz_add_index('ar_invoice',          'idx_ar_invoice_customer',    'INDEX `idx_ar_invoice_customer` (`customer_id`)');
CALL qz_add_index('ar_receipt',          'idx_ar_receipt_customer',    'INDEX `idx_ar_receipt_customer` (`customer_id`)');
CALL qz_add_index('ap_invoice',          'idx_ap_invoice_supplier',    'INDEX `idx_ap_invoice_supplier` (`supplier_id`)');
CALL qz_add_index('ap_payment',          'idx_ap_payment_supplier',    'INDEX `idx_ap_payment_supplier` (`supplier_id`)');
CALL qz_add_index('wf_approval_record',  'idx_wf_record_assignee',     'INDEX `idx_wf_record_assignee` (`assignee`, `status`)');
CALL qz_add_index('wf_approval_record',  'idx_wf_record_instance',     'INDEX `idx_wf_record_instance` (`instance_id`)');
CALL qz_add_index('wf_approval_config',  'idx_wf_config_business',     'INDEX `idx_wf_config_business` (`business_type`, `business_id`)');
CALL qz_add_index('sys_user',            'idx_sys_user_username',      'INDEX `idx_sys_user_username` (`username`)');
CALL qz_add_index('exp_application',     'idx_exp_app_status',         'INDEX `idx_exp_app_status` (`status`)');

-- ---------- 4. 系统参数：凭证职责分离开关 ----------
CALL qz_seed_config('gl.voucher.separate.duty', '凭证职责分离', '1', 'gl',
                    '1=审核人不能与制单人相同（默认）；0=允许同一人制单并审核');

-- ---------- 5. 系统参数：业务单据生成凭证的默认科目 ----------
CALL qz_seed_config('gl.biz.ar.control',    '应收往来科目',   '1122',     'gl', '销售发票借方挂账科目');
CALL qz_seed_config('gl.biz.ap.control',    '应付往来科目',   '2202',     'gl', '采购发票贷方挂账科目');
CALL qz_seed_config('gl.biz.ar.revenue',    '主营业务收入科目','6001',    'gl', '销售发票（类型1）默认收入科目');
CALL qz_seed_config('gl.biz.ar.other',      '其他业务收入科目','6051',    'gl', '其他应收（类型2）默认收入科目');
CALL qz_seed_config('gl.biz.ap.purchase',   '采购入库科目',   '1405',     'gl', '采购发票（类型1）默认借方科目');
CALL qz_seed_config('gl.biz.ap.expense',    '费用科目',       '6602',     'gl', '费用发票（类型2/3）默认借方科目');
CALL qz_seed_config('gl.biz.tax.output',    '销项税额科目',   '22210105', 'gl', '销售发票贷方税额科目');
CALL qz_seed_config('gl.biz.tax.input',     '进项税额科目',   '22210101', 'gl', '采购发票借方税额科目');
CALL qz_seed_config('gl.biz.cash',          '库存现金科目',   '1001',     'gl', '现金账户收付款默认科目');
CALL qz_seed_config('gl.biz.bank',          '银行存款科目',   '1002',     'gl', '银行账户收付款默认科目');
CALL qz_seed_config('gl.biz.expense',       '报销费用科目',   '6602',     'gl', '费用报销单未指定科目时的默认借方科目');
CALL qz_seed_config('gl.biz.expense.payable','应付报销款科目','2241',    'gl', '报销单已审批未付款时的贷方科目（其他应付款）');

-- ---------- 6. 审批人配置：费用报销默认由管理员审批 ----------
INSERT INTO wf_approval_config (doc_type, doc_type_name, approver_ids, approve_mode, status, remark, create_time, update_time, deleted)
SELECT 'expense', '费用报销审批', '1', 'SEQ', 1,
       '顺序审批；approver_ids 为 sys_user.id，多个用英文逗号分隔', NOW(), NOW(), 0
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM wf_approval_config WHERE flow_code IS NULL AND doc_type = 'expense' AND deleted = 0);

-- ---------- 7. 存量数据修正 ----------
-- 7.1 历史流程实例状态口径统一为 0=审批中 1=已通过 2=已驳回（仅流程实例行，配置行 flow_code 为空）
UPDATE wf_approval_config SET status = 0, update_time = NOW()
WHERE flow_code IS NOT NULL AND status NOT IN (0, 1, 2);

-- 7.2 审批已结束但单据状态仍停留在"待审"的费用申请，按流程结果回写
UPDATE exp_application a
JOIN wf_approval_config i ON i.business_type = 'expense' AND i.business_id = a.id
     AND i.flow_code IS NOT NULL AND i.deleted = 0
SET a.status = CASE WHEN i.status = 1 THEN '1' WHEN i.status = 2 THEN '2' ELSE a.status END,
    a.update_time = NOW()
WHERE a.deleted = 0 AND a.status = '0' AND i.status IN (1, 2);

-- 7.3 价税合计为空或与"金额+税额"不一致的发票，按明细金额校正
UPDATE ar_invoice
SET total_amount = IFNULL(amount,0) + IFNULL(tax_amount,0), update_time = NOW()
WHERE deleted = 0 AND (total_amount IS NULL
      OR total_amount <> IFNULL(amount,0) + IFNULL(tax_amount,0));

UPDATE ap_invoice
SET total_amount = IFNULL(amount,0) + IFNULL(tax_amount,0), update_time = NOW()
WHERE deleted = 0 AND (total_amount IS NULL
      OR total_amount <> IFNULL(amount,0) + IFNULL(tax_amount,0));

-- 7.4 未收/未付金额按已收付金额重算，避免历史脏数据
UPDATE ar_invoice SET collected_amount = IFNULL(collected_amount,0),
    uncollected_amount = total_amount - IFNULL(collected_amount,0), update_time = NOW()
WHERE deleted = 0 AND (uncollected_amount IS NULL OR uncollected_amount <> total_amount - IFNULL(collected_amount,0));

UPDATE ap_invoice SET paid_amount = IFNULL(paid_amount,0),
    unpaid_amount = total_amount - IFNULL(paid_amount,0), update_time = NOW()
WHERE deleted = 0 AND (unpaid_amount IS NULL OR unpaid_amount <> total_amount - IFNULL(paid_amount,0));

DROP PROCEDURE IF EXISTS qz_add_column;
DROP PROCEDURE IF EXISTS qz_add_index;
DROP PROCEDURE IF EXISTS qz_seed_config;

-- 迁移完成后建议调用 POST /api/ledger/balance/rebuild 重建总账余额
SELECT '26-audit-fixes applied' AS result;
