package com.finance.module.cashier.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("cash_reconciliation")
public class CashReconciliation implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String billNo;
    private Long accountId;
    private String accountName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal bookBalance;
    private BigDecimal bankBalance;
    private BigDecimal diffAmount;
    /** 状态：0-未对账 1-已对账 */
    private String status;
    private LocalDateTime reconTime;
    private Long operator;
    private String operatorName;
    private String remark;
    private LocalDateTime createTime;
    private Integer deleted;
}
