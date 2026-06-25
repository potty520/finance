package com.finance.module.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("ctr_sales_contract")
public class Contract implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String contractNo;
    private String contractName;
    @TableField(exist = false) private String contractType;
    @TableField("customer_id") private Long partyId;
    @TableField(exist = false) private String partyName;
    @TableField(exist = false) private String partyContact;
    @TableField(exist = false) private String partyPhone;
    @TableField("total_amount") private BigDecimal amount;
    private LocalDate signDate;
    private LocalDate startDate;
    private LocalDate endDate;
    @TableField(exist = false) private String payType;
    @TableField(exist = false) private String content;
    @TableField(exist = false) private String attachment;
    private Long ownerId;
    @TableField(exist = false) private String ownerName;
    @TableField(exist = false) private Long deptId;
    @TableField(exist = false) private String deptName;
    /** DRAFT/SIGNED/EXECUTING/COMPLETED/CANCEL */
    private String status;
    @TableField(exist = false) private String flowNo;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
