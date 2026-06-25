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

    @TableField(exist = false)
    private String flowCode;

    @TableField(exist = false)
    private String flowName;

    @TableField(exist = false)
    private String businessType;

    @TableField(exist = false)
    private String businessNo;

    @TableField(exist = false)
    private Long businessId;

    @TableField(exist = false)
    private String currentNode;

    @TableField(exist = false)
    private Integer currentLevel;

    @TableField(exist = false)
    private Long initiator;

    @TableField(exist = false)
    private String initiatorName;

    @TableField(exist = false)
    private LocalDateTime finishTime;
}
