package com.finance.module.cost.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("cost_center")
public class CostCenter implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String centerCode;
    private String centerName;
    @TableField(exist = false)
    private Long parentId;
    private String centerType;
    @TableField(exist = false)
    private Long managerId;
    @TableField(exist = false)
    private String managerName;
    private Long deptId;
    @TableField(exist = false)
    private String deptName;
    private String subjectCode;
    private Integer status;
    @TableField(exist = false)
    private String remark;
    @TableField(exist = false)
    private LocalDateTime createTime;
    @TableField(exist = false)
    private LocalDateTime updateTime;
    private Integer deleted;
}
