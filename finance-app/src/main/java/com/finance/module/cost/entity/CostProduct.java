package com.finance.module.cost.entity;

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
@TableName("cost_calculation")
public class CostProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String periodCode;

    private Long productItemId;

    private BigDecimal directMaterial;

    private BigDecimal directLabor;

    @TableField("manufacture_overhead")
    private BigDecimal manufactureOverhead;

    private BigDecimal totalCost;

    @TableField("output_qty")
    private BigDecimal outputQuantity;

    private BigDecimal unitCost;

    private String status;

    private LocalDateTime createTime;

    @JsonIgnore
    private Integer deleted;

    @TableField(exist = false)
    private String fiscalYear;

    @TableField(exist = false)
    private Integer fiscalPeriod;

    @TableField(exist = false)
    private String productCode;

    @TableField(exist = false)
    private String productName;

    @TableField(exist = false)
    private BigDecimal directExpense;

    @TableField(exist = false)
    private BigDecimal indirectMaterial;

    @TableField(exist = false)
    private BigDecimal indirectLabor;

    @TableField(exist = false)
    private BigDecimal indirectExpense;
}
