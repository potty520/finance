package com.finance.module.project.service;

import com.finance.module.project.entity.Project;

public interface IProjectService {
    Project save(Project p);
    boolean updateUsedAmount(Long id, java.math.BigDecimal used);
}
