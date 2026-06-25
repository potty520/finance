package com.finance.module.payable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("ap_invoice")
public class ApInvoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String billNo;
    /** 1-采购发票 2-费用发票 3-其他应付 */
    private String invoiceType;
    private LocalDate invoiceDate;
    private Long supplierId;
    private String supplierName;
    private String taxNo;
    private String taxRate;
    private BigDecimal amount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal unpaidAmount;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private String subjectCode;
    private LocalDate dueDate;
    private String contractNo;
    private String purchaseOrderNo;
    private String remark;
    private String status;
    private Long creator;
    private String creatorName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
