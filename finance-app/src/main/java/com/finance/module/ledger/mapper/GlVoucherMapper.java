package com.finance.module.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.ledger.entity.GlVoucher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GlVoucherMapper extends BaseMapper<GlVoucher> {
    List<GlVoucher> selectListWithEntries(@Param("ew") com.baomidou.mybatisplus.core.conditions.Wrapper<GlVoucher> wrapper);
}
