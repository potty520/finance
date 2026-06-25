package com.finance.module.payable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.payable.entity.ApWriteoff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApWriteoffMapper extends BaseMapper<ApWriteoff> {
    List<ApWriteoff> selectByPayment(@Param("paymentId") Long paymentId);
    List<ApWriteoff> selectByInvoice(@Param("invoiceId") Long invoiceId);
}
