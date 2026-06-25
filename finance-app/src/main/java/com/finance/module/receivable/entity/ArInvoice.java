package com.finance.module.receivable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("ar_invoice")
public class ArInvoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String billNo;
    /** 类型：1-销售发票 2-其他应收 */
    private String invoiceType;
    private LocalDate invoiceDate;
    private Long customerId;
    private String customerName;
    private String taxNo;
    private String taxRate;
    private BigDecimal amount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal collectedAmount;
    private BigDecimal uncollectedAmount;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private String subjectCode;
    private LocalDate dueDate;
    private String contractNo;
    private Long salesOrderId;
    private String salesOrderNo;
    private String remark;
    /** 状态：D-草稿 A-已审核 C-已关闭 */
    private String status;
    private Long creator;
    private String creatorName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
