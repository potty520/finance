package com.finance.module.cost.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("cost_allocation")
public class CostAllocation implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String billNo;
    private String fiscalYear;
    private Integer fiscalPeriod;
    private LocalDate allocationDate;
    private String allocationType;
    private String sourceCenterCode;
    private String sourceCenterName;
    private String targetCenterCode;
    private String targetCenterName;
    private String itemCode;
    private String itemName;
    private BigDecimal amount;
    private String sourceNo;
    private String remark;
    private String status;
    private Long operator;
    private String operatorName;
    private LocalDateTime createTime;
    private Integer deleted;
}
