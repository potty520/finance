package com.finance.module.receivable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ar_customer")
public class ArCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String customerCode;
    private String customerName;
    private String customerType;
    private String taxNo;
    private String bankName;
    private String bankAccount;
    private String contact;
    private String phone;
    private String address;
    private BigDecimal creditLimit;
    private Integer creditDays;
    private String subjectCode;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @JsonIgnore private Integer deleted;
}
