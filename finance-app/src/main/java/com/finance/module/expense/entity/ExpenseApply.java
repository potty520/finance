package com.finance.module.expense.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("exp_application")
public class ExpenseApply implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String billNo;
    private String applyType;
    private String subjectCode;
    private String subjectName;
    private BigDecimal amount;
    private LocalDate applyDate;
    private Long applicant;
    private String applicantName;
    private Long deptId;
    private String deptName;
    private String reason;
    private String attachment;
    /** 0-待审 1-通过 2-驳回 3-已付款 */
    private String status;
    private String flowNo;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
