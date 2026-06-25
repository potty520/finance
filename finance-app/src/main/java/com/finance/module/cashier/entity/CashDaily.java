package com.finance.module.cashier.entity;

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
@TableName("cash_journal")
public class CashDaily implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long accountId;
    private String accountCode;
    private LocalDate transDate;
    /** IN / OUT / TRANSFER */
    private String transType;
    private BigDecimal amount;
    private String subjectCode;
    private String summary;
    private Long voucherId;
    private String source;
    private Long sourceId;
    private Long customerId;
    private Long supplierId;
    private Long deptId;
    private Long employeeId;
    private String status;
    private Long createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @JsonIgnore
    private Integer deleted;

    /** 前端兼容字段（非 DB 列） */
    @TableField(exist = false)
    private String billNo;
    @TableField(exist = false)
    private String accountName;
    @TableField(exist = false)
    private String ioType;
    @TableField(exist = false)
    private String oppositeSubject;
    @TableField(exist = false)
    private BigDecimal balance;
}
