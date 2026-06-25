package com.finance.module.project.entity;

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
@TableName("prj_project")
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String projectCode;
    private String projectName;
    private String projectType;
    private Long managerId;
    private Long deptId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budgetAmount;
    private BigDecimal actualAmount;
    private String status;
    private BigDecimal revenue;
    private BigDecimal cost;
    private BigDecimal profit;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @JsonIgnore
    private Integer deleted;

    @TableField(exist = false)
    private Long contractId;

    @TableField(exist = false)
    private String contractNo;

    @TableField(exist = false)
    private String managerName;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private BigDecimal usedAmount;

    @TableField(exist = false)
    private String content;
}
