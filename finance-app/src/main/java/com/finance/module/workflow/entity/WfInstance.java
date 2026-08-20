package com.finance.module.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("wf_approval_config")
public class WfInstance implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String docType;
    private String docTypeName;
    private String approverIds;
    private String approveMode;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @JsonIgnore
    private Integer deleted;

    private String flowCode;

    private String flowName;

    private String businessType;

    private String businessNo;

    private Long businessId;

    private String currentNode;

    private Integer currentLevel;

    private Long initiator;

    private String initiatorName;

    private LocalDateTime finishTime;
}
