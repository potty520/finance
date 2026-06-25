package com.finance.module.cashier.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("cash_bill")
public class CashBill implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String billNo;
    /** 类型：1-汇票 2-本票 3-支票 4-其他 */
    private String billType;
    private String drawer;
    private String drawee;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private BigDecimal amount;
    private String currencyCode;
    /** 状态：1-未使用 2-已使用 3-已背书 4-已贴现 5-已作废 */
    private String status;
    private String acceptBank;
    private String usePurpose;
    private Long customerId;
    private String customerName;
    private Long supplierId;
    private String supplierName;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
