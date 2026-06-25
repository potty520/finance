package com.finance.module.payable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("ap_payment")
public class ApPayment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("payment_no")
    private String billNo;

    private Long supplierId;

    private LocalDate paymentDate;

    private BigDecimal amount;

    @TableField("settled_amount")
    private BigDecimal appliedAmount;

    private Long accountId;

    @TableField("payment_type")
    private String paymentType;

    private Long billId;

    private Long voucherId;

    private String status;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @JsonIgnore
    private Integer deleted;

    @TableField(exist = false)
    private String supplierName;

    @TableField(exist = false)
    private String accountName;

    @TableField(exist = false)
    private BigDecimal unappliedAmount;

    @TableField(exist = false)
    private String paymentMethod;
}
