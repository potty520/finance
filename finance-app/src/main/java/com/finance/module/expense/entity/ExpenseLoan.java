package com.finance.module.expense.entity;

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
@TableName("exp_loan")
public class ExpenseLoan implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String loanNo;
    private Long applicantId;
    private LocalDate loanDate;
    private BigDecimal amount;
    private BigDecimal repaidAmount;
    private String purpose;
    private String status;
    private Long voucherId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;

    @TableField(exist = false)
    private String billNo;

    @TableField(exist = false)
    private Long applicant;

    @TableField(exist = false)
    private String applicantName;

    @TableField(exist = false)
    private Long deptId;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private BigDecimal pendingAmount;

    @TableField(exist = false)
    private String reason;

    @TableField(exist = false)
    private String remark;
}
