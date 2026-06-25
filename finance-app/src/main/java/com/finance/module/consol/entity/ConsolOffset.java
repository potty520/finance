package com.finance.module.consol.entity;

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
@TableName("con_offset")
public class ConsolOffset implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer fiscalYear;
    private Integer fiscalPeriod;
    private String offsetType;
    private String subjectCode;
    private String subjectName;
    private String summary;
    private BigDecimal amount;
    private Long sourceCompanyId;
    private String sourceCompany;
    private Long targetCompanyId;
    private String targetCompany;
    private String status;
    @JsonIgnore
    private Integer deleted;

    @TableField(exist = false)
    private String remark;
    @TableField(exist = false)
    private Long operator;
    @TableField(exist = false)
    private String operatorName;
    @TableField(exist = false)
    private LocalDateTime createTime;
}
