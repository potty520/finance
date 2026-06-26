/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.11.14-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: 103.236.96.82    Database: finance_db
-- ------------------------------------------------------
-- Server version	8.0.46-0ubuntu0.22.04.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `ap_bill`
--

LOCK TABLES `ap_bill` WRITE;
/*!40000 ALTER TABLE `ap_bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `ap_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ap_invoice`
--

LOCK TABLES `ap_invoice` WRITE;
/*!40000 ALTER TABLE `ap_invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `ap_invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ap_invoice_entry`
--

LOCK TABLES `ap_invoice_entry` WRITE;
/*!40000 ALTER TABLE `ap_invoice_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `ap_invoice_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ap_payment`
--

LOCK TABLES `ap_payment` WRITE;
/*!40000 ALTER TABLE `ap_payment` DISABLE KEYS */;
INSERT INTO `ap_payment` (`id`, `payment_no`, `supplier_id`, `payment_date`, `amount`, `settled_amount`, `account_id`, `payment_type`, `bill_id`, `voucher_id`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (1,'PAY-202606-001',1,'2026-06-20',25000.00,0.00,NULL,'BANK',NULL,NULL,'POSTED',NULL,'2026-06-25 15:58:04','2026-06-25 15:58:04',0);
/*!40000 ALTER TABLE `ap_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ap_supplier`
--

LOCK TABLES `ap_supplier` WRITE;
/*!40000 ALTER TABLE `ap_supplier` DISABLE KEYS */;
/*!40000 ALTER TABLE `ap_supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ap_writeoff`
--

LOCK TABLES `ap_writeoff` WRITE;
/*!40000 ALTER TABLE `ap_writeoff` DISABLE KEYS */;
/*!40000 ALTER TABLE `ap_writeoff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ar_bad_debt`
--

LOCK TABLES `ar_bad_debt` WRITE;
/*!40000 ALTER TABLE `ar_bad_debt` DISABLE KEYS */;
/*!40000 ALTER TABLE `ar_bad_debt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ar_bill`
--

LOCK TABLES `ar_bill` WRITE;
/*!40000 ALTER TABLE `ar_bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `ar_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ar_customer`
--

LOCK TABLES `ar_customer` WRITE;
/*!40000 ALTER TABLE `ar_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `ar_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ar_invoice`
--

LOCK TABLES `ar_invoice` WRITE;
/*!40000 ALTER TABLE `ar_invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `ar_invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ar_invoice_entry`
--

LOCK TABLES `ar_invoice_entry` WRITE;
/*!40000 ALTER TABLE `ar_invoice_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `ar_invoice_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ar_receipt`
--

LOCK TABLES `ar_receipt` WRITE;
/*!40000 ALTER TABLE `ar_receipt` DISABLE KEYS */;
/*!40000 ALTER TABLE `ar_receipt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ar_writeoff`
--

LOCK TABLES `ar_writeoff` WRITE;
/*!40000 ALTER TABLE `ar_writeoff` DISABLE KEYS */;
/*!40000 ALTER TABLE `ar_writeoff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `budget_adjustment`
--

LOCK TABLES `budget_adjustment` WRITE;
/*!40000 ALTER TABLE `budget_adjustment` DISABLE KEYS */;
/*!40000 ALTER TABLE `budget_adjustment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `budget_master`
--

LOCK TABLES `budget_master` WRITE;
/*!40000 ALTER TABLE `budget_master` DISABLE KEYS */;
INSERT INTO `budget_master` (`id`, `fiscal_year`, `period_index`, `subject_code`, `dept_id`, `project_id`, `budget_amount`, `used_amount`, `control_type`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1,2026,6,'6602',1,NULL,200000.00,45000.00,'WARN',2,'2026-06-25 15:58:04','2026-06-25 15:58:04',0);
INSERT INTO `budget_master` (`id`, `fiscal_year`, `period_index`, `subject_code`, `dept_id`, `project_id`, `budget_amount`, `used_amount`, `control_type`, `status`, `create_time`, `update_time`, `deleted`) VALUES (2,2026,6,'6001',1,NULL,500000.00,120000.00,'WARN',2,'2026-06-25 15:58:04','2026-06-25 15:58:04',0);
INSERT INTO `budget_master` (`id`, `fiscal_year`, `period_index`, `subject_code`, `dept_id`, `project_id`, `budget_amount`, `used_amount`, `control_type`, `status`, `create_time`, `update_time`, `deleted`) VALUES (3,2026,6,'4101',2,NULL,150000.00,50000.00,'WARN',1,'2026-06-25 16:25:48','2026-06-25 16:25:48',0);
INSERT INTO `budget_master` (`id`, `fiscal_year`, `period_index`, `subject_code`, `dept_id`, `project_id`, `budget_amount`, `used_amount`, `control_type`, `status`, `create_time`, `update_time`, `deleted`) VALUES (4,2026,6,'4102',2,NULL,80000.00,20000.00,'WARN',1,'2026-06-25 16:25:48','2026-06-25 16:25:48',0);
INSERT INTO `budget_master` (`id`, `fiscal_year`, `period_index`, `subject_code`, `dept_id`, `project_id`, `budget_amount`, `used_amount`, `control_type`, `status`, `create_time`, `update_time`, `deleted`) VALUES (5,2026,6,'4401',1,NULL,100000.00,10000.00,'STRICT',1,'2026-06-25 16:25:48','2026-06-25 16:25:48',0);
/*!40000 ALTER TABLE `budget_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cash_account`
--

LOCK TABLES `cash_account` WRITE;
/*!40000 ALTER TABLE `cash_account` DISABLE KEYS */;
INSERT INTO `cash_account` (`id`, `account_code`, `account_name`, `account_type`, `bank_name`, `bank_account`, `currency_code`, `subject_code`, `opening_balance`, `current_balance`, `status`, `is_default`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (7,'CASH001','库存现金','CASH',NULL,NULL,'CNY',NULL,0.00,0.00,1,0,NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `cash_account` (`id`, `account_code`, `account_name`, `account_type`, `bank_name`, `bank_account`, `currency_code`, `subject_code`, `opening_balance`, `current_balance`, `status`, `is_default`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (8,'BANK001','基本户-工商银行','BANK',NULL,NULL,'CNY',NULL,0.00,0.00,1,1,NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
/*!40000 ALTER TABLE `cash_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cash_bank_statement`
--

LOCK TABLES `cash_bank_statement` WRITE;
/*!40000 ALTER TABLE `cash_bank_statement` DISABLE KEYS */;
/*!40000 ALTER TABLE `cash_bank_statement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cash_bill`
--

LOCK TABLES `cash_bill` WRITE;
/*!40000 ALTER TABLE `cash_bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `cash_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cash_journal`
--

LOCK TABLES `cash_journal` WRITE;
/*!40000 ALTER TABLE `cash_journal` DISABLE KEYS */;
INSERT INTO `cash_journal` (`id`, `account_id`, `account_code`, `trans_date`, `trans_type`, `amount`, `subject_code`, `summary`, `voucher_id`, `source`, `source_id`, `customer_id`, `supplier_id`, `dept_id`, `employee_id`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES (37,2,'BANK001','2026-06-05','IN',350000.00,NULL,'CF01|销售商品、提供劳务收到的现金',NULL,'MANUAL',NULL,NULL,NULL,NULL,NULL,'POSTED',NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `cash_journal` (`id`, `account_id`, `account_code`, `trans_date`, `trans_type`, `amount`, `subject_code`, `summary`, `voucher_id`, `source`, `source_id`, `customer_id`, `supplier_id`, `dept_id`, `employee_id`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES (38,2,'BANK001','2026-06-08','IN',15000.00,NULL,'CF02|收到的税费返还',NULL,'MANUAL',NULL,NULL,NULL,NULL,NULL,'POSTED',NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `cash_journal` (`id`, `account_id`, `account_code`, `trans_date`, `trans_type`, `amount`, `subject_code`, `summary`, `voucher_id`, `source`, `source_id`, `customer_id`, `supplier_id`, `dept_id`, `employee_id`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES (39,2,'BANK001','2026-06-10','OUT',180000.00,NULL,'CF04|购买商品、接受劳务支付的现金',NULL,'MANUAL',NULL,NULL,NULL,NULL,NULL,'POSTED',NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `cash_journal` (`id`, `account_id`, `account_code`, `trans_date`, `trans_type`, `amount`, `subject_code`, `summary`, `voucher_id`, `source`, `source_id`, `customer_id`, `supplier_id`, `dept_id`, `employee_id`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES (40,2,'BANK001','2026-06-15','OUT',85000.00,NULL,'CF05|支付给职工以及为职工支付的现金',NULL,'MANUAL',NULL,NULL,NULL,NULL,NULL,'POSTED',NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `cash_journal` (`id`, `account_id`, `account_code`, `trans_date`, `trans_type`, `amount`, `subject_code`, `summary`, `voucher_id`, `source`, `source_id`, `customer_id`, `supplier_id`, `dept_id`, `employee_id`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES (41,2,'BANK001','2026-06-18','OUT',42000.00,NULL,'CF06|支付的各项税费',NULL,'MANUAL',NULL,NULL,NULL,NULL,NULL,'POSTED',NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `cash_journal` (`id`, `account_id`, `account_code`, `trans_date`, `trans_type`, `amount`, `subject_code`, `summary`, `voucher_id`, `source`, `source_id`, `customer_id`, `supplier_id`, `dept_id`, `employee_id`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES (42,2,'BANK001','2026-06-20','IN',50000.00,NULL,'CF08|收回投资收到的现金',NULL,'MANUAL',NULL,NULL,NULL,NULL,NULL,'POSTED',NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `cash_journal` (`id`, `account_id`, `account_code`, `trans_date`, `trans_type`, `amount`, `subject_code`, `summary`, `voucher_id`, `source`, `source_id`, `customer_id`, `supplier_id`, `dept_id`, `employee_id`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES (43,2,'BANK001','2026-06-22','OUT',80000.00,NULL,'CF10|投资支付的现金',NULL,'MANUAL',NULL,NULL,NULL,NULL,NULL,'POSTED',NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `cash_journal` (`id`, `account_id`, `account_code`, `trans_date`, `trans_type`, `amount`, `subject_code`, `summary`, `voucher_id`, `source`, `source_id`, `customer_id`, `supplier_id`, `dept_id`, `employee_id`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES (44,2,'BANK001','2026-06-25','IN',200000.00,NULL,'CF11|取得借款收到的现金',NULL,'MANUAL',NULL,NULL,NULL,NULL,NULL,'POSTED',NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `cash_journal` (`id`, `account_id`, `account_code`, `trans_date`, `trans_type`, `amount`, `subject_code`, `summary`, `voucher_id`, `source`, `source_id`, `customer_id`, `supplier_id`, `dept_id`, `employee_id`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES (45,2,'BANK001','2026-06-28','OUT',120000.00,NULL,'CF12|偿还债务支付的现金',NULL,'MANUAL',NULL,NULL,NULL,NULL,NULL,'POSTED',NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `cash_journal` (`id`, `account_id`, `account_code`, `trans_date`, `trans_type`, `amount`, `subject_code`, `summary`, `voucher_id`, `source`, `source_id`, `customer_id`, `supplier_id`, `dept_id`, `employee_id`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES (46,2,'BANK001','2026-06-27','BANK',1000.00,NULL,'测试单号生成',NULL,'MANUAL',NULL,NULL,NULL,NULL,NULL,'DRAFT',NULL,'2026-06-26 16:03:49','2026-06-26 16:03:49',0);
/*!40000 ALTER TABLE `cash_journal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cash_reconciliation`
--

LOCK TABLES `cash_reconciliation` WRITE;
/*!40000 ALTER TABLE `cash_reconciliation` DISABLE KEYS */;
/*!40000 ALTER TABLE `cash_reconciliation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `con_group`
--

LOCK TABLES `con_group` WRITE;
/*!40000 ALTER TABLE `con_group` DISABLE KEYS */;
INSERT INTO `con_group` (`id`, `group_code`, `group_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (1,'CG001','合并报表组2026',1,NULL,'2026-06-25 16:07:48','2026-06-25 16:07:48',0);
/*!40000 ALTER TABLE `con_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `con_offset`
--

LOCK TABLES `con_offset` WRITE;
/*!40000 ALTER TABLE `con_offset` DISABLE KEYS */;
/*!40000 ALTER TABLE `con_offset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cost_allocation`
--

LOCK TABLES `cost_allocation` WRITE;
/*!40000 ALTER TABLE `cost_allocation` DISABLE KEYS */;
/*!40000 ALTER TABLE `cost_allocation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cost_calculation`
--

LOCK TABLES `cost_calculation` WRITE;
/*!40000 ALTER TABLE `cost_calculation` DISABLE KEYS */;
INSERT INTO `cost_calculation` (`id`, `period_code`, `product_item_id`, `direct_material`, `direct_labor`, `manufacture_overhead`, `total_cost`, `output_qty`, `unit_cost`, `status`, `create_time`, `deleted`) VALUES (1,'202606',1,120000.00,45000.00,18000.00,183000.00,1000.0000,183.0000,'POSTED','2026-06-25 15:58:04',0);
/*!40000 ALTER TABLE `cost_calculation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cost_center`
--

LOCK TABLES `cost_center` WRITE;
/*!40000 ALTER TABLE `cost_center` DISABLE KEYS */;
/*!40000 ALTER TABLE `cost_center` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cost_element`
--

LOCK TABLES `cost_element` WRITE;
/*!40000 ALTER TABLE `cost_element` DISABLE KEYS */;
/*!40000 ALTER TABLE `cost_element` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ctr_execution`
--

LOCK TABLES `ctr_execution` WRITE;
/*!40000 ALTER TABLE `ctr_execution` DISABLE KEYS */;
/*!40000 ALTER TABLE `ctr_execution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ctr_purchase_contract`
--

LOCK TABLES `ctr_purchase_contract` WRITE;
/*!40000 ALTER TABLE `ctr_purchase_contract` DISABLE KEYS */;
/*!40000 ALTER TABLE `ctr_purchase_contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ctr_sales_contract`
--

LOCK TABLES `ctr_sales_contract` WRITE;
/*!40000 ALTER TABLE `ctr_sales_contract` DISABLE KEYS */;
INSERT INTO `ctr_sales_contract` (`id`, `contract_no`, `contract_name`, `customer_id`, `sign_date`, `start_date`, `end_date`, `total_amount`, `invoiced_amount`, `received_amount`, `status`, `owner_id`, `remark`, `create_time`, `update_time`, `deleted`, `attachment`) VALUES (2,'CT-1782441496756','测试导入合同',0,'2026-06-26',NULL,NULL,0.00,0.00,0.00,'DRAFT',1,NULL,'2026-06-26 02:38:08','2026-06-26 02:38:08',0,'{\"ext\":\"pdf\",\"path\":\"contract/202606/60b7e7651a26451795b60e0ab7c3eb2c_test-contract.pdf\",\"fileName\":\"test-contract.pdf\",\"size\":22}');
/*!40000 ALTER TABLE `ctr_sales_contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `exp_application`
--

LOCK TABLES `exp_application` WRITE;
/*!40000 ALTER TABLE `exp_application` DISABLE KEYS */;
/*!40000 ALTER TABLE `exp_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `exp_loan`
--

LOCK TABLES `exp_loan` WRITE;
/*!40000 ALTER TABLE `exp_loan` DISABLE KEYS */;
/*!40000 ALTER TABLE `exp_loan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `exp_reimbursement`
--

LOCK TABLES `exp_reimbursement` WRITE;
/*!40000 ALTER TABLE `exp_reimbursement` DISABLE KEYS */;
/*!40000 ALTER TABLE `exp_reimbursement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `exp_reimbursement_entry`
--

LOCK TABLES `exp_reimbursement_entry` WRITE;
/*!40000 ALTER TABLE `exp_reimbursement_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `exp_reimbursement_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fa_asset`
--

LOCK TABLES `fa_asset` WRITE;
/*!40000 ALTER TABLE `fa_asset` DISABLE KEYS */;
/*!40000 ALTER TABLE `fa_asset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fa_category`
--

LOCK TABLES `fa_category` WRITE;
/*!40000 ALTER TABLE `fa_category` DISABLE KEYS */;
INSERT INTO `fa_category` (`id`, `category_code`, `category_name`, `asset_type`, `depreciation_method`, `useful_life`, `salvage_rate`, `subject_code`, `dep_subject_code`, `status`, `deleted`) VALUES (29,'CAT01','房屋及建筑物','FIXED','STRAIGHT',20,0.0500,NULL,NULL,1,0);
INSERT INTO `fa_category` (`id`, `category_code`, `category_name`, `asset_type`, `depreciation_method`, `useful_life`, `salvage_rate`, `subject_code`, `dep_subject_code`, `status`, `deleted`) VALUES (30,'CAT02','机器设备','FIXED','STRAIGHT',10,0.0500,NULL,NULL,1,0);
INSERT INTO `fa_category` (`id`, `category_code`, `category_name`, `asset_type`, `depreciation_method`, `useful_life`, `salvage_rate`, `subject_code`, `dep_subject_code`, `status`, `deleted`) VALUES (31,'CAT03','运输工具','FIXED','STRAIGHT',4,0.0500,NULL,NULL,1,0);
INSERT INTO `fa_category` (`id`, `category_code`, `category_name`, `asset_type`, `depreciation_method`, `useful_life`, `salvage_rate`, `subject_code`, `dep_subject_code`, `status`, `deleted`) VALUES (32,'CAT04','办公设备','FIXED','STRAIGHT',5,0.0500,NULL,NULL,1,0);
INSERT INTO `fa_category` (`id`, `category_code`, `category_name`, `asset_type`, `depreciation_method`, `useful_life`, `salvage_rate`, `subject_code`, `dep_subject_code`, `status`, `deleted`) VALUES (33,'CAT05','电子设备','FIXED','STRAIGHT',3,0.0500,NULL,NULL,1,0);
INSERT INTO `fa_category` (`id`, `category_code`, `category_name`, `asset_type`, `depreciation_method`, `useful_life`, `salvage_rate`, `subject_code`, `dep_subject_code`, `status`, `deleted`) VALUES (34,'CAT06','专利权','INTANGIBLE','STRAIGHT',10,0.0000,NULL,NULL,1,0);
INSERT INTO `fa_category` (`id`, `category_code`, `category_name`, `asset_type`, `depreciation_method`, `useful_life`, `salvage_rate`, `subject_code`, `dep_subject_code`, `status`, `deleted`) VALUES (35,'CAT07','软件','INTANGIBLE','STRAIGHT',5,0.0000,NULL,NULL,1,0);
/*!40000 ALTER TABLE `fa_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fa_depreciation`
--

LOCK TABLES `fa_depreciation` WRITE;
/*!40000 ALTER TABLE `fa_depreciation` DISABLE KEYS */;
/*!40000 ALTER TABLE `fa_depreciation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fa_disposal`
--

LOCK TABLES `fa_disposal` WRITE;
/*!40000 ALTER TABLE `fa_disposal` DISABLE KEYS */;
/*!40000 ALTER TABLE `fa_disposal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fa_inventory`
--

LOCK TABLES `fa_inventory` WRITE;
/*!40000 ALTER TABLE `fa_inventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `fa_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fa_inventory_detail`
--

LOCK TABLES `fa_inventory_detail` WRITE;
/*!40000 ALTER TABLE `fa_inventory_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `fa_inventory_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fa_transfer`
--

LOCK TABLES `fa_transfer` WRITE;
/*!40000 ALTER TABLE `fa_transfer` DISABLE KEYS */;
/*!40000 ALTER TABLE `fa_transfer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gl_account_category`
--

LOCK TABLES `gl_account_category` WRITE;
/*!40000 ALTER TABLE `gl_account_category` DISABLE KEYS */;
INSERT INTO `gl_account_category` (`id`, `category_code`, `category_name`, `balance_direction`, `sort_order`, `remark`, `deleted`) VALUES (19,'ASSET','资产','DEBIT',1,NULL,0);
INSERT INTO `gl_account_category` (`id`, `category_code`, `category_name`, `balance_direction`, `sort_order`, `remark`, `deleted`) VALUES (20,'LIAB','负债','CREDIT',2,NULL,0);
INSERT INTO `gl_account_category` (`id`, `category_code`, `category_name`, `balance_direction`, `sort_order`, `remark`, `deleted`) VALUES (21,'EQUITY','所有者权益','CREDIT',3,NULL,0);
INSERT INTO `gl_account_category` (`id`, `category_code`, `category_name`, `balance_direction`, `sort_order`, `remark`, `deleted`) VALUES (22,'INCOME','收入','CREDIT',4,NULL,0);
INSERT INTO `gl_account_category` (`id`, `category_code`, `category_name`, `balance_direction`, `sort_order`, `remark`, `deleted`) VALUES (23,'EXPENSE','费用','DEBIT',5,NULL,0);
INSERT INTO `gl_account_category` (`id`, `category_code`, `category_name`, `balance_direction`, `sort_order`, `remark`, `deleted`) VALUES (24,'COST','成本','DEBIT',6,NULL,0);
/*!40000 ALTER TABLE `gl_account_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gl_account_subject`
--

LOCK TABLES `gl_account_subject` WRITE;
/*!40000 ALTER TABLE `gl_account_subject` DISABLE KEYS */;
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (136,'1001','库存现金',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (137,'1002','银行存款',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (138,'1012','其他货币资金',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (139,'1101','交易性金融资产',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (140,'1121','应收票据',0,NULL,'ASSET',2,1,'借方',1,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (141,'1122','应收账款',0,NULL,'ASSET',2,1,'借方',1,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (142,'1123','预付账款',0,NULL,'ASSET',2,1,'借方',0,1,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (143,'1131','应收股利',0,NULL,'ASSET',2,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (144,'1221','其他应收款',0,NULL,'ASSET',2,1,'借方',0,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (145,'1401','材料采购',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (146,'1403','原材料',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (147,'1405','库存商品',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (148,'1411','周转材料',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (149,'1601','固定资产',0,NULL,'ASSET',1,1,'借方',0,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (150,'1602','累计折旧',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (151,'1604','在建工程',0,NULL,'ASSET',1,1,'借方',0,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (152,'1701','无形资产',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (153,'1801','长期待摊费用',0,NULL,'ASSET',1,1,'借方',0,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (154,'2001','短期借款',0,NULL,'LIAB',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (155,'2201','应付票据',0,NULL,'LIAB',2,1,'贷方',0,1,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (156,'2202','应付账款',0,NULL,'LIAB',2,1,'贷方',0,1,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (157,'2203','预收账款',0,NULL,'LIAB',2,1,'贷方',1,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (158,'2211','应付职工薪酬',0,NULL,'LIAB',2,1,'贷方',0,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (159,'2221','应交税费',0,NULL,'LIAB',2,0,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (160,'222101','应交增值税',2221,NULL,'LIAB',3,0,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-26 14:41:25',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (161,'222102','未交增值税',2221,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (162,'222106','应交所得税',2221,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (163,'2231','应付利息',0,NULL,'LIAB',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (164,'2241','其他应付款',0,NULL,'LIAB',2,1,'贷方',0,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (165,'2501','长期借款',0,NULL,'LIAB',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (166,'4001','实收资本',0,NULL,'EQUITY',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (167,'4002','资本公积',0,NULL,'EQUITY',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (168,'4101','盈余公积',0,NULL,'EQUITY',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (169,'4103','本年利润',0,NULL,'EQUITY',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (170,'4102','利润分配',0,NULL,'EQUITY',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (171,'6001','主营业务收入',0,NULL,'INCOME',1,1,'贷方',1,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (172,'6051','其他业务收入',0,NULL,'INCOME',1,1,'贷方',1,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (173,'6111','投资收益',0,NULL,'INCOME',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (174,'6301','营业外收入',0,NULL,'INCOME',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:13',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (175,'5401','主营业务成本',0,NULL,'COST',1,1,'借方',1,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (176,'5402','其他业务成本',0,NULL,'COST',1,1,'借方',0,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (177,'6601','销售费用',0,NULL,'EXPENSE',1,1,'借方',0,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (178,'6602','管理费用',0,NULL,'EXPENSE',1,1,'借方',0,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (179,'6603','财务费用',0,NULL,'EXPENSE',1,1,'借方',0,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (180,'6711','营业外支出',0,NULL,'EXPENSE',1,1,'借方',0,0,1,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:49:10',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (182,'1102','短期投资',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (183,'1124','应收补贴款',0,NULL,'ASSET',2,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (184,'1132','应收利息',0,NULL,'ASSET',2,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (185,'1231','坏账准备',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (186,'1301','待摊费用',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (187,'1402','在途物资',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (188,'1404','材料成本差异',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (189,'1406','发出商品',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (190,'1407','商品进销差价',0,NULL,'ASSET',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (191,'1408','委托加工物资',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (192,'1412','包装物',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (193,'1413','低值易耗品',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (194,'1421','消耗性生物资产',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (195,'1431','贵金属',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (196,'1441','抵债资产',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (197,'1461','融资租赁资产',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (198,'1471','存货跌价准备',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (199,'1481','合同资产',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (200,'1482','合同资产减值准备',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (201,'1501','债权投资',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (202,'1502','债权投资减值准备',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (203,'1503','其他债权投资',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (204,'1504','其他权益工具投资',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (205,'1511','长期股权投资',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (206,'1512','长期股权投资减值准备',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (207,'1521','投资性房地产',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (208,'1522','投资性房地产累计折旧',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (209,'1523','投资性房地产减值准备',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (210,'1531','长期应收款',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (211,'1532','未实现融资收益',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (212,'1541','存出资本保证金',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (213,'1603','固定资产减值准备',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (214,'1605','工程物资',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (215,'1606','固定资产清理',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (216,'1611','未担保余值',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (217,'1621','生产性生物资产',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (218,'1622','生产性生物资产累计折旧',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (219,'1631','油气资产',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (220,'1632','累计折耗',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (221,'1702','累计摊销',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (222,'1703','无形资产减值准备',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (223,'1711','商誉',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (224,'1712','商誉减值准备',0,NULL,'ASSET',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (225,'1802','递延所得税资产',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (226,'1811','待处理财产损溢',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (227,'1901','其他非流动资产',0,NULL,'ASSET',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (228,'2002','存入保证金',0,NULL,'LIAB',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (229,'2101','交易性金融负债',0,NULL,'LIAB',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (230,'2111','卖出回购金融资产款',0,NULL,'LIAB',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (231,'2204','合同负债',0,NULL,'LIAB',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (232,'222103','应交消费税',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (233,'222104','应交资源税',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-26 14:41:25',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (234,'222105','应交资源税',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (235,'222107','应交土地增值税',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (236,'222108','应交城市维护建设税',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (237,'222109','应交房产税',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (238,'222110','应交土地使用税',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (239,'222111','应交车船税',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (240,'222112','应交个人所得税',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (241,'222113','应交教育费附加',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (242,'222114','应交地方教育附加',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (243,'222115','应交印花税',0,NULL,'LIAB',3,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (244,'2232','应付股利',0,NULL,'LIAB',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (245,'2233','其他应付款-押金',0,NULL,'LIAB',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (246,'2234','应付暂收款',0,NULL,'LIAB',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (247,'2401','递延收益-政府补助',0,NULL,'LIAB',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (248,'2402','专项应付款',0,NULL,'LIAB',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (249,'2502','应付债券',0,NULL,'LIAB',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (250,'2701','长期应付款',0,NULL,'LIAB',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (251,'2702','未确认融资费用',0,NULL,'LIAB',2,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (252,'2711','预计负债',0,NULL,'LIAB',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (253,'2712','递延收益',0,NULL,'LIAB',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (254,'2801','递延所得税负债',0,NULL,'LIAB',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (255,'4003','其他综合收益',0,NULL,'EQUITY',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (256,'4104','库存股',0,NULL,'EQUITY',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (257,'4105','专项储备',0,NULL,'EQUITY',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (258,'4201','一般风险准备',0,NULL,'EQUITY',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (259,'4301','未分配利润',0,NULL,'EQUITY',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (260,'5101','制造费用',0,NULL,'COST',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (261,'5201','劳务成本',0,NULL,'COST',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (262,'5301','研发支出',0,NULL,'COST',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (263,'5501','合同履约成本',0,NULL,'COST',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (264,'5601','合同取得成本',0,NULL,'COST',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (265,'3001','清算资金往来',0,NULL,'COMMON',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (266,'3002','货币兑换',0,NULL,'COMMON',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (267,'3101','衍生工具',0,NULL,'COMMON',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (268,'3102','套期工具',0,NULL,'COMMON',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (269,'3103','被套期项目',0,NULL,'COMMON',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (270,'6011','利息收入',0,NULL,'INCOME',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (271,'6021','手续费及佣金收入',0,NULL,'INCOME',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (272,'6031','保费收入',0,NULL,'INCOME',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (273,'6041','租赁收入',0,NULL,'INCOME',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (274,'6061','汇兑收益',0,NULL,'INCOME',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (275,'6101','公允价值变动损益',0,NULL,'INCOME',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (276,'6201','以前年度损益调整',0,NULL,'INCOME',1,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (277,'6401','税金及附加',0,NULL,'EXPENSE',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (278,'6402','资源税',0,NULL,'EXPENSE',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (279,'6701','资产减值损失',0,NULL,'EXPENSE',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (280,'6702','信用减值损失',0,NULL,'EXPENSE',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (281,'6801','所得税费用',0,NULL,'EXPENSE',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (282,'6802','递延所得税费用',0,NULL,'EXPENSE',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (283,'6901','少数股东损益',0,NULL,'EXPENSE',1,1,'借方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,0,NULL,'2026-06-25 16:41:43','2026-06-25 16:41:43',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (284,'22210101','进项税额',160,'222101','LIAB',4,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-26 14:41:25','2026-06-26 14:41:25',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (285,'22210102','已交税金',160,'222101','LIAB',4,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-26 14:41:25','2026-06-26 14:41:25',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (286,'22210103','转出未交增值税',160,'222101','LIAB',4,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-26 14:41:25','2026-06-26 14:41:25',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (287,'22210104','减免税款',160,'222101','LIAB',4,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-26 14:41:25','2026-06-26 14:41:25',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (288,'22210105','销项税额',160,'222101','LIAB',4,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-26 14:41:25','2026-06-26 14:41:25',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (289,'22210106','出口退税',160,'222101','LIAB',4,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-26 14:41:25','2026-06-26 14:41:25',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (290,'22210107','进项税额转出',160,'222101','LIAB',4,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-26 14:41:25','2026-06-26 14:41:25',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (291,'22210108','转出多交增值税',160,'222101','LIAB',4,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-26 14:41:25','2026-06-26 14:41:25',0);
INSERT INTO `gl_account_subject` (`id`, `subject_code`, `subject_name`, `parent_id`, `parent_code`, `category_code`, `level`, `is_leaf`, `balance_direction`, `aux_customer`, `aux_supplier`, `aux_dept`, `aux_project`, `aux_employee`, `aux_product`, `currency_code`, `quantity_unit`, `is_cash_flow`, `cash_flow_dir`, `status`, `is_system`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (292,'4401','其他权益',259,'4','EQUITY',2,1,'贷方',0,0,0,0,0,0,'CNY',NULL,0,NULL,1,1,NULL,'2026-06-26 15:13:11','2026-06-26 15:13:11',0);
/*!40000 ALTER TABLE `gl_account_subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gl_auxiliary_item`
--

LOCK TABLES `gl_auxiliary_item` WRITE;
/*!40000 ALTER TABLE `gl_auxiliary_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `gl_auxiliary_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gl_auxiliary_type`
--

LOCK TABLES `gl_auxiliary_type` WRITE;
/*!40000 ALTER TABLE `gl_auxiliary_type` DISABLE KEYS */;
INSERT INTO `gl_auxiliary_type` (`id`, `type_code`, `type_name`, `table_name`, `status`, `sort_order`, `deleted`) VALUES (19,'CUSTOMER','客户',NULL,1,1,0);
INSERT INTO `gl_auxiliary_type` (`id`, `type_code`, `type_name`, `table_name`, `status`, `sort_order`, `deleted`) VALUES (20,'SUPPLIER','供应商',NULL,1,2,0);
INSERT INTO `gl_auxiliary_type` (`id`, `type_code`, `type_name`, `table_name`, `status`, `sort_order`, `deleted`) VALUES (21,'DEPT','部门',NULL,1,3,0);
INSERT INTO `gl_auxiliary_type` (`id`, `type_code`, `type_name`, `table_name`, `status`, `sort_order`, `deleted`) VALUES (22,'PROJECT','项目',NULL,1,4,0);
INSERT INTO `gl_auxiliary_type` (`id`, `type_code`, `type_name`, `table_name`, `status`, `sort_order`, `deleted`) VALUES (23,'EMPLOYEE','员工',NULL,1,5,0);
INSERT INTO `gl_auxiliary_type` (`id`, `type_code`, `type_name`, `table_name`, `status`, `sort_order`, `deleted`) VALUES (24,'PRODUCT','产品',NULL,1,6,0);
/*!40000 ALTER TABLE `gl_auxiliary_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gl_balance`
--

LOCK TABLES `gl_balance` WRITE;
/*!40000 ALTER TABLE `gl_balance` DISABLE KEYS */;
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (65,'202606','1001','CNY',48000.00,0.00,10000.00,8000.00,60000.00,48000.00,50000.00,0.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (66,'202606','1002','CNY',480000.00,0.00,350000.00,280000.00,2100000.00,1680000.00,550000.00,0.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (67,'202606','1122','CNY',120000.00,0.00,80000.00,50000.00,480000.00,300000.00,150000.00,0.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (68,'202606','1405','CNY',200000.00,0.00,60000.00,40000.00,360000.00,240000.00,220000.00,0.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (69,'202606','1601','CNY',800000.00,0.00,0.00,0.00,0.00,0.00,800000.00,0.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (70,'202606','1602','CNY',0.00,120000.00,0.00,8000.00,0.00,48000.00,0.00,128000.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (71,'202606','2202','CNY',0.00,80000.00,30000.00,50000.00,180000.00,300000.00,0.00,100000.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (72,'202606','222101','CNY',0.00,15000.00,5000.00,12000.00,30000.00,72000.00,0.00,22000.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (73,'202606','4001','CNY',0.00,1000000.00,0.00,0.00,0.00,0.00,0.00,1000000.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (74,'202606','4103','CNY',0.00,150000.00,0.00,50000.00,0.00,300000.00,0.00,200000.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (75,'202606','6001','CNY',0.00,0.00,0.00,200000.00,0.00,1200000.00,0.00,200000.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (76,'202606','6051','CNY',0.00,0.00,0.00,30000.00,0.00,180000.00,0.00,30000.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (77,'202606','5401','CNY',0.00,0.00,120000.00,0.00,720000.00,0.00,120000.00,0.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (78,'202606','6601','CNY',0.00,0.00,20000.00,0.00,120000.00,0.00,20000.00,0.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (79,'202606','6602','CNY',0.00,0.00,35000.00,0.00,210000.00,0.00,35000.00,0.00,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_balance` (`id`, `period_code`, `subject_code`, `currency_code`, `opening_debit`, `opening_credit`, `period_debit`, `period_credit`, `year_debit`, `year_credit`, `ending_debit`, `ending_credit`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`) VALUES (80,'202606','6603','CNY',0.00,0.00,5000.00,0.00,30000.00,0.00,5000.00,0.00,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `gl_balance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gl_carryover_template`
--

LOCK TABLES `gl_carryover_template` WRITE;
/*!40000 ALTER TABLE `gl_carryover_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `gl_carryover_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gl_cash_flow_item`
--

LOCK TABLES `gl_cash_flow_item` WRITE;
/*!40000 ALTER TABLE `gl_cash_flow_item` DISABLE KEYS */;
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (40,'CF01','销售商品、提供劳务收到的现金',0,'IN','MAIN',1,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (41,'CF02','收到的税费返还',0,'IN','MAIN',2,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (42,'CF03','收到其他与经营活动有关的现金',0,'IN','MAIN',3,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (43,'CF04','购买商品、接受劳务支付的现金',0,'OUT','MAIN',4,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (44,'CF05','支付给职工以及为职工支付的现金',0,'OUT','MAIN',5,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (45,'CF06','支付的各项税费',0,'OUT','MAIN',6,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (46,'CF07','支付其他与经营活动有关的现金',0,'OUT','MAIN',7,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (47,'CF08','收回投资收到的现金',0,'IN','MAIN',8,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (48,'CF09','取得投资收益收到的现金',0,'IN','MAIN',9,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (49,'CF10','投资支付的现金',0,'OUT','MAIN',10,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (50,'CF11','取得借款收到的现金',0,'IN','MAIN',11,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (51,'CF12','偿还债务支付的现金',0,'OUT','MAIN',12,1,0);
INSERT INTO `gl_cash_flow_item` (`id`, `item_code`, `item_name`, `parent_id`, `direction`, `flow_type`, `sort_order`, `status`, `deleted`) VALUES (52,'CF13','分配股利、利润或偿付利息支付的现金',0,'OUT','MAIN',13,1,0);
/*!40000 ALTER TABLE `gl_cash_flow_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gl_exchange_rate`
--

LOCK TABLES `gl_exchange_rate` WRITE;
/*!40000 ALTER TABLE `gl_exchange_rate` DISABLE KEYS */;
/*!40000 ALTER TABLE `gl_exchange_rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gl_period`
--

LOCK TABLES `gl_period` WRITE;
/*!40000 ALTER TABLE `gl_period` DISABLE KEYS */;
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (37,'202601','2026年1月','2026-01-01','2026-01-31',2026,1,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (38,'202602','2026年2月','2026-02-01','2026-02-28',2026,2,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (39,'202603','2026年3月','2026-03-01','2026-03-31',2026,3,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (40,'202604','2026年4月','2026-04-01','2026-04-30',2026,4,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (41,'202605','2026年5月','2026-05-01','2026-05-31',2026,5,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (42,'202606','2026年6月','2026-06-01','2026-06-30',2026,6,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (43,'202607','2026年7月','2026-07-01','2026-07-31',2026,7,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (44,'202608','2026年8月','2026-08-01','2026-08-31',2026,8,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (45,'202609','2026年9月','2026-09-01','2026-09-30',2026,9,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (46,'202610','2026年10月','2026-10-01','2026-10-31',2026,10,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (47,'202611','2026年11月','2026-11-01','2026-11-30',2026,11,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `gl_period` (`id`, `period_code`, `period_name`, `start_date`, `end_date`, `fiscal_year`, `period_index`, `status`, `is_adjust`, `create_time`, `update_time`, `deleted`) VALUES (48,'202612','2026年12月','2026-12-01','2026-12-31',2026,12,'OPEN',0,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
/*!40000 ALTER TABLE `gl_period` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gl_voucher`
--

LOCK TABLES `gl_voucher` WRITE;
/*!40000 ALTER TABLE `gl_voucher` DISABLE KEYS */;
INSERT INTO `gl_voucher` (`id`, `voucher_no`, `voucher_type`, `period_code`, `voucher_date`, `attach_count`, `total_debit`, `total_credit`, `summary`, `source`, `source_biz`, `source_id`, `status`, `is_cash`, `is_audit`, `create_by`, `create_by_name`, `audit_by`, `audit_by_name`, `audit_time`, `post_by`, `post_time`, `cancel_by`, `cancel_time`, `cancel_reason`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (1,'2026-06-0001','JZ','202606','2026-06-26',0,5987.00,5987.00,'','MANUAL',NULL,NULL,'DRAFT',0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-06-26 10:21:00','2026-06-26 02:20:51',0);
INSERT INTO `gl_voucher` (`id`, `voucher_no`, `voucher_type`, `period_code`, `voucher_date`, `attach_count`, `total_debit`, `total_credit`, `summary`, `source`, `source_biz`, `source_id`, `status`, `is_cash`, `is_audit`, `create_by`, `create_by_name`, `audit_by`, `audit_by_name`, `audit_time`, `post_by`, `post_time`, `cancel_by`, `cancel_time`, `cancel_reason`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (2,'2026-06-0002','JZ','202606','2026-06-26',0,11300.00,11300.00,'销售商品','MANUAL',NULL,NULL,'DRAFT',0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-06-26 14:34:16','2026-06-26 14:34:16',0);
INSERT INTO `gl_voucher` (`id`, `voucher_no`, `voucher_type`, `period_code`, `voucher_date`, `attach_count`, `total_debit`, `total_credit`, `summary`, `source`, `source_biz`, `source_id`, `status`, `is_cash`, `is_audit`, `create_by`, `create_by_name`, `audit_by`, `audit_by_name`, `audit_time`, `post_by`, `post_time`, `cancel_by`, `cancel_time`, `cancel_reason`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (3,'2026-06-0003','JZ','202606','2026-06-20',0,11300.00,11300.00,'采购商品','MANUAL',NULL,NULL,'DRAFT',0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-06-26 14:34:16','2026-06-26 14:34:16',0);
INSERT INTO `gl_voucher` (`id`, `voucher_no`, `voucher_type`, `period_code`, `voucher_date`, `attach_count`, `total_debit`, `total_credit`, `summary`, `source`, `source_biz`, `source_id`, `status`, `is_cash`, `is_audit`, `create_by`, `create_by_name`, `audit_by`, `audit_by_name`, `audit_time`, `post_by`, `post_time`, `cancel_by`, `cancel_time`, `cancel_reason`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (4,'2026-06-0004','JZ','202606','2026-06-15',0,5300.00,5300.00,'支付广告费','MANUAL',NULL,NULL,'DRAFT',0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-06-26 14:34:16','2026-06-26 14:34:16',0);
INSERT INTO `gl_voucher` (`id`, `voucher_no`, `voucher_type`, `period_code`, `voucher_date`, `attach_count`, `total_debit`, `total_credit`, `summary`, `source`, `source_biz`, `source_id`, `status`, `is_cash`, `is_audit`, `create_by`, `create_by_name`, `audit_by`, `audit_by_name`, `audit_time`, `post_by`, `post_time`, `cancel_by`, `cancel_time`, `cancel_reason`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (5,'2026-07-0005','JZ','202607','2026-07-15',0,56500.00,56500.00,'销售商品','MANUAL',NULL,NULL,'DRAFT',0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-06-26 14:37:18','2026-06-26 14:37:18',0);
INSERT INTO `gl_voucher` (`id`, `voucher_no`, `voucher_type`, `period_code`, `voucher_date`, `attach_count`, `total_debit`, `total_credit`, `summary`, `source`, `source_biz`, `source_id`, `status`, `is_cash`, `is_audit`, `create_by`, `create_by_name`, `audit_by`, `audit_by_name`, `audit_time`, `post_by`, `post_time`, `cancel_by`, `cancel_time`, `cancel_reason`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (6,'2026-07-0006','JZ','202607','2026-07-10',0,2260.00,2260.00,'采购办公用品','MANUAL',NULL,NULL,'DRAFT',0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-06-26 14:37:18','2026-06-26 14:37:18',0);
/*!40000 ALTER TABLE `gl_voucher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `gl_voucher_entry`
--

LOCK TABLES `gl_voucher_entry` WRITE;
/*!40000 ALTER TABLE `gl_voucher_entry` DISABLE KEYS */;
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (1,1,1,'1001','库存现金','DEBIT',5987.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (2,2,1,'1002','银行存款','DEBIT',11300.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (3,2,2,'6001','主营业务收入','CREDIT',10000.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (4,2,3,'22210105','销项税额','CREDIT',1300.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (5,3,1,'1405','库存商品','DEBIT',10000.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (6,3,2,'22210101','进项税额','DEBIT',1300.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (7,3,3,'1002','银行存款','CREDIT',11300.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (8,4,1,'6601','销售费用','DEBIT',5000.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (9,4,2,'22210101','进项税额','DEBIT',300.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (10,4,3,'1002','银行存款','CREDIT',5300.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (11,5,1,'1002','银行存款','DEBIT',56500.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (12,5,2,'6001','主营业务收入','CREDIT',50000.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (13,5,3,'22210105','销项税额','CREDIT',6500.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (14,6,1,'6602','管理费用','DEBIT',2000.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (15,6,2,'22210101','进项税额','DEBIT',260.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `gl_voucher_entry` (`id`, `voucher_id`, `line_no`, `subject_code`, `subject_name`, `dc_direction`, `amount`, `currency_code`, `orig_amount`, `exchange_rate`, `quantity`, `price`, `aux_customer_id`, `aux_supplier_id`, `aux_dept_id`, `aux_project_id`, `aux_employee_id`, `aux_product_id`, `aux_summary`, `cash_flow_id`) VALUES (16,6,3,'1002','银行存款','CREDIT',2260.00,'CNY',NULL,1.00000000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `gl_voucher_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `inv_balance`
--

LOCK TABLES `inv_balance` WRITE;
/*!40000 ALTER TABLE `inv_balance` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_balance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `inv_category`
--

LOCK TABLES `inv_category` WRITE;
/*!40000 ALTER TABLE `inv_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `inv_item`
--

LOCK TABLES `inv_item` WRITE;
/*!40000 ALTER TABLE `inv_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `inv_transaction`
--

LOCK TABLES `inv_transaction` WRITE;
/*!40000 ALTER TABLE `inv_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `inv_warehouse`
--

LOCK TABLES `inv_warehouse` WRITE;
/*!40000 ALTER TABLE `inv_warehouse` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_warehouse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `prj_budget`
--

LOCK TABLES `prj_budget` WRITE;
/*!40000 ALTER TABLE `prj_budget` DISABLE KEYS */;
INSERT INTO `prj_budget` (`id`, `project_id`, `budget_type`, `subject_code`, `amount`, `remark`, `create_time`, `deleted`) VALUES (1,1,'COST','6602',500000.00,'管理费用预算','2026-06-25 15:58:04',0);
/*!40000 ALTER TABLE `prj_budget` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `prj_cost`
--

LOCK TABLES `prj_cost` WRITE;
/*!40000 ALTER TABLE `prj_cost` DISABLE KEYS */;
/*!40000 ALTER TABLE `prj_cost` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `prj_ledger`
--

LOCK TABLES `prj_ledger` WRITE;
/*!40000 ALTER TABLE `prj_ledger` DISABLE KEYS */;
INSERT INTO `prj_ledger` (`id`, `project_id`, `project_name`, `business_type`, `business_id`, `business_no`, `summary`, `amount`, `handle_date`) VALUES (2,1,'ERP实施项目','EXPENSE',1,'EXP-001','差旅费报销',3500.00,'2026-06-20');
/*!40000 ALTER TABLE `prj_ledger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `prj_project`
--

LOCK TABLES `prj_project` WRITE;
/*!40000 ALTER TABLE `prj_project` DISABLE KEYS */;
/*!40000 ALTER TABLE `prj_project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `rpt_custom`
--

LOCK TABLES `rpt_custom` WRITE;
/*!40000 ALTER TABLE `rpt_custom` DISABLE KEYS */;
INSERT INTO `rpt_custom` (`id`, `report_code`, `report_name`, `report_type`, `fiscal_year`, `fiscal_period`, `config_json`, `status`, `creator_id`, `create_time`, `deleted`) VALUES (2,'RPT-DEMO-001','部门费用分析','CUSTOM',2026,6,'{}','A',NULL,'2026-06-25 16:11:18',0);
/*!40000 ALTER TABLE `rpt_custom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `rpt_template`
--

LOCK TABLES `rpt_template` WRITE;
/*!40000 ALTER TABLE `rpt_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `rpt_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_company`
--

LOCK TABLES `sys_company` WRITE;
/*!40000 ALTER TABLE `sys_company` DISABLE KEYS */;
INSERT INTO `sys_company` (`id`, `company_code`, `company_name`, `short_name`, `tax_no`, `legal_person`, `address`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (2,'C001','示例科技有限公司','示例科技','91110000MA00000000','张三','北京市朝阳区示例路1号',1,NULL,'2026-06-25 16:25:35','2026-06-25 16:25:35',0);
/*!40000 ALTER TABLE `sys_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (25,'当前会计期间','sys.current_period','202601','SYSTEM',NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (26,'本位币','sys.base_currency','CNY','SYSTEM',NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (27,'凭证是否需要出纳签字','gl.voucher.cash_sign','false','BUSINESS',NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (28,'凭证是否需要主管审核','gl.voucher.audit_required','true','BUSINESS',NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (29,'预算超支控制','budget.overrun_control','WARN','BUSINESS',NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (30,'结账控制','gl.closing.lock_period','true','BUSINESS',NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (31,'小数位数(金额)','sys.decimal.amount','2','SYSTEM',NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (32,'小数位数(单价)','sys.decimal.price','4','SYSTEM',NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` (`id`, `parent_id`, `ancestors`, `dept_code`, `dept_name`, `sort_order`, `leader`, `phone`, `email`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (1,0,'','D001','总经办',0,'张总',NULL,NULL,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dept` (`id`, `parent_id`, `ancestors`, `dept_code`, `dept_name`, `sort_order`, `leader`, `phone`, `email`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (2,1,'','D002','财务部',1,'王会计',NULL,NULL,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dept` (`id`, `parent_id`, `ancestors`, `dept_code`, `dept_name`, `sort_order`, `leader`, `phone`, `email`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (3,1,'','D003','业务部',2,'李经理',NULL,NULL,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dept` (`id`, `parent_id`, `ancestors`, `dept_code`, `dept_name`, `sort_order`, `leader`, `phone`, `email`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (4,1,'','D004','行政部',3,'赵主管',NULL,NULL,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (115,'voucher_type','记账凭证','JZ',1,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (116,'voucher_type','收款凭证','SK',2,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (117,'voucher_type','付款凭证','FK',3,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (118,'voucher_type','转账凭证','ZZ',4,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (119,'doc_status','草稿','DRAFT',1,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (120,'doc_status','待审核','PENDING',2,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (121,'doc_status','已审核','APPROVED',3,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (122,'doc_status','已驳回','REJECTED',4,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (123,'doc_status','已过账','POSTED',5,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (124,'doc_status','已作废','CANCELLED',6,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (125,'account_category','资产','ASSET',1,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (126,'account_category','负债','LIAB',2,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (127,'account_category','所有者权益','EQUITY',3,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (128,'account_category','收入','INCOME',4,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (129,'account_category','费用','EXPENSE',5,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (130,'account_category','成本','COST',6,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (131,'depr_method','平均年限法','STRAIGHT',1,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (132,'depr_method','工作量法','WORKLOAD',2,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (133,'depr_method','双倍余额递减法','DOUBLE_DECLINE',3,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (134,'depr_method','年数总和法','SUM_YEARS',4,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (135,'cost_method','全月一次加权平均法','WAM',1,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (136,'trans_type','收入','IN',1,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (137,'trans_type','支出','OUT',2,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (138,'trans_type','转账','TRANSFER',3,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (139,'bill_type','支票','CHEQUE',1,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (140,'bill_type','银行承兑汇票','BANK_ACCEPTANCE',2,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (141,'bill_type','商业承兑汇票','COMMERCIAL',3,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (142,'bill_status','持有','HOLD',1,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (143,'bill_status','已使用','USE',2,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (144,'bill_status','已背书','ENDORSE',3,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (145,'bill_status','已贴现','DISCOUNT',4,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (146,'bill_status','已收款','RECEIVE',5,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (147,'asset_type','固定资产','FIXED',1,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (148,'asset_type','无形资产','INTANGIBLE',2,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (149,'asset_type','投资性房地产','INVESTMENT',3,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (150,'doc_source','手工','MANUAL',1,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (151,'doc_source','导入','IMPORT',2,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (152,'doc_source','自动','AUTO',3,0,1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (31,'voucher_type','凭证类型',1,'收/付/转/记','2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (32,'doc_status','单据状态',1,'通用状态','2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (33,'account_category','科目类别',1,'资产/负债/权益/收入/费用/成本','2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (34,'depr_method','折旧方法',1,'固定资产折旧方法','2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (35,'cost_method','成本核算方法',1,'存货核算方法','2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (36,'trans_type','收付类型',1,'日记账方向','2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (37,'bill_type','票据类型',1,'支票/汇票/本票','2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (38,'bill_status','票据状态',1,'持有/背书/贴现','2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (39,'asset_type','资产类型',1,'固定资产/无形资产/投资性房地产','2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (40,'doc_source','单据来源',1,'手工/导入/自动','2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (41,'dt2590','D2590',1,NULL,'2026-06-26 16:49:43','2026-06-26 16:49:43',0);
INSERT INTO `sys_dict_type` (`id`, `dict_type`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (42,'dt2623','D2623',1,NULL,'2026-06-26 16:50:16','2026-06-26 16:50:16',0);
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_file`
--

LOCK TABLES `sys_file` WRITE;
/*!40000 ALTER TABLE `sys_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_log_login`
--

LOCK TABLES `sys_log_login` WRITE;
/*!40000 ALTER TABLE `sys_log_login` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log_login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_log_operation`
--

LOCK TABLES `sys_log_operation` WRITE;
/*!40000 ALTER TABLE `sys_log_operation` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log_operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1,0,'系统管理','M','system','Layout','','Setting',1,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (2,0,'基础资料','M','base','Layout','','Files',2,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (3,0,'总账','M','gl','Layout','','Notebook',3,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (4,0,'出纳','M','cash','Layout','','Money',4,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (5,0,'固定资产','M','fa','Layout','','Box',5,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (6,0,'应收账款','M','ar','Layout','','CreditCard',6,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (7,0,'应付账款','M','ap','Layout','','Wallet',7,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (8,0,'存货核算','M','inventory','Layout','','Goods',8,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (9,0,'成本管理','M','cost','Layout','','DataAnalysis',9,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (10,0,'预算管理','M','budget','Layout','','TrendCharts',10,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (11,0,'费用报销','M','expense','Layout','','Tickets',11,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (12,0,'合同管理','M','contract','Layout','','Document',12,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (13,0,'项目管理','M','project','Layout','','Connection',13,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (14,0,'合并报表','M','consolidation','Layout','','Share',14,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (15,0,'财务报表','M','report','Layout','','PieChart',15,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (101,1,'用户管理','C','user','system/user/index','system:user:list','User',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (102,1,'角色管理','C','role','system/role/index','system:role:list','UserFilled',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (103,1,'菜单管理','C','menu','system/menu/index','system:menu:list','Menu',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (104,1,'部门管理','C','dept','system/dept/index','system:dept:list','OfficeBuilding',4,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (105,1,'字典管理','C','dict','system/dict/index','system:dict:list','Collection',5,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (106,1,'参数设置','C','config','system/config/index','system:config:list','Tools',6,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (107,1,'登录日志','C','loginLog','system/log/login','system:log:login','Document',7,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (108,1,'操作日志','C','opLog','system/log/operation','system:log:operation','Edit',8,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (109,1,'公司主体','C','company','system/company/index','system:company:list','School',9,1,1,'2026-06-25 16:07:33','2026-06-26 15:35:16',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (201,2,'会计科目','C','subject','gl/subject/index','gl:subject:list','List',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (202,2,'辅助核算','C','aux','gl/auxiliary/index','gl:aux:list','CollectionTag',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (203,2,'会计期间','C','period','gl/period/index','gl:period:list','Calendar',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (204,2,'客户档案','C','customer','ar/customer/index','ar:customer:list','Avatar',4,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (205,2,'供应商档案','C','supplier','ap/supplier/index','ap:supplier:list','Avatar',5,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (206,2,'物料档案','C','item','inventory/item/index','inventory:item:list','Goods',6,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (207,2,'仓库管理','C','warehouse','inventory/warehouse/index','inventory:warehouse:list','House',7,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (301,3,'凭证录入','C','voucher','gl/voucher/index','gl:voucher:list','EditPen',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (302,3,'凭证管理','C','voucherList','gl/voucher/list','gl:voucher:list','Document',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (303,3,'账簿查询','C','ledger','gl/ledger/index','gl:ledger:list','Notebook',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (304,3,'结账管理','C','closing','gl/closing/index','gl:closing:list','Lock',4,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (305,3,'现金流量','C','cashFlow','gl/cashflow/index','gl:cashflow:list','Money',5,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (306,301,'凭证新增','F','','','gl:voucher:add','',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (307,301,'凭证修改','F','','','gl:voucher:edit','',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (308,301,'凭证删除','F','','','gl:voucher:delete','',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (309,301,'凭证审核','F','','','gl:voucher:audit','',4,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (310,301,'凭证过账','F','','','gl:voucher:post','',5,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (311,301,'凭证冲销','F','','','gl:voucher:reverse','',6,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (401,4,'资金账户','C','account','cash/account/index','cash:account:list','CreditCard',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (402,4,'日记账','C','journal','cash/journal/index','cash:journal:list','Notebook',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (403,4,'银行对账','C','reconcile','cash/reconcile/index','cash:reconcile:list','Refresh',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (404,4,'票据管理','C','bill','cash/bill/index','cash:bill:list','Ticket',4,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (501,5,'资产类别','C','category','fa/category/index','fa:category:list','Menu',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (502,5,'资产卡片','C','asset','fa/asset/index','fa:asset:list','Box',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (503,5,'折旧管理','C','depreciation','fa/depreciation/index','fa:depreciation:list','TrendCharts',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (504,5,'资产处置','C','disposal','fa/disposal/index','fa:disposal:list','Delete',4,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (505,5,'资产盘点','C','inventory','fa/inventory/index','fa:inventory:list','List',5,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (601,6,'销售发票','C','invoice','ar/invoice/index','ar:invoice:list','Tickets',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (602,6,'收款单','C','receipt','ar/receipt/index','ar:receipt:list','Wallet',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (603,6,'核销管理','C','writeoff','ar/writeoff/index','ar:writeoff:list','Connection',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (604,6,'应收票据','C','bill','ar/bill/index','ar:bill:list','Money',4,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (605,6,'账龄分析','C','aging','ar/aging/index','ar:aging:list','DataLine',5,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (701,7,'采购发票','C','invoice','ap/invoice/index','ap:invoice:list','Tickets',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (702,7,'付款单','C','payment','ap/payment/index','ap:payment:list','CreditCard',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (703,7,'核销管理','C','writeoff','ap/writeoff/index','ap:writeoff:list','Connection',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (704,7,'应付票据','C','bill','ap/bill/index','ap:bill:list','Money',4,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (705,7,'账龄分析','C','aging','ap/aging/index','ap:aging:list','DataLine',5,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (801,8,'收发存明细','C','transaction','inventory/transaction/index','inventory:trans:list','List',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (802,8,'库存余额','C','balance','inventory/balance/index','inventory:balance:list','PieChart',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (803,8,'成本调整','C','adjust','inventory/adjust/index','inventory:adjust:list','Tools',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (901,9,'成本中心','C','center','cost/center/index','cost:center:list','OfficeBuilding',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (902,9,'成本要素','C','element','cost/element/index','cost:element:list','Collection',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (903,9,'成本计算','C','calc','cost/calc/index','cost:calc:list','DataAnalysis',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1001,10,'预算编制','C','budget','budget/master/index','budget:master:list','Edit',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1002,10,'预算执行','C','exec','budget/exec/index','budget:exec:list','TrendCharts',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1003,10,'预算分析','C','analysis','budget/analysis/index','budget:analysis:list','DataAnalysis',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1101,11,'我的申请','C','apply','expense/apply/index','expense:apply:list','EditPen',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1102,11,'我的报销','C','repay','expense/repay/index','expense:repay:list','Wallet',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1103,11,'我的借款','C','loan','expense/loan/index','expense:loan:list','CreditCard',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1201,12,'销售合同','C','sales','contract/sales/index','contract:sales:list','Document',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1202,12,'采购合同','C','purchase','contract/purchase/index','contract:purchase:list','Document',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1301,13,'项目列表','C','list','project/index','project:list','List',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1302,13,'项目预算','C','budget','project/budget/index','project:budget:list','Money',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1303,13,'项目成本','C','cost','project/cost/index','project:cost:list','DataAnalysis',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1401,14,'集团架构','C','group','consolidation/group/index','consolidation:group:list','Connection',1,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1402,14,'抵消分录','C','elim','consolidation/elim/index','consolidation:elim:list','Remove',2,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1403,14,'合并底稿','C','worksheet','consolidation/worksheet/index','consolidation:worksheet:list','DataLine',3,1,1,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1501,15,'资产负债表','C','bs','report/bs/index','report:bs','PieChart',1,1,1,'2026-06-25 16:07:33','2026-06-26 16:12:47',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1502,15,'利润表','C','is','report/is/index','report:is','DataLine',2,1,1,'2026-06-25 16:07:33','2026-06-26 16:12:47',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1503,15,'现金流量表','C','cf','report/cf/index','report:cf','Money',3,1,1,'2026-06-25 16:07:33','2026-06-26 16:12:47',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1504,15,'自定义报表','C','report/custom','report/Custom','report:custom','EditPen',4,1,1,'2026-06-25 16:07:33','2026-06-26 15:09:54',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1505,1,'system:user:add权限','button',NULL,NULL,'system:user:add',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1506,1,'system:user:edit权限','button',NULL,NULL,'system:user:edit',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1507,1,'system:user:delete权限','button',NULL,NULL,'system:user:delete',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1508,1,'system:user:resetPwd权限','button',NULL,NULL,'system:user:resetPwd',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1509,1,'system:role:add权限','button',NULL,NULL,'system:role:add',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1510,1,'system:role:edit权限','button',NULL,NULL,'system:role:edit',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1511,1,'system:role:delete权限','button',NULL,NULL,'system:role:delete',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1512,1,'system:menu:add权限','button',NULL,NULL,'system:menu:add',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1513,1,'system:menu:edit权限','button',NULL,NULL,'system:menu:edit',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1514,1,'system:menu:delete权限','button',NULL,NULL,'system:menu:delete',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1515,1,'system:dept:add权限','button',NULL,NULL,'system:dept:add',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1516,1,'system:dept:edit权限','button',NULL,NULL,'system:dept:edit',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1517,1,'system:dept:delete权限','button',NULL,NULL,'system:dept:delete',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1518,1,'system:dict:add权限','button',NULL,NULL,'system:dict:add',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1519,1,'system:dict:edit权限','button',NULL,NULL,'system:dict:edit',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1520,1,'system:dict:delete权限','button',NULL,NULL,'system:dict:delete',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1521,1,'system:config:add权限','button',NULL,NULL,'system:config:add',NULL,99,0,1,'2026-06-26 13:22:26','2026-06-26 13:22:26',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1522,1,'system:config:edit权限','button',NULL,NULL,'system:config:edit',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1523,1,'system:config:delete权限','button',NULL,NULL,'system:config:delete',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1524,1,'system:company:add权限','button',NULL,NULL,'system:company:add',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1525,1,'system:company:edit权限','button',NULL,NULL,'system:company:edit',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1526,1,'system:company:delete权限','button',NULL,NULL,'system:company:delete',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1527,2,'gl:subject:add权限','button',NULL,NULL,'gl:subject:add',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1528,2,'gl:subject:edit权限','button',NULL,NULL,'gl:subject:edit',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1529,2,'gl:subject:delete权限','button',NULL,NULL,'gl:subject:delete',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1530,2,'gl:aux:add权限','button',NULL,NULL,'gl:aux:add',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1531,2,'gl:aux:edit权限','button',NULL,NULL,'gl:aux:edit',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1532,2,'gl:aux:delete权限','button',NULL,NULL,'gl:aux:delete',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1533,2,'gl:period:add权限','button',NULL,NULL,'gl:period:add',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1534,2,'gl:period:edit权限','button',NULL,NULL,'gl:period:edit',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1535,2,'gl:period:delete权限','button',NULL,NULL,'gl:period:delete',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1536,2,'ar:customer:add权限','button',NULL,NULL,'ar:customer:add',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1537,2,'ar:customer:edit权限','button',NULL,NULL,'ar:customer:edit',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1538,2,'ar:customer:delete权限','button',NULL,NULL,'ar:customer:delete',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1539,6,'ar:invoice:add权限','button',NULL,NULL,'ar:invoice:add',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1540,6,'ar:invoice:edit权限','button',NULL,NULL,'ar:invoice:edit',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1541,6,'ar:invoice:delete权限','button',NULL,NULL,'ar:invoice:delete',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1542,6,'ar:receipt:add权限','button',NULL,NULL,'ar:receipt:add',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1543,6,'ar:receipt:edit权限','button',NULL,NULL,'ar:receipt:edit',NULL,99,0,1,'2026-06-26 13:22:27','2026-06-26 13:22:27',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1544,6,'ar:receipt:delete权限','button',NULL,NULL,'ar:receipt:delete',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1545,6,'ar:writeoff:add权限','button',NULL,NULL,'ar:writeoff:add',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1546,6,'ar:writeoff:edit权限','button',NULL,NULL,'ar:writeoff:edit',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1547,6,'ar:writeoff:delete权限','button',NULL,NULL,'ar:writeoff:delete',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1548,6,'ar:bill:add权限','button',NULL,NULL,'ar:bill:add',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1549,6,'ar:bill:edit权限','button',NULL,NULL,'ar:bill:edit',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1550,6,'ar:bill:delete权限','button',NULL,NULL,'ar:bill:delete',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1551,2,'ap:supplier:add权限','button',NULL,NULL,'ap:supplier:add',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1552,2,'ap:supplier:edit权限','button',NULL,NULL,'ap:supplier:edit',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1553,2,'ap:supplier:delete权限','button',NULL,NULL,'ap:supplier:delete',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1554,7,'ap:invoice:add权限','button',NULL,NULL,'ap:invoice:add',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1555,7,'ap:invoice:edit权限','button',NULL,NULL,'ap:invoice:edit',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1556,7,'ap:invoice:delete权限','button',NULL,NULL,'ap:invoice:delete',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1557,7,'ap:payment:add权限','button',NULL,NULL,'ap:payment:add',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1558,7,'ap:payment:edit权限','button',NULL,NULL,'ap:payment:edit',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1559,7,'ap:payment:delete权限','button',NULL,NULL,'ap:payment:delete',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1560,7,'ap:writeoff:add权限','button',NULL,NULL,'ap:writeoff:add',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1561,7,'ap:writeoff:edit权限','button',NULL,NULL,'ap:writeoff:edit',NULL,99,0,1,'2026-06-26 13:22:28','2026-06-26 13:22:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1562,7,'ap:writeoff:delete权限','button',NULL,NULL,'ap:writeoff:delete',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1563,7,'ap:bill:add权限','button',NULL,NULL,'ap:bill:add',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1564,7,'ap:bill:edit权限','button',NULL,NULL,'ap:bill:edit',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1565,7,'ap:bill:delete权限','button',NULL,NULL,'ap:bill:delete',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1566,2,'inventory:item:add权限','button',NULL,NULL,'inventory:item:add',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1567,2,'inventory:item:edit权限','button',NULL,NULL,'inventory:item:edit',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1568,2,'inventory:item:delete权限','button',NULL,NULL,'inventory:item:delete',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1569,2,'inventory:warehouse:add权限','button',NULL,NULL,'inventory:warehouse:add',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1570,2,'inventory:warehouse:edit权限','button',NULL,NULL,'inventory:warehouse:edit',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1571,2,'inventory:warehouse:delete权限','button',NULL,NULL,'inventory:warehouse:delete',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1572,8,'inventory:trans:add权限','button',NULL,NULL,'inventory:trans:add',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1573,8,'inventory:trans:edit权限','button',NULL,NULL,'inventory:trans:edit',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1574,8,'inventory:trans:delete权限','button',NULL,NULL,'inventory:trans:delete',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1575,8,'inventory:adjust:add权限','button',NULL,NULL,'inventory:adjust:add',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1576,8,'inventory:adjust:edit权限','button',NULL,NULL,'inventory:adjust:edit',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1577,8,'inventory:adjust:delete权限','button',NULL,NULL,'inventory:adjust:delete',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1578,4,'cash:account:add权限','button',NULL,NULL,'cash:account:add',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1579,4,'cash:account:edit权限','button',NULL,NULL,'cash:account:edit',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1580,4,'cash:account:delete权限','button',NULL,NULL,'cash:account:delete',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1581,4,'cash:journal:add权限','button',NULL,NULL,'cash:journal:add',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1582,4,'cash:journal:edit权限','button',NULL,NULL,'cash:journal:edit',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1583,4,'cash:journal:delete权限','button',NULL,NULL,'cash:journal:delete',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1584,4,'cash:reconcile:add权限','button',NULL,NULL,'cash:reconcile:add',NULL,99,0,1,'2026-06-26 13:22:29','2026-06-26 13:22:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1585,4,'cash:reconcile:edit权限','button',NULL,NULL,'cash:reconcile:edit',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1586,4,'cash:reconcile:delete权限','button',NULL,NULL,'cash:reconcile:delete',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1587,4,'cash:bill:add权限','button',NULL,NULL,'cash:bill:add',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1588,4,'cash:bill:edit权限','button',NULL,NULL,'cash:bill:edit',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1589,4,'cash:bill:delete权限','button',NULL,NULL,'cash:bill:delete',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1590,5,'fa:category:add权限','button',NULL,NULL,'fa:category:add',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1591,5,'fa:category:edit权限','button',NULL,NULL,'fa:category:edit',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1592,5,'fa:category:delete权限','button',NULL,NULL,'fa:category:delete',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1593,5,'fa:asset:add权限','button',NULL,NULL,'fa:asset:add',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1594,5,'fa:asset:edit权限','button',NULL,NULL,'fa:asset:edit',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1595,5,'fa:asset:delete权限','button',NULL,NULL,'fa:asset:delete',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1596,5,'fa:depreciation:add权限','button',NULL,NULL,'fa:depreciation:add',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1597,5,'fa:depreciation:edit权限','button',NULL,NULL,'fa:depreciation:edit',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1598,5,'fa:depreciation:delete权限','button',NULL,NULL,'fa:depreciation:delete',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1599,5,'fa:disposal:add权限','button',NULL,NULL,'fa:disposal:add',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1600,5,'fa:disposal:edit权限','button',NULL,NULL,'fa:disposal:edit',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1601,5,'fa:disposal:delete权限','button',NULL,NULL,'fa:disposal:delete',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1602,5,'fa:inventory:add权限','button',NULL,NULL,'fa:inventory:add',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1603,5,'fa:inventory:edit权限','button',NULL,NULL,'fa:inventory:edit',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1604,5,'fa:inventory:delete权限','button',NULL,NULL,'fa:inventory:delete',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1605,9,'cost:center:add权限','button',NULL,NULL,'cost:center:add',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1606,9,'cost:center:edit权限','button',NULL,NULL,'cost:center:edit',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1607,9,'cost:center:delete权限','button',NULL,NULL,'cost:center:delete',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1608,9,'cost:element:add权限','button',NULL,NULL,'cost:element:add',NULL,99,0,1,'2026-06-26 13:22:30','2026-06-26 13:22:30',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1609,9,'cost:element:edit权限','button',NULL,NULL,'cost:element:edit',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1610,9,'cost:element:delete权限','button',NULL,NULL,'cost:element:delete',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1611,9,'cost:calc:add权限','button',NULL,NULL,'cost:calc:add',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1612,9,'cost:calc:edit权限','button',NULL,NULL,'cost:calc:edit',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1613,9,'cost:calc:delete权限','button',NULL,NULL,'cost:calc:delete',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1614,10,'budget:master:add权限','button',NULL,NULL,'budget:master:add',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1615,10,'budget:master:edit权限','button',NULL,NULL,'budget:master:edit',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1616,10,'budget:master:delete权限','button',NULL,NULL,'budget:master:delete',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1617,10,'budget:exec:add权限','button',NULL,NULL,'budget:exec:add',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1618,10,'budget:exec:edit权限','button',NULL,NULL,'budget:exec:edit',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1619,10,'budget:exec:delete权限','button',NULL,NULL,'budget:exec:delete',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1620,11,'expense:apply:add权限','button',NULL,NULL,'expense:apply:add',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1621,11,'expense:apply:edit权限','button',NULL,NULL,'expense:apply:edit',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1622,11,'expense:apply:delete权限','button',NULL,NULL,'expense:apply:delete',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1623,11,'expense:repay:add权限','button',NULL,NULL,'expense:repay:add',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1624,11,'expense:repay:edit权限','button',NULL,NULL,'expense:repay:edit',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1625,11,'expense:repay:delete权限','button',NULL,NULL,'expense:repay:delete',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1626,11,'expense:loan:add权限','button',NULL,NULL,'expense:loan:add',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1627,11,'expense:loan:edit权限','button',NULL,NULL,'expense:loan:edit',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1628,11,'expense:loan:delete权限','button',NULL,NULL,'expense:loan:delete',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1629,12,'contract:sales:add权限','button',NULL,NULL,'contract:sales:add',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1630,12,'contract:sales:edit权限','button',NULL,NULL,'contract:sales:edit',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1631,12,'contract:sales:delete权限','button',NULL,NULL,'contract:sales:delete',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1632,12,'contract:purchase:add权限','button',NULL,NULL,'contract:purchase:add',NULL,99,0,1,'2026-06-26 13:22:31','2026-06-26 13:22:31',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1633,12,'contract:purchase:edit权限','button',NULL,NULL,'contract:purchase:edit',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1634,12,'contract:purchase:delete权限','button',NULL,NULL,'contract:purchase:delete',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1635,13,'project:add权限','button',NULL,NULL,'project:add',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1636,13,'project:edit权限','button',NULL,NULL,'project:edit',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1637,13,'project:delete权限','button',NULL,NULL,'project:delete',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1638,13,'project:budget:add权限','button',NULL,NULL,'project:budget:add',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1639,13,'project:budget:edit权限','button',NULL,NULL,'project:budget:edit',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1640,13,'project:budget:delete权限','button',NULL,NULL,'project:budget:delete',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1641,13,'project:cost:add权限','button',NULL,NULL,'project:cost:add',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1642,13,'project:cost:edit权限','button',NULL,NULL,'project:cost:edit',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1643,13,'project:cost:delete权限','button',NULL,NULL,'project:cost:delete',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1644,14,'consolidation:group:add权限','button',NULL,NULL,'consolidation:group:add',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1645,14,'consolidation:group:edit权限','button',NULL,NULL,'consolidation:group:edit',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1646,14,'consolidation:group:delete权限','button',NULL,NULL,'consolidation:group:delete',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1647,14,'consolidation:elim:add权限','button',NULL,NULL,'consolidation:elim:add',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1648,14,'consolidation:elim:edit权限','button',NULL,NULL,'consolidation:elim:edit',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1649,14,'consolidation:elim:delete权限','button',NULL,NULL,'consolidation:elim:delete',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1650,14,'consolidation:worksheet:add权限','button',NULL,NULL,'consolidation:worksheet:add',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1651,14,'consolidation:worksheet:edit权限','button',NULL,NULL,'consolidation:worksheet:edit',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1652,14,'consolidation:worksheet:delete权限','button',NULL,NULL,'consolidation:worksheet:delete',NULL,99,0,1,'2026-06-26 13:22:32','2026-06-26 13:22:32',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1653,1654,'增值税申报','C','vat','tax/Vat','tax:vat','Tickets',1,1,1,'2026-06-26 14:38:40','2026-06-26 15:35:18',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1654,0,'税务管理','M','tax','Layout',NULL,'el-icon-document',17,1,1,'2026-06-26 15:10:01','2026-06-26 16:00:06',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1655,0,'M2590','M','/2590','a',NULL,NULL,0,1,1,'2026-06-26 16:49:43','2026-06-26 16:49:43',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perm_code`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1656,0,'M2623','M','/2623','a',NULL,NULL,0,1,1,'2026-06-26 16:50:15','2026-06-26 16:50:15',0);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `sort_order`, `data_scope`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (1,'ADMIN','系统管理员',0,'ALL',1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `sort_order`, `data_scope`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (2,'FINANCE','财务人员',1,'ALL',1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `sort_order`, `data_scope`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (3,'CASHIER','出纳',2,'SELF',1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `sort_order`, `data_scope`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (4,'MANAGER','业务经理',3,'DEPT',1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `sort_order`, `data_scope`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (5,'EMPLOYEE','普通员工',4,'SELF',1,NULL,'2026-06-25 16:07:33','2026-06-25 16:07:33',0);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `sort_order`, `data_scope`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (6,'R2590','R2590',0,'ALL',1,NULL,'2026-06-26 16:49:42','2026-06-26 16:49:42',0);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `sort_order`, `data_scope`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (7,'R2623','R2623',0,'ALL',1,NULL,'2026-06-26 16:50:15','2026-06-26 16:50:15',0);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,2);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,3);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,4);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,5);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,6);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,7);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,8);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,9);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,10);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,11);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,12);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,13);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,14);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,15);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,101);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,102);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,103);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,104);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,105);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,106);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,107);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,108);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,109);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,201);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,202);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,203);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,204);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,205);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,206);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,207);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,301);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,302);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,303);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,304);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,305);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,306);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,307);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,308);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,309);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,310);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,311);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,401);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,402);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,403);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,404);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,501);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,502);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,503);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,504);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,505);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,601);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,602);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,603);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,604);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,605);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,701);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,702);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,703);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,704);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,705);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,801);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,802);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,803);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,901);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,902);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,903);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1001);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1002);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1003);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1101);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1102);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1103);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1201);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1202);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1301);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1302);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1303);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1401);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1402);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1403);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1501);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1502);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1503);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1504);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1505);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1506);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1507);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1508);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1509);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1510);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1511);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1512);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1513);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1514);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1515);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1516);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1517);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1518);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1519);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1520);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1521);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1522);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1523);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1524);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1525);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1526);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1527);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1528);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1529);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1530);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1531);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1532);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1533);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1534);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1535);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1536);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1537);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1538);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1539);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1540);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1541);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1542);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1543);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1544);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1545);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1546);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1547);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1548);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1549);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1550);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1551);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1552);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1553);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1554);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1555);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1556);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1557);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1558);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1559);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1560);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1561);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1562);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1563);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1564);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1565);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1566);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1567);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1568);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1569);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1570);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1571);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1572);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1573);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1574);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1575);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1576);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1577);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1578);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1579);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1580);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1581);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1582);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1583);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1584);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1585);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1586);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1587);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1588);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1589);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1590);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1591);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1592);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1593);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1594);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1595);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1596);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1597);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1598);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1599);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1600);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1601);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1602);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1603);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1604);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1605);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1606);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1607);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1608);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1609);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1610);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1611);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1612);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1613);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1614);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1615);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1616);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1617);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1618);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1619);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1620);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1621);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1622);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1623);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1624);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1625);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1626);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1627);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1628);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1629);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1630);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1631);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1632);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1633);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1634);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1635);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1636);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1637);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1638);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1639);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1640);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1641);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1642);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1643);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1644);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1645);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1646);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1647);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1648);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1649);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1650);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1651);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1652);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1653);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1654);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (1,'admin','$2a$10$tkw6m3zkMrcz1Tc3ws4A5.kZNZ/EnnETFC.vFwaZAuyBJXsIfy.c6','系统管理员_改',NULL,NULL,NULL,1,1,'总经办','ADMIN',1,'2026-06-27 00:53:00','127.0.0.1',NULL,'2026-06-25 16:07:33','2026-06-27 00:53:00',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (2,'test_op_ok','$2a$10$E/2fnStPrfHGs/13pAH2LusY3.Lwm0jDI24L46vesmsDESfuLNUkC','测试操作',NULL,NULL,NULL,1,1,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 13:33:36','2026-06-26 13:33:36',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (3,'verify_admin','$2a$10$RDV8SzMoYKy42Kte5NpVx.NpBbovKpRM/bghuK.KhDAepRTvx7RKq','验证',NULL,NULL,NULL,1,1,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 13:36:30','2026-06-26 13:36:30',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (4,'crud_test','$2a$10$lcr9tvodFZ8FhcH6bIRopefvP26CsWV2bn77y/rEtB3/t5K412iYa','CRUD测试',NULL,NULL,NULL,1,1,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 13:36:45','2026-06-26 13:36:45',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (5,'crud_test_v2','$2a$10$uK2TaCBzY..23GvWQRB6O.w3zOpEwB3yN3H2xJAMTPYj0JjSd2Hbq','CRUD全流程_已改',NULL,NULL,NULL,1,1,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 13:36:59','2026-06-26 13:36:59',1);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (6,'test_op_final','$2a$10$PoZvGJD2Jy/Sjus0wL7bzeWdXFgj/jrdjUSoIPlAX1XDyW/5QR46e','最终测试',NULL,NULL,NULL,1,1,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 13:41:13','2026-06-26 13:41:13',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (7,'test001','$2a$10$RAXntvPVEjXKayLwIJHo8uDa6mwJ70mzDhivq/v9O6YI6JtYCRY2e','测试用户',NULL,'13800138001','test@test.com',1,1,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 13:56:57','2026-06-26 13:56:57',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (8,'test_api','$2a$10$o8HVXvgWOfWHN65m4IiW8O2nutmLrN66bQ4XgOXeSn2d4GnF3xTpa','API测试',NULL,'13900000001','api@test.com',1,1,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 13:57:54','2026-06-26 13:57:54',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (9,'test_api2','$2a$10$R34L36e0I9lD/mkPCTemPuGgIEXcMFb/OhFofJrJ2t2EqZQv4qu0W','测试',NULL,'13999999991','t1@t.com',1,1,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 14:00:25','2026-06-26 14:00:25',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (10,'test_fix','$2a$10$Uod18Rz4y/uOkcNTC1Ige.0UeS9TZ2C79bGzgcpr2SvsZ411BZFci','修复测试',NULL,'13800000001','fx@t.com',1,1,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 14:14:06','2026-06-26 14:14:06',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (11,'t92031','$2a$10$UbHib320MPjZkJjWpYvJcuCQv3iSb0RW4SZutv/EY3CgDd90LRJt6','测试',NULL,'13900000001',NULL,1,NULL,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 16:40:23','2026-06-26 16:40:23',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (12,'testtest','$2a$10$EYH3VMBziPFXMF.PAyfvtuE3HYbTwvmliOYbQx7SqiPC4ENqaWZPO','test',NULL,'13900000001',NULL,1,NULL,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 16:40:36','2026-06-26 16:40:36',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (13,'u2590','$2a$10$iidwRNhIIoPS228u//Sp4.wSyuckCwOVbe8M4GAet3cYvaK143XlW','U',NULL,'13900000001',NULL,1,NULL,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 16:49:41','2026-06-26 16:49:41',0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `phone`, `email`, `gender`, `dept_id`, `dept_name`, `user_type`, `status`, `last_login`, `last_ip`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (14,'u2623','$2a$10$awp6QHSByX7mABtgZtPQV.p7zfUx7kV94cjIf4cQTsQAtQOXaqJaK','U',NULL,'13900000001',NULL,1,NULL,NULL,'EMPLOYEE',1,NULL,NULL,NULL,'2026-06-26 16:50:14','2026-06-26 16:50:14',0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1,1);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `wf_approval_config`
--

LOCK TABLES `wf_approval_config` WRITE;
/*!40000 ALTER TABLE `wf_approval_config` DISABLE KEYS */;
INSERT INTO `wf_approval_config` (`id`, `doc_type`, `doc_type_name`, `approver_ids`, `approve_mode`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (13,'VOUCHER','凭证','1','SEQUENCE',1,NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `wf_approval_config` (`id`, `doc_type`, `doc_type_name`, `approver_ids`, `approve_mode`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (14,'EXPENSE','费用报销','1','SEQUENCE',1,NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `wf_approval_config` (`id`, `doc_type`, `doc_type_name`, `approver_ids`, `approve_mode`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (15,'PAYMENT','付款单','1','SEQUENCE',1,NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `wf_approval_config` (`id`, `doc_type`, `doc_type_name`, `approver_ids`, `approve_mode`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (16,'RECEIPT','收款单','1','SEQUENCE',1,NULL,'2026-06-25 16:07:34','2026-06-25 16:07:34',0);
INSERT INTO `wf_approval_config` (`id`, `doc_type`, `doc_type_name`, `approver_ids`, `approve_mode`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES (17,'contract','contract审批',NULL,'SEQUENCE',1,NULL,'2026-06-26 10:38:17','2026-06-26 02:38:08',0);
/*!40000 ALTER TABLE `wf_approval_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `wf_approval_record`
--

LOCK TABLES `wf_approval_record` WRITE;
/*!40000 ALTER TABLE `wf_approval_record` DISABLE KEYS */;
INSERT INTO `wf_approval_record` (`id`, `doc_type`, `doc_id`, `doc_no`, `step_no`, `submitter_id`, `submitter_name`, `approver_id`, `approver_name`, `action`, `opinion`, `status`, `submit_time`, `approve_time`) VALUES (1,'contract',2,'CT-1782441496756',1,1,'管理员',2,'审批员1',NULL,NULL,'0',NULL,NULL);
/*!40000 ALTER TABLE `wf_approval_record` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-27  1:02:39
