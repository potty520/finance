package com.finance.module.receivable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.receivable.entity.ArInvoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface ArInvoiceMapper extends BaseMapper<ArInvoice> {
    BigDecimal sumUncollected(@Param("customerId") Long customerId);
    List<Map<String, Object>> agingAnalysis(@Param("customerId") Long customerId);
}
