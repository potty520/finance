package com.finance.module.cashier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.cashier.entity.CashDaily;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CashDailyMapper extends BaseMapper<CashDaily> {
    List<CashDaily> selectByAccount(@Param("accountId") Long accountId,
                                    @Param("startDate") String startDate,
                                    @Param("endDate") String endDate);
    BigDecimal sumAmount(@Param("accountId") Long accountId,
                         @Param("billType") String billType,
                         @Param("startDate") String startDate,
                         @Param("endDate") String endDate);
}
