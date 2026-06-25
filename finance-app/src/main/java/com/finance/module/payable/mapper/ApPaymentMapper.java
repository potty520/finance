package com.finance.module.payable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.payable.entity.ApPayment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ApPaymentMapper extends BaseMapper<ApPayment> {
    BigDecimal sumUnapplied(@Param("supplierId") Long supplierId);
    List<ApPayment> selectBySupplier(@Param("supplierId") Long supplierId);
}
