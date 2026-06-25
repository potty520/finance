package com.finance.module.cashier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.cashier.entity.CashBill;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CashBillMapper extends BaseMapper<CashBill> {
}
