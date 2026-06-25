package com.finance.module.consol.service;

import com.finance.module.consol.entity.ConsolOffset;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IConsolService {
    boolean addOffset(ConsolOffset offset);
    BigDecimal sumByType(String year, Integer period);
    List<Map<String, Object>> analysis(String year, Integer period);
    List<Map<String, Object>> analysis(String year, Integer period, String reportCode);
    boolean generate(String year, Integer period);
}
