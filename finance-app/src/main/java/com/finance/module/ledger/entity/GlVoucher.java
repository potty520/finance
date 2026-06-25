package com.finance.module.ledger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 凭证主表
 */
@Data
@TableName("gl_voucher")
public class GlVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String voucherNo;

    private String voucherType;

    private String periodCode;

    private LocalDate voucherDate;

    private Integer attachCount;

    private BigDecimal totalDebit;

    private BigDecimal totalCredit;

    private String summary;

    private String source;

    private String sourceBiz;

    private Long sourceId;

    /** DRAFT / APPROVED / POSTED 等 */
    private String status;

    private Integer isCash;

    private Integer isAudit;

    private Long createBy;

    private String createByName;

    private Long auditBy;

    private String auditByName;

    private LocalDateTime auditTime;

    private Long postBy;

    private LocalDateTime postTime;

    private Long cancelBy;

    private LocalDateTime cancelTime;

    private String cancelReason;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @JsonIgnore
    private Integer deleted;

    @TableField(exist = false)
    private List<GlVoucherEntry> entries;

    /** 兼容旧 API 字段名 */
    @TableField(exist = false)
    private Integer voucherWord;

    @TableField(exist = false)
    private Integer voucherSeq;

    @TableField(exist = false)
    private String fiscalYear;

    @TableField(exist = false)
    private Integer fiscalPeriod;

    @TableField(exist = false)
    private String sourceModule;

    @TableField(exist = false)
    private String sourceBillId;

    @TableField(exist = false)
    private String attachmentIds;

    @TableField(exist = false)
    private Long creator;

    @TableField(exist = false)
    private String creatorName;

    @TableField(exist = false)
    private Long auditor;

    @TableField(exist = false)
    private String auditorName;

    @TableField(exist = false)
    private Long poster;

    @TableField(exist = false)
    private String posterName;
}
