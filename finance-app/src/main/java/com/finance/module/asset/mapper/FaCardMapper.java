package com.finance.module.asset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.asset.entity.FaCard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FaCardMapper extends BaseMapper<FaCard> {
    List<Map<String, Object>> sumByCategory();
}
