package com.finance.module.project.service.impl;

import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import com.finance.module.project.entity.Project;
import com.finance.module.project.mapper.ProjectMapper;
import com.finance.module.project.service.IProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class ProjectServiceImpl implements IProjectService {

    @Resource private ProjectMapper projectMapper;

    @Override
    public Project save(Project p) {
        if (p.getProjectCode() == null) p.setProjectCode("PJ-" + System.currentTimeMillis());
        if (p.getStatus() == null) p.setStatus("0");
        if (p.getUsedAmount() == null) p.setUsedAmount(BigDecimal.ZERO);
        if (p.getId() == null) projectMapper.insert(p);
        else projectMapper.updateById(p);
        return p;
    }

    @Override
    public boolean updateUsedAmount(Long id, BigDecimal used) {
        Project p = projectMapper.selectById(id);
        if (p == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        p.setUsedAmount((p.getUsedAmount() == null ? BigDecimal.ZERO : p.getUsedAmount()).add(used));
        if (p.getBudgetAmount() != null && p.getUsedAmount().compareTo(p.getBudgetAmount()) > 0) {
            throw new BusinessException("项目预算超限");
        }
        return projectMapper.updateById(p) > 0;
    }
}
