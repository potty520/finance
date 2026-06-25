package com.finance.module.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.project.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
    List<Map<String, Object>> stats(@Param("status") String status);
}
