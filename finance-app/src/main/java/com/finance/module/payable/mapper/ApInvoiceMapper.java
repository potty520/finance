package com.finance.module.payable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.payable.entity.ApInvoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface ApInvoiceMapper extends BaseMapper<ApInvoice> {
    BigDecimal sumUnpaid(@Param("supplierId") Long supplierId);
    List<Map<String, Object>> agingAnalysis(@Param("supplierId") Long supplierId);
}
