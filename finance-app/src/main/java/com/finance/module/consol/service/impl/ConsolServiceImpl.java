package com.finance.module.consol.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.module.consol.entity.ConsolOffset;
import com.finance.module.consol.mapper.ConsolOffsetMapper;
import com.finance.module.consol.service.IConsolService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsolServiceImpl implements IConsolService {

    @Resource private ConsolOffsetMapper offsetMapper;

    @Override
    public boolean addOffset(ConsolOffset offset) {
        if (offset.getStatus() == null) offset.setStatus("A");
        if (offset.getDeleted() == null) offset.setDeleted(0);
        return offsetMapper.insert(offset) > 0;
    }

    @Override
    public BigDecimal sumByType(String year, Integer period) {
        return offsetMapper.sumByType(year, period);
    }

    @Override
    public List<Map<String, Object>> analysis(String year, Integer period) {
        return analysis(year, period, null);
    }

    @Override
    public List<Map<String, Object>> analysis(String year, Integer period, String reportCode) {
        LambdaQueryWrapper<ConsolOffset> qw = new LambdaQueryWrapper<ConsolOffset>()
                .eq(ConsolOffset::getFiscalYear, Integer.valueOf(year))
                .eq(ConsolOffset::getFiscalPeriod, period)
                .eq(ConsolOffset::getDeleted, 0)
                .orderByAsc(ConsolOffset::getOffsetType)
                .orderByAsc(ConsolOffset::getSubjectCode);
        List<ConsolOffset> offsets = offsetMapper.selectList(qw);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (ConsolOffset offset : offsets) {
            BigDecimal amount = offset.getAmount() != null ? offset.getAmount() : BigDecimal.ZERO;
            BigDecimal elim = amount.negate();
            Map<String, Object> row = new HashMap<>();
            row.put("rowCode", offset.getSubjectCode());
            row.put("rowName", StringUtils.hasText(offset.getSubjectName()) ? offset.getSubjectName() : offset.getSummary());
            row.put("reportCode", StringUtils.hasText(reportCode) ? reportCode : "BS");
            row.put("companyName", offset.getSourceCompany());
            row.put("offsetType", offset.getOffsetType());
            row.put("summary", offset.getSummary());
            row.put("amount", amount);
            row.put("adjAmount", BigDecimal.ZERO);
            row.put("elimAmount", elim);
            row.put("finalAmount", amount.add(elim));
            rows.add(row);
        }
        return rows;
    }

    @Override
    public boolean generate(String year, Integer period) {
        // 合并底稿生成（简化：实际应汇总各公司报表并按股权比例调整）
        Map<String, Object> result = new HashMap<>();
        result.put("year", year);
        result.put("period", period);
        result.put("status", "generated");
        return true;
    }
}
