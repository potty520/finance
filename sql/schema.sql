-- 自动生成建表 SQL（从实体类解析）
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `ap_invoice`;
CREATE TABLE `ap_invoice` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` VARCHAR(64),
  `invoice_type` VARCHAR(255),
  `invoice_date` DATE,
  `supplier_id` BIGINT,
  `supplier_name` VARCHAR(255),
  `tax_no` VARCHAR(64),
  `tax_rate` VARCHAR(255),
  `amount` DECIMAL(18,2),
  `tax_amount` DECIMAL(18,2),
  `total_amount` DECIMAL(18,2),
  `paid_amount` DECIMAL(18,2),
  `unpaid_amount` DECIMAL(18,2),
  `currency_code` VARCHAR(16),
  `exchange_rate` DECIMAL(18,4),
  `subject_code` VARCHAR(64),
  `due_date` DATE,
  `contract_no` VARCHAR(255),
  `purchase_order_no` VARCHAR(255),
  `remark` TEXT,
  `status` VARCHAR(255) DEFAULT 1,
  `creator` BIGINT,
  `creator_name` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 ap_invoice';

DROP TABLE IF EXISTS `ap_payment`;
CREATE TABLE `ap_payment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `payment_no` VARCHAR(255),
  `supplier_id` BIGINT,
  `payment_date` DATE,
  `amount` DECIMAL(18,2),
  `settled_amount` DECIMAL(18,2),
  `account_id` BIGINT,
  `payment_type` VARCHAR(255),
  `bill_id` BIGINT,
  `voucher_id` BIGINT,
  `status` VARCHAR(255) DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 ap_payment';

DROP TABLE IF EXISTS `ap_supplier`;
CREATE TABLE `ap_supplier` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `supplier_code` VARCHAR(255),
  `supplier_name` VARCHAR(255),
  `supplier_type` VARCHAR(255),
  `tax_no` VARCHAR(64),
  `bank_name` VARCHAR(255),
  `bank_account` VARCHAR(255),
  `contact` VARCHAR(255),
  `phone` VARCHAR(32),
  `address` TEXT,
  `subject_code` VARCHAR(64),
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 ap_supplier';

DROP TABLE IF EXISTS `ap_writeoff`;
CREATE TABLE `ap_writeoff` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `payment_id` BIGINT,
  `payment_no` VARCHAR(255),
  `invoice_id` BIGINT,
  `invoice_no` VARCHAR(64),
  `writeoff_amount` DECIMAL(18,2),
  `remark` TEXT,
  `operator` BIGINT,
  `operator_name` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 ap_writeoff';

DROP TABLE IF EXISTS `ar_customer`;
CREATE TABLE `ar_customer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_code` VARCHAR(255),
  `customer_name` VARCHAR(255),
  `customer_type` VARCHAR(255),
  `tax_no` VARCHAR(64),
  `bank_name` VARCHAR(255),
  `bank_account` VARCHAR(255),
  `contact` VARCHAR(255),
  `phone` VARCHAR(32),
  `address` TEXT,
  `credit_limit` DECIMAL(18,2),
  `credit_days` INT,
  `subject_code` VARCHAR(64),
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 ar_customer';

DROP TABLE IF EXISTS `ar_invoice`;
CREATE TABLE `ar_invoice` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` VARCHAR(64),
  `invoice_type` VARCHAR(255),
  `invoice_date` DATE,
  `customer_id` BIGINT,
  `customer_name` VARCHAR(255),
  `tax_no` VARCHAR(64),
  `tax_rate` VARCHAR(255),
  `amount` DECIMAL(18,2),
  `tax_amount` DECIMAL(18,2),
  `total_amount` DECIMAL(18,2),
  `collected_amount` DECIMAL(18,2),
  `uncollected_amount` DECIMAL(18,2),
  `currency_code` VARCHAR(16),
  `exchange_rate` DECIMAL(18,4),
  `subject_code` VARCHAR(64),
  `due_date` DATE,
  `contract_no` VARCHAR(255),
  `sales_order_id` BIGINT,
  `sales_order_no` VARCHAR(255),
  `remark` TEXT,
  `status` VARCHAR(255) DEFAULT 1,
  `creator` BIGINT,
  `creator_name` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 ar_invoice';

DROP TABLE IF EXISTS `ar_receipt`;
CREATE TABLE `ar_receipt` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` VARCHAR(64),
  `receipt_date` DATE,
  `customer_id` BIGINT,
  `customer_name` VARCHAR(255),
  `receipt_type` VARCHAR(255),
  `account_id` BIGINT,
  `account_name` VARCHAR(255),
  `amount` DECIMAL(18,2),
  `currency_code` VARCHAR(16),
  `exchange_rate` DECIMAL(18,4),
  `applied_amount` DECIMAL(18,2),
  `unapplied_amount` DECIMAL(18,2),
  `subject_code` VARCHAR(64),
  `voucher_no` VARCHAR(64),
  `voucher_id` BIGINT,
  `remark` TEXT,
  `status` VARCHAR(255) DEFAULT 1,
  `creator` BIGINT,
  `creator_name` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 ar_receipt';

DROP TABLE IF EXISTS `ar_writeoff`;
CREATE TABLE `ar_writeoff` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `receipt_id` BIGINT,
  `receipt_no` VARCHAR(255),
  `invoice_id` BIGINT,
  `invoice_no` VARCHAR(64),
  `writeoff_amount` DECIMAL(18,2),
  `remark` TEXT,
  `operator` BIGINT,
  `operator_name` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 ar_writeoff';

DROP TABLE IF EXISTS `budget_adjustment`;
CREATE TABLE `budget_adjustment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `budget_id` BIGINT,
  `source_module` VARCHAR(255),
  `source_bill_no` VARCHAR(255),
  `source_bill_id` VARCHAR(255),
  `amount` DECIMAL(18,2),
  `fiscal_year` VARCHAR(255),
  `fiscal_period` INT,
  `remark` TEXT,
  `operator` BIGINT,
  `operator_name` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 budget_adjustment';

DROP TABLE IF EXISTS `budget_master`;
CREATE TABLE `budget_master` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fiscal_year` INT,
  `period_index` INT,
  `subject_code` VARCHAR(64),
  `dept_id` BIGINT,
  `project_id` BIGINT,
  `budget_amount` DECIMAL(18,2),
  `used_amount` DECIMAL(18,2),
  `control_type` VARCHAR(255),
  `status` TINYINT DEFAULT 1,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 budget_master';

DROP TABLE IF EXISTS `cash_account`;
CREATE TABLE `cash_account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `account_code` VARCHAR(255),
  `account_name` VARCHAR(255),
  `account_type` VARCHAR(255),
  `bank_name` VARCHAR(255),
  `bank_account` VARCHAR(255),
  `currency_code` VARCHAR(16),
  `subject_code` VARCHAR(64),
  `opening_balance` DECIMAL(18,2),
  `current_balance` DECIMAL(18,2),
  `status` TINYINT DEFAULT 1,
  `is_default` INT,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 cash_account';

DROP TABLE IF EXISTS `cash_bill`;
CREATE TABLE `cash_bill` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` VARCHAR(64),
  `bill_type` VARCHAR(255),
  `direction` VARCHAR(255),
  `drawer` VARCHAR(255),
  `payee` VARCHAR(255),
  `issue_date` DATE,
  `due_date` DATE,
  `amount` DECIMAL(18,2),
  `status` VARCHAR(255) DEFAULT 1,
  `bank_name` VARCHAR(255),
  `customer_id` BIGINT,
  `supplier_id` BIGINT,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 cash_bill';

DROP TABLE IF EXISTS `cash_journal`;
CREATE TABLE `cash_journal` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `account_id` BIGINT,
  `account_code` VARCHAR(255),
  `trans_date` DATE,
  `trans_type` VARCHAR(255),
  `amount` DECIMAL(18,2),
  `subject_code` VARCHAR(64),
  `summary` TEXT,
  `voucher_id` BIGINT,
  `source` VARCHAR(255),
  `source_id` BIGINT,
  `customer_id` BIGINT,
  `supplier_id` BIGINT,
  `dept_id` BIGINT,
  `employee_id` BIGINT,
  `status` VARCHAR(255) DEFAULT 1,
  `create_by` BIGINT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 cash_journal';

DROP TABLE IF EXISTS `cash_reconciliation`;
CREATE TABLE `cash_reconciliation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` VARCHAR(64),
  `account_id` BIGINT,
  `account_name` VARCHAR(255),
  `start_date` DATE,
  `end_date` DATE,
  `book_balance` DECIMAL(18,2),
  `bank_balance` DECIMAL(18,2),
  `diff_amount` DECIMAL(18,2),
  `status` VARCHAR(255) DEFAULT 1,
  `recon_time` DATETIME,
  `operator` BIGINT,
  `operator_name` VARCHAR(255),
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 cash_reconciliation';

DROP TABLE IF EXISTS `con_group`;
CREATE TABLE `con_group` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_code` VARCHAR(255),
  `group_name` VARCHAR(255),
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 con_group';

DROP TABLE IF EXISTS `con_offset`;
CREATE TABLE `con_offset` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fiscal_year` INT,
  `fiscal_period` INT,
  `offset_type` VARCHAR(255),
  `subject_code` VARCHAR(64),
  `subject_name` VARCHAR(255),
  `summary` TEXT,
  `amount` DECIMAL(18,2),
  `source_company_id` BIGINT,
  `source_company` VARCHAR(255),
  `target_company_id` BIGINT,
  `target_company` VARCHAR(255),
  `status` VARCHAR(255) DEFAULT 1,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 con_offset';

DROP TABLE IF EXISTS `cost_allocation`;
CREATE TABLE `cost_allocation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` VARCHAR(64),
  `fiscal_year` VARCHAR(255),
  `fiscal_period` INT,
  `allocation_date` DATE,
  `allocation_type` VARCHAR(255),
  `source_center_code` VARCHAR(255),
  `source_center_name` VARCHAR(255),
  `target_center_code` VARCHAR(255),
  `target_center_name` VARCHAR(255),
  `item_code` VARCHAR(64),
  `item_name` VARCHAR(128),
  `amount` DECIMAL(18,2),
  `source_no` VARCHAR(255),
  `remark` TEXT,
  `status` VARCHAR(255) DEFAULT 1,
  `operator` BIGINT,
  `operator_name` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 cost_allocation';

DROP TABLE IF EXISTS `cost_calculation`;
CREATE TABLE `cost_calculation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `period_code` VARCHAR(255),
  `product_item_id` BIGINT,
  `direct_material` DECIMAL(18,2),
  `direct_labor` DECIMAL(18,2),
  `manufacture_overhead` DECIMAL(18,2),
  `total_cost` DECIMAL(18,4),
  `output_qty` DECIMAL(18,4),
  `unit_cost` DECIMAL(18,4),
  `status` VARCHAR(255) DEFAULT 1,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 cost_calculation';

DROP TABLE IF EXISTS `cost_center`;
CREATE TABLE `cost_center` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `center_code` VARCHAR(255),
  `center_name` VARCHAR(255),
  `center_type` VARCHAR(255),
  `dept_id` BIGINT,
  `subject_code` VARCHAR(64),
  `status` TINYINT DEFAULT 1,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 cost_center';

DROP TABLE IF EXISTS `cost_element`;
CREATE TABLE `cost_element` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `element_code` VARCHAR(255),
  `element_name` VARCHAR(255),
  `element_type` VARCHAR(255),
  `subject_code` VARCHAR(64),
  `status` TINYINT DEFAULT 1,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 cost_element';

DROP TABLE IF EXISTS `ctr_purchase_contract`;
CREATE TABLE `ctr_purchase_contract` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `contract_no` VARCHAR(255),
  `contract_name` VARCHAR(255),
  `supplier_id` BIGINT,
  `total_amount` DECIMAL(18,2),
  `sign_date` DATE,
  `start_date` DATE,
  `end_date` DATE,
  `status` VARCHAR(255) DEFAULT 1,
  `attachment` TEXT,
  `owner_id` BIGINT,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 ctr_purchase_contract';

DROP TABLE IF EXISTS `ctr_sales_contract`;
CREATE TABLE `ctr_sales_contract` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `contract_no` VARCHAR(255),
  `contract_name` VARCHAR(255),
  `customer_id` BIGINT,
  `total_amount` DECIMAL(18,2),
  `sign_date` DATE,
  `start_date` DATE,
  `end_date` DATE,
  `attachment` TEXT,
  `owner_id` BIGINT,
  `status` VARCHAR(255) DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 ctr_sales_contract';

DROP TABLE IF EXISTS `exp_application`;
CREATE TABLE `exp_application` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` VARCHAR(64),
  `apply_type` VARCHAR(255),
  `subject_code` VARCHAR(64),
  `subject_name` VARCHAR(255),
  `amount` DECIMAL(18,2),
  `apply_date` DATE,
  `applicant` BIGINT,
  `applicant_name` VARCHAR(255),
  `dept_id` BIGINT,
  `dept_name` VARCHAR(128),
  `reason` TEXT,
  `attachment` TEXT,
  `status` VARCHAR(255) DEFAULT 1,
  `flow_no` VARCHAR(255),
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 exp_application';

DROP TABLE IF EXISTS `exp_loan`;
CREATE TABLE `exp_loan` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `loan_no` VARCHAR(255),
  `applicant_id` BIGINT,
  `loan_date` DATE,
  `amount` DECIMAL(18,2),
  `repaid_amount` DECIMAL(18,2),
  `purpose` VARCHAR(255),
  `status` VARCHAR(255) DEFAULT 1,
  `voucher_id` BIGINT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 exp_loan';

DROP TABLE IF EXISTS `fa_asset`;
CREATE TABLE `fa_asset` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `asset_code` VARCHAR(255),
  `asset_name` VARCHAR(255),
  `category_id` BIGINT,
  `spec` VARCHAR(128),
  `unit` VARCHAR(32),
  `purchase_date` DATE,
  `original_value` DECIMAL(18,2),
  `net_value` DECIMAL(18,2),
  `salvage_value` DECIMAL(18,2),
  `useful_life` INT,
  `depreciation_method` VARCHAR(255),
  `dept_id` BIGINT,
  `location` VARCHAR(255),
  `status` VARCHAR(255) DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 fa_asset';

DROP TABLE IF EXISTS `fa_category`;
CREATE TABLE `fa_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category_code` VARCHAR(64),
  `category_name` VARCHAR(128),
  `asset_type` VARCHAR(255),
  `depreciation_method` VARCHAR(255),
  `useful_life` INT,
  `salvage_rate` DECIMAL(18,4),
  `subject_code` VARCHAR(64),
  `dep_subject_code` VARCHAR(255),
  `status` TINYINT DEFAULT 1,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 fa_category';

DROP TABLE IF EXISTS `fa_depreciation`;
CREATE TABLE `fa_depreciation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `asset_id` BIGINT,
  `period_code` VARCHAR(255),
  `dep_date` DATE,
  `dep_amount` DECIMAL(18,2),
  `accumulated` DECIMAL(18,2),
  `net_value` DECIMAL(18,2),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 fa_depreciation';

DROP TABLE IF EXISTS `fa_disposal`;
CREATE TABLE `fa_disposal` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `disposal_no` VARCHAR(255),
  `asset_id` BIGINT,
  `disposal_date` DATE,
  `disposal_type` VARCHAR(255),
  `disposal_amount` DECIMAL(18,2),
  `gain_loss` DECIMAL(18,2),
  `reason` TEXT,
  `status` VARCHAR(255) DEFAULT 1,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 fa_disposal';

DROP TABLE IF EXISTS `fa_inventory`;
CREATE TABLE `fa_inventory` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` VARCHAR(64),
  `inventory_date` DATE,
  `inventory_type` VARCHAR(255),
  `status` VARCHAR(255) DEFAULT 1,
  `operator` BIGINT,
  `operator_name` VARCHAR(255),
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 fa_inventory';

DROP TABLE IF EXISTS `gl_account_subject`;
CREATE TABLE `gl_account_subject` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `subject_code` VARCHAR(64),
  `subject_name` VARCHAR(255),
  `parent_id` BIGINT,
  `parent_code` VARCHAR(255),
  `category_code` VARCHAR(64),
  `balance_direction` VARCHAR(255),
  `currency_code` VARCHAR(16),
  `level` INT,
  `is_leaf` INT,
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 gl_account_subject';

DROP TABLE IF EXISTS `gl_auxiliary_item`;
CREATE TABLE `gl_auxiliary_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type_code` VARCHAR(255),
  `item_code` VARCHAR(64),
  `item_name` VARCHAR(128),
  `parent_id` BIGINT,
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 gl_auxiliary_item';

DROP TABLE IF EXISTS `gl_balance`;
CREATE TABLE `gl_balance` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `period_code` VARCHAR(255),
  `subject_code` VARCHAR(64),
  `currency_code` VARCHAR(16),
  `opening_debit` DECIMAL(18,2),
  `opening_credit` DECIMAL(18,2),
  `period_debit` DECIMAL(18,2),
  `period_credit` DECIMAL(18,2),
  `year_debit` DECIMAL(18,2),
  `year_credit` DECIMAL(18,2),
  `ending_debit` DECIMAL(18,2),
  `ending_credit` DECIMAL(18,2),
  `aux_customer_id` BIGINT,
  `aux_supplier_id` BIGINT,
  `aux_dept_id` BIGINT,
  `aux_project_id` BIGINT,
  `aux_employee_id` BIGINT,
  `aux_product_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 gl_balance';

DROP TABLE IF EXISTS `gl_cash_flow_item`;
CREATE TABLE `gl_cash_flow_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `item_code` VARCHAR(64),
  `item_name` VARCHAR(128),
  `parent_id` BIGINT,
  `direction` VARCHAR(255),
  `flow_type` VARCHAR(255),
  `sort_order` INT,
  `status` TINYINT DEFAULT 1,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 gl_cash_flow_item';

DROP TABLE IF EXISTS `gl_period`;
CREATE TABLE `gl_period` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `period_code` VARCHAR(255),
  `period_name` VARCHAR(255),
  `start_date` DATE,
  `end_date` DATE,
  `fiscal_year` INT,
  `period_index` INT,
  `status` VARCHAR(255) DEFAULT 1,
  `is_adjust` INT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 gl_period';

DROP TABLE IF EXISTS `gl_voucher`;
CREATE TABLE `gl_voucher` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `voucher_no` VARCHAR(64),
  `voucher_type` VARCHAR(255),
  `period_code` VARCHAR(255),
  `voucher_date` DATE,
  `attach_count` INT,
  `total_debit` DECIMAL(18,2),
  `total_credit` DECIMAL(18,2),
  `summary` TEXT,
  `source` VARCHAR(255),
  `source_biz` VARCHAR(255),
  `source_id` BIGINT,
  `status` VARCHAR(255) DEFAULT 1,
  `is_cash` INT,
  `is_audit` INT,
  `create_by` BIGINT,
  `create_by_name` VARCHAR(255),
  `audit_by` BIGINT,
  `audit_by_name` VARCHAR(255),
  `audit_time` DATETIME,
  `post_by` BIGINT,
  `post_time` DATETIME,
  `cancel_by` BIGINT,
  `cancel_time` DATETIME,
  `cancel_reason` TEXT,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 gl_voucher';

DROP TABLE IF EXISTS `gl_voucher_entry`;
CREATE TABLE `gl_voucher_entry` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `voucher_id` BIGINT,
  `line_no` INT,
  `aux_summary` TEXT,
  `subject_code` VARCHAR(64),
  `subject_name` VARCHAR(255),
  `aux_employee_id` BIGINT,
  `aux_product_id` BIGINT,
  `currency_code` VARCHAR(16),
  `orig_amount` DECIMAL(18,2),
  `exchange_rate` DECIMAL(18,4),
  `dc_direction` VARCHAR(255),
  `amount` DECIMAL(18,2),
  `quantity` DECIMAL(18,4),
  `price` DECIMAL(18,4),
  `cash_flow_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 gl_voucher_entry';

DROP TABLE IF EXISTS `inv_balance`;
CREATE TABLE `inv_balance` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `item_id` BIGINT,
  `wh_id` BIGINT,
  `period_code` VARCHAR(255),
  `opening_qty` DECIMAL(18,4),
  `opening_amt` DECIMAL(18,2),
  `period_in_qty` DECIMAL(18,4),
  `period_in_amt` DECIMAL(18,2),
  `period_out_qty` DECIMAL(18,4),
  `period_out_amt` DECIMAL(18,2),
  `ending_qty` DECIMAL(18,4),
  `ending_amt` DECIMAL(18,2),
  `avg_price` DECIMAL(18,4),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 inv_balance';

DROP TABLE IF EXISTS `inv_item`;
CREATE TABLE `inv_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `item_code` VARCHAR(64),
  `item_name` VARCHAR(128),
  `category_id` BIGINT,
  `spec` VARCHAR(128),
  `unit` VARCHAR(32),
  `barcode` VARCHAR(255),
  `subject_code` VARCHAR(64),
  `cost_method` VARCHAR(255),
  `safety_stock` DECIMAL(18,2),
  `is_batch_manage` INT,
  `is_serial_manage` INT,
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 inv_item';

DROP TABLE IF EXISTS `inv_transaction`;
CREATE TABLE `inv_transaction` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` VARCHAR(64),
  `io_type` VARCHAR(255),
  `io_date` DATE,
  `goods_id` BIGINT,
  `goods_code` VARCHAR(255),
  `goods_name` VARCHAR(255),
  `spec` VARCHAR(128),
  `unit` VARCHAR(32),
  `quantity` DECIMAL(18,4),
  `price` DECIMAL(18,4),
  `amount` DECIMAL(18,2),
  `total_cost` DECIMAL(18,4),
  `warehouse_id` BIGINT,
  `warehouse_name` VARCHAR(128),
  `batch_no` VARCHAR(255),
  `business_no` VARCHAR(255),
  `source_module` VARCHAR(255),
  `dept_id` BIGINT,
  `dept_name` VARCHAR(128),
  `remark` TEXT,
  `status` VARCHAR(255) DEFAULT 1,
  `creator` BIGINT,
  `creator_name` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 inv_transaction';

DROP TABLE IF EXISTS `inv_warehouse`;
CREATE TABLE `inv_warehouse` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `location` VARCHAR(255),
  `manager` VARCHAR(255),
  `status` TINYINT DEFAULT 1,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 inv_warehouse';

DROP TABLE IF EXISTS `prj_budget`;
CREATE TABLE `prj_budget` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` BIGINT,
  `budget_type` VARCHAR(255),
  `subject_code` VARCHAR(64),
  `amount` DECIMAL(18,2),
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 prj_budget';

DROP TABLE IF EXISTS `prj_ledger`;
CREATE TABLE `prj_ledger` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` BIGINT,
  `project_name` VARCHAR(255),
  `business_type` VARCHAR(255),
  `business_id` BIGINT,
  `business_no` VARCHAR(255),
  `summary` TEXT,
  `amount` DECIMAL(18,2),
  `handle_date` DATE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 prj_ledger';

DROP TABLE IF EXISTS `prj_project`;
CREATE TABLE `prj_project` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_code` VARCHAR(255),
  `project_name` VARCHAR(255),
  `project_type` VARCHAR(255),
  `manager_id` BIGINT,
  `dept_id` BIGINT,
  `start_date` DATE,
  `end_date` DATE,
  `budget_amount` DECIMAL(18,2),
  `actual_amount` DECIMAL(18,2),
  `status` VARCHAR(255) DEFAULT 1,
  `revenue` DECIMAL(18,2),
  `cost` DECIMAL(18,4),
  `profit` DECIMAL(18,2),
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 prj_project';

DROP TABLE IF EXISTS `rpt_custom`;
CREATE TABLE `rpt_custom` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `report_code` VARCHAR(255),
  `report_name` VARCHAR(255),
  `report_type` VARCHAR(255),
  `fiscal_year` INT,
  `fiscal_period` INT,
  `config_json` VARCHAR(255),
  `status` VARCHAR(255) DEFAULT 1,
  `creator_id` BIGINT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 rpt_custom';

DROP TABLE IF EXISTS `sys_company`;
CREATE TABLE `sys_company` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `company_code` VARCHAR(64),
  `company_name` VARCHAR(255),
  `short_name` VARCHAR(128),
  `tax_no` VARCHAR(64),
  `legal_person` VARCHAR(64),
  `address` TEXT,
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 sys_company';

DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_name` VARCHAR(128),
  `config_key` VARCHAR(128),
  `config_value` VARCHAR(255),
  `config_type` VARCHAR(255),
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 sys_config';

DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` BIGINT,
  `ancestors` VARCHAR(255),
  `dept_code` VARCHAR(255),
  `dept_name` VARCHAR(128),
  `sort_order` INT,
  `leader` VARCHAR(255),
  `phone` VARCHAR(32),
  `email` VARCHAR(128),
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 sys_dept';

DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dict_type` VARCHAR(255),
  `dict_label` VARCHAR(128),
  `dict_value` VARCHAR(128),
  `sort_order` INT,
  `is_default` INT,
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 sys_dict_data';

DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dict_type` VARCHAR(255),
  `dict_name` VARCHAR(255),
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 sys_dict_type';

DROP TABLE IF EXISTS `sys_log_login`;
CREATE TABLE `sys_log_login` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(64),
  `ip` VARCHAR(64),
  `location` VARCHAR(255),
  `browser` VARCHAR(64),
  `os` VARCHAR(64),
  `status` TINYINT DEFAULT 1,
  `message` VARCHAR(255),
  `login_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 sys_log_login';

DROP TABLE IF EXISTS `sys_log_operation`;
CREATE TABLE `sys_log_operation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `module` VARCHAR(255),
  `action` VARCHAR(255),
  `method` VARCHAR(255),
  `request_url` TEXT,
  `request_data` VARCHAR(255),
  `response` VARCHAR(255),
  `user_id` BIGINT,
  `username` VARCHAR(64),
  `ip` VARCHAR(64),
  `cost_time` BIGINT,
  `status` TINYINT DEFAULT 1,
  `error_msg` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 sys_log_operation';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` BIGINT,
  `menu_name` VARCHAR(128),
  `menu_type` VARCHAR(255),
  `path` TEXT,
  `component` VARCHAR(255),
  `perm_code` VARCHAR(255),
  `icon` VARCHAR(255),
  `sort_order` INT,
  `visible` INT,
  `status` TINYINT DEFAULT 1,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 sys_menu';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_code` VARCHAR(255),
  `role_name` VARCHAR(128),
  `sort_order` INT,
  `data_scope` VARCHAR(255),
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 sys_role';

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(64),
  `password` VARCHAR(100),
  `real_name` VARCHAR(64),
  `avatar` VARCHAR(255),
  `phone` VARCHAR(32),
  `email` VARCHAR(128),
  `gender` INT,
  `dept_id` BIGINT,
  `dept_name` VARCHAR(128),
  `user_type` VARCHAR(255),
  `status` TINYINT DEFAULT 1,
  `last_login` DATETIME,
  `last_ip` VARCHAR(255),
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 sys_user';

DROP TABLE IF EXISTS `wf_approval_config`;
CREATE TABLE `wf_approval_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `doc_type` VARCHAR(255),
  `doc_type_name` VARCHAR(255),
  `approver_ids` VARCHAR(255),
  `approve_mode` VARCHAR(255),
  `status` TINYINT DEFAULT 1,
  `remark` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 wf_approval_config';

DROP TABLE IF EXISTS `wf_approval_record`;
CREATE TABLE `wf_approval_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `doc_type` VARCHAR(255),
  `doc_id` BIGINT,
  `doc_no` VARCHAR(255),
  `step_no` INT,
  `submitter_id` BIGINT,
  `submitter_name` VARCHAR(255),
  `approver_id` BIGINT,
  `approver_name` VARCHAR(255),
  `action` VARCHAR(255),
  `opinion` TEXT,
  `status` VARCHAR(255) DEFAULT 1,
  `submit_time` DATETIME,
  `approve_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表 wf_approval_record';

SET FOREIGN_KEY_CHECKS = 1;
-- ===== 补充表（无实体类，从 dump/代码引用推断） =====
DROP TABLE IF EXISTS `gl_account_category`;
CREATE TABLE `gl_account_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category_code` VARCHAR(64) COMMENT '科目类别编码',
  `category_name` VARCHAR(128) COMMENT '科目类别名称',
  `balance_direction` VARCHAR(16) COMMENT '余额方向 DEBIT/CREDIT',
  `sort_order` INT DEFAULT 0,
  `remark` VARCHAR(500),
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='科目类别';

DROP TABLE IF EXISTS `gl_auxiliary_type`;
CREATE TABLE `gl_auxiliary_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type_code` VARCHAR(64) COMMENT '辅助核算类型编码',
  `type_name` VARCHAR(128) COMMENT '辅助核算类型名称',
  `table_name` VARCHAR(128) COMMENT '关联表名',
  `status` TINYINT DEFAULT 1,
  `sort_order` INT DEFAULT 0,
  `deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='辅助核算类型';

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单关联';

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联';
