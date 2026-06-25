package com.finance.module.payable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ap_writeoff")
public class ApWriteoff implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private Long paymentId;
    private String paymentNo;
    private Long invoiceId;
    private String invoiceNo;
    private BigDecimal writeoffAmount;
    private String remark;
    private Long operator;
    private String operatorName;
    private LocalDateTime createTime;
}
