package com.finance.module.receivable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.receivable.entity.ArReceipt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ArReceiptMapper extends BaseMapper<ArReceipt> {
    BigDecimal sumUnapplied(@Param("customerId") Long customerId);
    List<ArReceipt> selectByCustomer(@Param("customerId") Long customerId);
}
