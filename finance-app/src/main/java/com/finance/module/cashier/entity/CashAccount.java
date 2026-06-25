package com.finance.module.cashier.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("cash_account")
public class CashAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String accountCode;
    private String accountName;
    private String accountType;
    private String bankName;
    private String bankAccount;
    private String currencyCode;
    private String subjectCode;
    private BigDecimal openingBalance;
    private BigDecimal currentBalance;
    private Integer status;
    private Integer isDefault;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @JsonIgnore private Integer deleted;

    /** 兼容旧字段名 */
    @TableField(exist = false)
    private BigDecimal beginBalance;
}
