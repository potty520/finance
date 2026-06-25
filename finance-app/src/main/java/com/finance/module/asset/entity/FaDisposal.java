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
@TableName("fa_disposal")
public class FaDisposal implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;

    @TableField("disposal_no")
    private String billNo;

    @TableField("asset_id")
    private Long cardId;

    @TableField(exist = false)
    private String cardCode;

    @TableField(exist = false)
    private String cardName;

    @TableField("disposal_date")
    private LocalDate disposalDate;

    @TableField("disposal_type")
    private String disposalType;

    @TableField(exist = false)
    private BigDecimal originalValue;

    @TableField(exist = false)
    private BigDecimal accumulatedDepreciation;

    @TableField(exist = false)
    private BigDecimal residualValue;

    @TableField("disposal_amount")
    private BigDecimal disposalAmount;

    @TableField(exist = false)
    private BigDecimal taxAmount;

    @TableField("gain_loss")
    private BigDecimal profitLoss;

    private String reason;

    private String status;

    @TableField(exist = false)
    private Long operator;

    @TableField(exist = false)
    private String operatorName;

    private LocalDateTime createTime;

    private Integer deleted;
}
