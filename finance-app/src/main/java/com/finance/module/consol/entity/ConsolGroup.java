package com.finance.module.consol.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("con_group")
public class ConsolGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String groupCode;
    private String groupName;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;

    @TableField(exist = false)
    private Long parentId;

    @TableField(exist = false)
    private String groupType;

    @TableField(exist = false)
    private String currencyCode;

    @TableField(exist = false)
    private Integer ownershipRatio;
}
