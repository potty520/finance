package com.finance.module.cashier.entity;

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
@TableName("cash_bill")
public class CashBill implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;

    @TableField("bill_no")
    private String billNo;
    /** 类型：1-汇票 2-本票 3-支票 4-其他 */
    @TableField("bill_type")
    private String billType;
    /** 方向：IN/OUT */
    @TableField("direction")
    private String direction;
    private String drawer;
    @TableField("payee")
    private String payee;
    @TableField("issue_date")
    private LocalDate issueDate;
    @TableField("due_date")
    private LocalDate dueDate;
    private BigDecimal amount;
    /** 状态：HOLD/REGISTERED/USED/CANCELED */
    private String status;
    @TableField("bank_name")
    private String bankName;
    @TableField("customer_id")
    private Long customerId;
    @TableField("supplier_id")
    private Long supplierId;
    private String remark;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
    private Integer deleted;
}