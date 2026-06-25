package com.finance.module.cost.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("cost_element")
public class CostItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;

    @TableField("element_code")
    private String elementCode;

    @TableField("element_name")
    private String elementName;

    @TableField("element_type")
    private String elementType;

    private String subjectCode;
    private Integer status;
    private Integer deleted;

    @TableField(exist = false)
    private String itemCode;

    @TableField(exist = false)
    private String itemName;

    @TableField(exist = false)
    private String itemType;

    @TableField(exist = false)
    private String remark;
}
