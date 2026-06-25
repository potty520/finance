package com.finance.module.stock.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("inv_transaction")
public class StockIo implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO) private Long id;
    private String billNo;
    /** 1-采购入库 2-销售出库 3-领料出库 4-退料入库 5-调拨 6-盘点 7-其他 */
    private String ioType;
    private LocalDate ioDate;
    private Long goodsId;
    private String goodsCode;
    private String goodsName;
    private String spec;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private BigDecimal totalCost;
    private Long warehouseId;
    private String warehouseName;
    private String batchNo;
    private String businessNo;
    private String sourceModule;
    private Long deptId;
    private String deptName;
    private String remark;
    private String status;
    private Long creator;
    private String creatorName;
    private LocalDateTime createTime;
    private Integer deleted;
}
