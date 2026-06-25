package com.finance.module.ledger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 凭证分录（库表：line_no + dc_direction + amount）
 */
@Data
@TableName("gl_voucher_entry")
public class GlVoucherEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long voucherId;

    @TableField("line_no")
    private Integer entryNo;

    @TableField("aux_summary")
    private String summary;

    private String subjectCode;

    private String subjectName;

    @TableField(exist = false)
    private Long auxCustomerId;

    @TableField(exist = false)
    private String auxCustomerName;

    @TableField(exist = false)
    private Long auxSupplierId;

    @TableField(exist = false)
    private String auxSupplierName;

    @TableField(exist = false)
    private Long auxDeptId;

    @TableField(exist = false)
    private String auxDeptName;

    @TableField(exist = false)
    private Long auxProjectId;

    @TableField(exist = false)
    private String auxProjectName;

    @TableField("aux_employee_id")
    private Long auxPersonId;

    @TableField(exist = false)
    private String auxPersonName;

    @TableField("aux_product_id")
    private Long auxGoodsId;

    @TableField(exist = false)
    private String auxGoodsName;

    @TableField(exist = false)
    private String auxCustom1;

    @TableField(exist = false)
    private String auxCustom2;

    private String currencyCode;

    @TableField("orig_amount")
    private BigDecimal originalAmount;

    private BigDecimal exchangeRate;

    /** DEBIT / CREDIT */
    private String dcDirection;

    private BigDecimal amount;

    @TableField(exist = false)
    private BigDecimal debitAmount;

    @TableField(exist = false)
    private BigDecimal creditAmount;

    private BigDecimal quantity;

    private BigDecimal price;

    @TableField(exist = false)
    private String priceUnit;

    @TableField(exist = false)
    private String billNo;

    private Long cashFlowId;

    public void prepareForPersist() {
        BigDecimal debit = debitAmount == null ? BigDecimal.ZERO : debitAmount;
        BigDecimal credit = creditAmount == null ? BigDecimal.ZERO : creditAmount;
        if (debit.compareTo(BigDecimal.ZERO) > 0) {
            dcDirection = "DEBIT";
            amount = debit;
        } else {
            dcDirection = "CREDIT";
            amount = credit;
        }
    }

    public void afterLoad() {
        if (amount == null) {
            debitAmount = BigDecimal.ZERO;
            creditAmount = BigDecimal.ZERO;
            return;
        }
        if ("DEBIT".equalsIgnoreCase(dcDirection)) {
            debitAmount = amount;
            creditAmount = BigDecimal.ZERO;
        } else {
            creditAmount = amount;
            debitAmount = BigDecimal.ZERO;
        }
    }
}
