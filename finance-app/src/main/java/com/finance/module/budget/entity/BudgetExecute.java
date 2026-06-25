package com.finance.module.budget.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("budget_adjustment")
public class BudgetExecute implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private Long budgetId;
    private String sourceModule;
    private String sourceBillNo;
    private String sourceBillId;
    private BigDecimal amount;
    private String fiscalYear;
    private Integer fiscalPeriod;
    private String remark;
    private Long operator;
    private String operatorName;
    private LocalDateTime createTime;
}
