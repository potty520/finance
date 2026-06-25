package com.finance.module.cashier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.cashier.entity.CashAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CashAccountMapper extends BaseMapper<CashAccount> {
    List<CashAccount> selectActive();
}
