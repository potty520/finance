package com.finance.module.asset.entity;

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
@TableName("fa_depreciation")
public class FaDepreciation implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;

    @TableField("asset_id")
    private Long cardId;

    @TableField(exist = false)
    private String cardCode;

    @TableField(exist = false)
    private String cardName;

    @TableField(exist = false)
    private String fiscalYear;

    @TableField(exist = false)
    private Integer fiscalPeriod;

    @TableField("period_code")
    private String periodCode;

    @TableField("dep_date")
    private LocalDate depreciationDate;

    @TableField(exist = false)
    private BigDecimal originalValue;

    @TableField("dep_amount")
    private BigDecimal periodDepreciation;

    @TableField("accumulated")
    private BigDecimal accumulatedDepreciation;

    private BigDecimal netValue;

    @TableField(exist = false)
    private String status;

    @TableField(exist = false)
    private Long operator;

    @TableField(exist = false)
    private String operatorName;

    private LocalDateTime createTime;

    private Integer deleted;
}
