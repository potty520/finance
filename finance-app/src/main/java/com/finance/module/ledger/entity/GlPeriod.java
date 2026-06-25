package com.finance.module.ledger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("gl_period")
public class GlPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String periodCode;

    private String periodName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer fiscalYear;

    @TableField("period_index")
    private Integer periodIndex;

    /** OPEN / CLOSED */
    private String status;

    private Integer isAdjust;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @JsonIgnore
    private Integer deleted;

    /** 兼容旧 API */
    @TableField(exist = false)
    private Integer fiscalPeriod;

    @TableField(exist = false)
    private Long closer;

    @TableField(exist = false)
    private String closerName;

    @TableField(exist = false)
    private LocalDateTime closeTime;

    @TableField(exist = false)
    private String remark;
}
