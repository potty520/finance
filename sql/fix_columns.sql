-- 自动生成的缺失列补充语句（类型已人工修正）
ALTER TABLE `ctr_sales_contract` ADD COLUMN `invoiced_amount` DECIMAL(18,2) DEFAULT 0 COMMENT '已开票金额';
ALTER TABLE `ctr_sales_contract` ADD COLUMN `received_amount` DECIMAL(18,2) DEFAULT 0 COMMENT '已收款金额';

ALTER TABLE `gl_account_subject` ADD COLUMN `aux_customer` TINYINT(1) DEFAULT 0 COMMENT '客户辅助核算';
ALTER TABLE `gl_account_subject` ADD COLUMN `aux_supplier` TINYINT(1) DEFAULT 0 COMMENT '供应商辅助核算';
ALTER TABLE `gl_account_subject` ADD COLUMN `aux_dept` TINYINT(1) DEFAULT 0 COMMENT '部门辅助核算';
ALTER TABLE `gl_account_subject` ADD COLUMN `aux_project` TINYINT(1) DEFAULT 0 COMMENT '项目辅助核算';
ALTER TABLE `gl_account_subject` ADD COLUMN `aux_employee` TINYINT(1) DEFAULT 0 COMMENT '员工辅助核算';
ALTER TABLE `gl_account_subject` ADD COLUMN `aux_product` TINYINT(1) DEFAULT 0 COMMENT '存货辅助核算';
ALTER TABLE `gl_account_subject` ADD COLUMN `quantity_unit` VARCHAR(32) DEFAULT NULL COMMENT '计量单位';
ALTER TABLE `gl_account_subject` ADD COLUMN `is_cash_flow` TINYINT(1) DEFAULT 0 COMMENT '是否现金流量项目';
ALTER TABLE `gl_account_subject` ADD COLUMN `cash_flow_dir` VARCHAR(16) DEFAULT NULL COMMENT '现金流量方向';
ALTER TABLE `gl_account_subject` ADD COLUMN `is_system` TINYINT(1) DEFAULT 0 COMMENT '系统内置';

ALTER TABLE `gl_voucher_entry` ADD COLUMN `aux_customer_id` BIGINT DEFAULT NULL COMMENT '客户辅助ID';
ALTER TABLE `gl_voucher_entry` ADD COLUMN `aux_dept_id` BIGINT DEFAULT NULL COMMENT '部门辅助ID';
ALTER TABLE `gl_voucher_entry` ADD COLUMN `aux_project_id` BIGINT DEFAULT NULL COMMENT '项目辅助ID';
ALTER TABLE `gl_voucher_entry` ADD COLUMN `aux_supplier_id` BIGINT DEFAULT NULL COMMENT '供应商辅助ID';
