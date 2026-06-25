package com.finance.module.budget.entity;

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
@TableName("budget_master")
public class Budget implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer fiscalYear;

    @TableField("period_index")
    private Integer fiscalPeriod;

    private String subjectCode;

    private Long deptId;

    private Long projectId;

    @TableField("budget_amount")
    private BigDecimal amount;

    private BigDecimal usedAmount;

    @TableField("control_type")
    private String controlType;

    /** 0-未提交 1-已审核 2-已下达 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @JsonIgnore
    private Integer deleted;

    @TableField(exist = false)
    private String budgetNo;

    @TableField(exist = false)
    private String budgetType;

    @TableField(exist = false)
    private String subjectName;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String projectName;

    @TableField(exist = false)
    private BigDecimal availableAmount;

    @TableField(exist = false)
    private String remark;

    @TableField(exist = false)
    private Long creator;

    @TableField(exist = false)
    private String creatorName;
}
