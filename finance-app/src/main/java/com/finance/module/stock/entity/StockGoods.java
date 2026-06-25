package com.finance.module.stock.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("inv_item")
public class StockGoods implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;

    @TableField("item_code")
    private String itemCode;

    @TableField("item_name")
    private String itemName;

    private Long categoryId;

    private String spec;

    private String unit;

    private String barcode;

    private String subjectCode;

    private String costMethod;

    private BigDecimal safetyStock;

    private Integer isBatchManage;

    private Integer isSerialManage;

    private Integer status;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;

    /** 兼容旧 API 字段名 */
    @TableField(exist = false)
    private String goodsCode;

    @TableField(exist = false)
    private String goodsName;

    @TableField(exist = false)
    private String categoryCode;

    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private String model;

    @TableField(exist = false)
    private String priceUnit;

    @TableField(exist = false)
    private BigDecimal lastPrice;

    @TableField(exist = false)
    private BigDecimal avgPrice;

    @TableField(exist = false)
    private BigDecimal referenceCost;

    @TableField(exist = false)
    private String pricingMethod;

    @TableField(exist = false)
    private String salesSubjectCode;

    @TableField(exist = false)
    private String cogsSubjectCode;

    @TableField(exist = false)
    private BigDecimal maxStock;

    @TableField(exist = false)
    private BigDecimal minStock;
}
