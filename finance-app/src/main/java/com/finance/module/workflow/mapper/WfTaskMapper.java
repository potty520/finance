package com.finance.module.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.workflow.entity.WfTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WfTaskMapper extends BaseMapper<WfTask> {
    List<WfTask> selectByInstance(@Param("instanceId") Long instanceId);
    List<WfTask> selectPendingByUser(@Param("userId") Long userId);
}
