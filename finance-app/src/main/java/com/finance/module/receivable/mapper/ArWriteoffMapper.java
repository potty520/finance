package com.finance.module.receivable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.receivable.entity.ArWriteoff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArWriteoffMapper extends BaseMapper<ArWriteoff> {
    List<ArWriteoff> selectByReceipt(@Param("receiptId") Long receiptId);
    List<ArWriteoff> selectByInvoice(@Param("invoiceId") Long invoiceId);
}
