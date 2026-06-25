package com.finance.module.ledger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("gl_auxiliary_item")
public class GlAuxiliary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("type_code")
    private String typeCode;

    @TableField("item_code")
    private String itemCode;

    @TableField("item_name")
    private String itemName;

    private Long parentId;

    private Integer status;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @JsonIgnore
    private Integer deleted;

    /** 兼容旧 API 参数名 */
    @TableField(exist = false)
    private String auxType;

    @TableField(exist = false)
    private String auxCode;

    @TableField(exist = false)
    private String auxName;
}
