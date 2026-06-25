package com.finance.module.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.ledger.entity.GlSubject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GlSubjectMapper extends BaseMapper<GlSubject> {
    List<GlSubject> selectByCategory(@Param("category") String category);
    GlSubject selectByCode(@Param("code") String code);
}
