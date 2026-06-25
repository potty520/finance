package com.finance.module.ledger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("gl_balance")
public class GlBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String periodCode;

    private String subjectCode;

    private String currencyCode;

    private BigDecimal openingDebit;

    private BigDecimal openingCredit;

    private BigDecimal periodDebit;

    private BigDecimal periodCredit;

    private BigDecimal yearDebit;

    private BigDecimal yearCredit;

    private BigDecimal endingDebit;

    private BigDecimal endingCredit;

    private Long auxCustomerId;

    private Long auxSupplierId;

    private Long auxDeptId;

    private Long auxProjectId;

    private Long auxEmployeeId;

    private Long auxProductId;

    /** 兼容旧 API 查询参数 */
    @TableField(exist = false)
    private String fiscalYear;

    @TableField(exist = false)
    private Integer fiscalPeriod;

    @TableField(exist = false)
    private String subjectName;

    @TableField(exist = false)
    private BigDecimal beginBalance;

    @TableField(exist = false)
    private BigDecimal endBalance;
}
