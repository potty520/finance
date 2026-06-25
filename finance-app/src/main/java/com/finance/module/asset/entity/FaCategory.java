package com.finance.module.asset.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("fa_category")
public class FaCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String categoryCode;
    private String categoryName;
    private String assetType;
    private String depreciationMethod;
    private Integer usefulLife;
    private BigDecimal salvageRate;
    private String subjectCode;
    private String depSubjectCode;
    private Integer status;
    private Integer deleted;

    @TableField(exist = false)
    private Long parentId;

    @TableField(exist = false)
    private Integer useLifeMonth;

    @TableField(exist = false)
    private BigDecimal residualRate;
}
