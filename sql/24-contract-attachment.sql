-- 合同电子附件字段（MySQL 兼容写法，重复执行前请确认列是否已存在）
ALTER TABLE ctr_sales_contract
    ADD COLUMN attachment VARCHAR(1000) DEFAULT NULL COMMENT '电子合同附件JSON';

ALTER TABLE ctr_purchase_contract
    ADD COLUMN attachment VARCHAR(1000) DEFAULT NULL COMMENT '电子合同附件JSON';
