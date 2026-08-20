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

    private Long instanceId;

    private String flowCode;

    private String businessType;

    private String businessNo;

    private Long businessId;

    private Integer level;

    private String nodeName;

    private Long assignee;

    private String assigneeName;

    private LocalDateTime createTime;

    private LocalDateTime handleTime;
}
