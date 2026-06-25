package com.finance.module.ledger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("gl_cash_flow_item")
public class GlCashFlowItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String itemCode;

    private String itemName;

    private Long parentId;

    private String direction;

    private String flowType;

    private Integer sortOrder;

    private Integer status;

    @JsonIgnore
    private Integer deleted;
}
