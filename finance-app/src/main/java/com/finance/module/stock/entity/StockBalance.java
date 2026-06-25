package com.finance.module.stock.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("inv_balance")
public class StockBalance implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("item_id")
    private Long goodsId;

    @TableField("wh_id")
    private Long warehouseId;

    private String periodCode;

    @TableField("opening_qty")
    private BigDecimal beginQuantity;

    @TableField("opening_amt")
    private BigDecimal beginAmount;

    @TableField("period_in_qty")
    private BigDecimal inQuantity;

    @TableField("period_in_amt")
    private BigDecimal inAmount;

    @TableField("period_out_qty")
    private BigDecimal outQuantity;

    @TableField("period_out_amt")
    private BigDecimal outAmount;

    @TableField("ending_qty")
    private BigDecimal endQuantity;

    @TableField("ending_amt")
    private BigDecimal endAmount;

    private BigDecimal avgPrice;

    @TableField(exist = false)
    private String goodsCode;

    @TableField(exist = false)
    private String goodsName;

    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private String spec;

    @TableField(exist = false)
    private String unit;

    @TableField(exist = false)
    private String warehouseName;

    @TableField(exist = false)
    private String batchNo;

    @TableField(exist = false)
    private Integer deleted;
}
