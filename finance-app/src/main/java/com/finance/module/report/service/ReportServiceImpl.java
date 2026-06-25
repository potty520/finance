package com.finance.module.report.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.module.ledger.mapper.GlBalanceMapper;
import com.finance.module.report.entity.FinReport;
import com.finance.module.report.mapper.FinReportMapper;
import com.finance.module.report.mapper.ReportMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReportServiceImpl {

    @Resource private FinReportMapper finReportMapper;
    @Resource private GlBalanceMapper balanceMapper;
    @Resource private ReportMapper reportQueryMapper;

    public Map<String, Object> balanceSheet(String year, Integer period) {
        List<Map<String, Object>> rows = balanceMapper.selectBalanceSheetItems(year, period);
        List<Map<String, Object>> items = new ArrayList<>();
        BigDecimal assetTotal = BigDecimal.ZERO;
        BigDecimal liabTotal = BigDecimal.ZERO;
        BigDecimal equityTotal = BigDecimal.ZERO;

        for (Map<String, Object> row : rows) {
            String categoryCode = String.valueOf(row.get("categoryCode"));
            String balanceDirection = String.valueOf(row.get("balanceDirection"));
            BigDecimal beginBalance = netBalance(
                    toDecimal(row.get("openingDebit")),
                    toDecimal(row.get("openingCredit")),
                    balanceDirection);
            BigDecimal endBalance = netBalance(
                    toDecimal(row.get("endingDebit")),
                    toDecimal(row.get("endingCredit")),
                    balanceDirection);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("code", row.get("subjectCode"));
            item.put("name", row.get("subjectName"));
            item.put("category", categoryCode);
            item.put("beginBalance", beginBalance);
            item.put("endBalance", endBalance);

            if ("ASSET".equals(categoryCode)) {
                item.put("type", "asset");
                assetTotal = assetTotal.add(endBalance);
            } else if ("LIAB".equals(categoryCode)) {
                item.put("type", "liab");
                liabTotal = liabTotal.add(endBalance);
            } else if ("EQUITY".equals(categoryCode)) {
                item.put("type", "liab");
                equityTotal = equityTotal.add(endBalance);
            } else {
                continue;
            }
            items.add(item);
        }

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("title", "资产负债表");
        report.put("year", year);
        report.put("period", period);
        report.put("items", items);
        report.put("assetTotal", assetTotal);
        report.put("liabTotal", liabTotal);
        report.put("equityTotal", equityTotal);
        report.put("liabEquityTotal", liabTotal.add(equityTotal));
        return report;
    }

    public Map<String, Object> incomeStatement(String year, Integer period) {
        List<Map<String, Object>> rows = reportQueryMapper.selectIncomeStatementItems(year, period);
        List<Map<String, Object>> items = new ArrayList<>();
        BigDecimal revenue = BigDecimal.ZERO;
        BigDecimal cost = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        BigDecimal revenueYear = BigDecimal.ZERO;
        BigDecimal costYear = BigDecimal.ZERO;
        BigDecimal expenseYear = BigDecimal.ZERO;

        for (Map<String, Object> row : rows) {
            String categoryCode = String.valueOf(row.get("categoryCode"));
            String balanceDirection = String.valueOf(row.get("balanceDirection"));
            BigDecimal current = periodAmount(
                    toDecimal(row.get("periodDebit")),
                    toDecimal(row.get("periodCredit")),
                    balanceDirection);
            BigDecimal cumulative = periodAmount(
                    toDecimal(row.get("yearDebit")),
                    toDecimal(row.get("yearCredit")),
                    balanceDirection);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("code", row.get("subjectCode"));
            item.put("name", row.get("subjectName"));
            item.put("category", categoryCode);
            item.put("current", current);
            item.put("cumulative", cumulative);
            items.add(item);

            if ("INCOME".equals(categoryCode)) {
                revenue = revenue.add(current);
                revenueYear = revenueYear.add(cumulative);
            } else if ("COST".equals(categoryCode)) {
                cost = cost.add(current);
                costYear = costYear.add(cumulative);
            } else if ("EXPENSE".equals(categoryCode)) {
                expense = expense.add(current);
                expenseYear = expenseYear.add(cumulative);
            }
        }

        BigDecimal profit = revenue.subtract(cost).subtract(expense);
        BigDecimal profitYear = revenueYear.subtract(costYear).subtract(expenseYear);

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("title", "利润表");
        report.put("year", year);
        report.put("period", period);
        report.put("items", items);
        report.put("revenue", revenue);
        report.put("cost", cost);
        report.put("expense", expense);
        report.put("profit", profit);
        report.put("revenueYear", revenueYear);
        report.put("costYear", costYear);
        report.put("expenseYear", expenseYear);
        report.put("profitYear", profitYear);
        return report;
    }

    public Map<String, Object> cashFlow(String year, Integer period) {
        List<Map<String, Object>> rows = reportQueryMapper.selectCashFlowItems(year, period);
        List<Map<String, Object>> operatingItems = new ArrayList<>();
        List<Map<String, Object>> investingItems = new ArrayList<>();
        List<Map<String, Object>> financingItems = new ArrayList<>();
        BigDecimal operating = BigDecimal.ZERO;
        BigDecimal investing = BigDecimal.ZERO;
        BigDecimal financing = BigDecimal.ZERO;

        for (Map<String, Object> row : rows) {
            BigDecimal amount = toDecimal(row.get("amount"));
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("code", row.get("itemCode"));
            item.put("name", row.get("itemName"));
            item.put("direction", row.get("direction"));
            item.put("amount", amount);

            String group = String.valueOf(row.get("flowGroup"));
            if ("operating".equals(group)) {
                operatingItems.add(item);
                operating = operating.add(amount);
            } else if ("investing".equals(group)) {
                investingItems.add(item);
                investing = investing.add(amount);
            } else {
                financingItems.add(item);
                financing = financing.add(amount);
            }
        }

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("title", "现金流量表");
        report.put("year", year);
        report.put("period", period);
        report.put("operatingItems", operatingItems);
        report.put("investingItems", investingItems);
        report.put("financingItems", financingItems);
        report.put("operating", operating);
        report.put("investing", investing);
        report.put("financing", financing);
        report.put("net", operating.add(investing).add(financing));
        return report;
    }

    public FinReport save(FinReport report) {
        if (report.getId() == null) {
            if (report.getCreateTime() == null) report.setCreateTime(LocalDateTime.now());
            if (report.getDeleted() == null) report.setDeleted(0);
            if (report.getStatus() == null) report.setStatus("A");
            finReportMapper.insert(report);
        } else {
            finReportMapper.updateById(report);
        }
        return report;
    }

    public List<FinReport> listCustom() {
        return finReportMapper.selectList(
                new LambdaQueryWrapper<FinReport>()
                        .eq(FinReport::getDeleted, 0)
                        .orderByDesc(FinReport::getCreateTime));
    }

    private BigDecimal netBalance(BigDecimal debit, BigDecimal credit, String direction) {
        if ("CREDIT".equalsIgnoreCase(direction)) {
            return credit.subtract(debit);
        }
        return debit.subtract(credit);
    }

    private BigDecimal periodAmount(BigDecimal debit, BigDecimal credit, String direction) {
        return netBalance(debit, credit, direction);
    }

    private BigDecimal toDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        return new BigDecimal(value.toString());
    }
}
