package com.finance.module.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("wf_approval_record")
public class WfTask implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String docType;
    private Long docId;
    private String docNo;
    private Integer stepNo;
    private Long submitterId;
    private String submitterName;
    private Long approverId;
    private String approverName;
    private String action;
    private String opinion;
    private String status;
    private LocalDateTime submitTime;
    private LocalDateTime approveTime;

    @TableField(exist = false)
    private Long instanceId;

    @TableField(exist = false)
    private String flowCode;

    @TableField(exist = false)
    private String businessType;

    @TableField(exist = false)
    private String businessNo;

    @TableField(exist = false)
    private Long businessId;

    @TableField(exist = false)
    private Integer level;

    @TableField(exist = false)
    private String nodeName;

    @TableField(exist = false)
    private Long assignee;

    @TableField(exist = false)
    private String assigneeName;

    @TableField(exist = false)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private LocalDateTime handleTime;
}
