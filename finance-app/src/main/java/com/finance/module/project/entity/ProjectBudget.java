package com.finance.module.project.entity;

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
@TableName("prj_budget")
public class ProjectBudget implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private String budgetType;
    private String subjectCode;
    private BigDecimal amount;
    private String remark;
    private LocalDateTime createTime;
    @JsonIgnore
    private Integer deleted;

    @TableField(exist = false)
    private String projectName;
    @TableField(exist = false)
    private String subjectName;
}
