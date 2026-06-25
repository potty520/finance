package com.finance.module.payable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("ap_supplier")
public class ApSupplier implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String supplierCode;
    private String supplierName;
    private String supplierType;
    private String taxNo;
    private String bankName;
    private String bankAccount;
    private String contact;
    private String phone;
    private String address;
    private String subjectCode;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @JsonIgnore private Integer deleted;
}
