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
@TableName("fa_asset")
public class FaCard implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;

    @TableField("asset_code")
    private String cardCode;

    @TableField("asset_name")
    private String cardName;

    @TableField("category_id")
    private Long categoryId;

    @TableField(exist = false)
    private String categoryCode;

    @TableField(exist = false)
    private String categoryName;

    private String spec;

    @TableField(exist = false)
    private String model;

    private String unit;

    @TableField(exist = false)
    private Integer quantity;

    @TableField("purchase_date")
    private LocalDate purchaseDate;

    @TableField(exist = false)
    private String purchaseNo;

    @TableField(exist = false)
    private String supplierId;

    @TableField(exist = false)
    private String supplierName;

    private BigDecimal originalValue;

    @TableField(exist = false)
    private BigDecimal taxAmount;

    private BigDecimal netValue;

    @TableField(exist = false)
    private BigDecimal residualRate;

    @TableField("salvage_value")
    private BigDecimal residualValue;

    @TableField("useful_life")
    private Integer useLifeMonth;

    @TableField(exist = false)
    private Integer usedMonth;

    @TableField(exist = false)
    private Integer remainMonth;

    @TableField("depreciation_method")
    private String depreciationMethod;

    private Long deptId;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private Long userId;

    @TableField(exist = false)
    private String userName;

    private String location;

    private String status;

    @TableField(exist = false)
    private String subjectCode;

    @TableField(exist = false)
    private String depSubjectCode;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
