package com.finance.module.ledger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会计科目
 */
@Data
@TableName("gl_account_subject")
public class GlSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String subjectCode;

    private String subjectName;

    private Long parentId;

    private String parentCode;

    @TableField("category_code")
    private String category;

    /** 科目类别：ASSET/LIAB/EQUITY/COST/INCOME/EXPENSE/COMMON */

    /** 余额方向：1-借方 2-贷方 */
    private String balanceDirection;

    @TableField(exist = false)
    private String auxType;

    private String currencyCode;

    @TableField(exist = false)
    private Integer isQuantities;

    @TableField(exist = false)
    private Integer isAux;

    @TableField(exist = false)
    private Integer isCash;

    @TableField(exist = false)
    private Integer isBank;

    @TableField(exist = false)
    private Integer isTotal;

    private Integer level;

    private Integer isLeaf;

    @TableField(exist = false)
    private String fullCode;

    private Integer status;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @JsonIgnore
    private Integer deleted;

    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private List<GlSubject> children;
}
