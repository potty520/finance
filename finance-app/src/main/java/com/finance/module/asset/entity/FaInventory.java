package com.finance.module.asset.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("fa_inventory")
public class FaInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String billNo;
    private LocalDate inventoryDate;
    private String inventoryType;
    private String status;
    private Long operator;
    private String operatorName;
    private String remark;
    private LocalDateTime createTime;
    private Integer deleted;
}
