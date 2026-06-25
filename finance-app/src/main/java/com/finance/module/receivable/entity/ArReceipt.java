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
@TableName("ar_receipt")
public class ArReceipt implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String billNo;
    private LocalDate receiptDate;
    private Long customerId;
    private String customerName;
    /** 收款方式：1-现金 2-银行 3-票据 4-其他 */
    private String receiptType;
    private Long accountId;
    private String accountName;
    private BigDecimal amount;
    private String currencyCode;
    private BigDecimal exchangeRate;
    /** 已核销金额 */
    private BigDecimal appliedAmount;
    private BigDecimal unappliedAmount;
    private String subjectCode;
    private String voucherNo;
    private Long voucherId;
    private String remark;
    /** 状态：D-草稿 A-已审核 */
    private String status;
    private Long creator;
    private String creatorName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
