package com.finance.module.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("prj_ledger")
public class ProjectLedger implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private String projectName;
    private String businessType;
    private Long businessId;
    private String businessNo;
    private String summary;
    private BigDecimal amount;
    private LocalDate handleDate;
}
