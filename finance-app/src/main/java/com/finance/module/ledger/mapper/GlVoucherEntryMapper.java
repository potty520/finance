package com.finance.module.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.ledger.entity.GlVoucherEntry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GlVoucherEntryMapper extends BaseMapper<GlVoucherEntry> {
    List<GlVoucherEntry> selectByVoucherId(@Param("voucherId") Long voucherId);
    int deleteByVoucherId(@Param("voucherId") Long voucherId);
}
