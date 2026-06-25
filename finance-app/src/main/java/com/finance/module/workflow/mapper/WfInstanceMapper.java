package com.finance.module.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.workflow.entity.WfInstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WfInstanceMapper extends BaseMapper<WfInstance> {
    List<WfInstance> selectByBusiness(@Param("businessType") String businessType, @Param("businessId") Long businessId);
}
