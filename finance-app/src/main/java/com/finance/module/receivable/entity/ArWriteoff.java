package com.finance.module.receivable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ar_writeoff")
public class ArWriteoff implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private Long receiptId;
    private String receiptNo;
    private Long invoiceId;
    private String invoiceNo;
    private BigDecimal writeoffAmount;
    private String remark;
    private Long operator;
    private String operatorName;
    private LocalDateTime createTime;
}
