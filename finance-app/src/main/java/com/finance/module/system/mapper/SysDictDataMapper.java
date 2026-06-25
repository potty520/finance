package com.finance.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.system.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    List<SysDictData> selectByDictType(@Param("dictType") String dictType);
}
