package com.finance.module.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.ledger.entity.GlAuxiliary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GlAuxiliaryMapper extends BaseMapper<GlAuxiliary> {
    List<GlAuxiliary> selectByType(@Param("auxType") String auxType);
}
