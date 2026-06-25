package com.finance.module.stock.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("inv_warehouse")
public class StockWarehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    @TableField(exist = false)
    private String warehouseCode;
    @TableField(exist = false)
    private String warehouseName;
    @TableField(exist = false)
    private String warehouseType;
    private String location;
    private String manager;
    @TableField(exist = false)
    private String phone;
    private Integer status;
    @TableField(exist = false)
    private String remark;
    @TableField(exist = false)
    private LocalDateTime createTime;
    private Integer deleted;
}
