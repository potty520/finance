package com.finance.module.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("rpt_custom")
public class FinReport implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String reportCode;
    private String reportName;
    private String reportType;
    private Integer fiscalYear;
    private Integer fiscalPeriod;
    @TableField("config_json")
    private String content;
    private String status;
    @TableField("creator_id")
    private Long creator;
    private LocalDateTime createTime;
    @JsonIgnore
    private Integer deleted;

    @TableField(exist = false)
    private String creatorName;
    @TableField(exist = false)
    private LocalDateTime updateTime;
    @TableField(exist = false)
    private String templateId;
}
