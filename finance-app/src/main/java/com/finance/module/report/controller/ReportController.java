package com.finance.module.report.controller;

import com.finance.common.response.Result;
import com.finance.module.report.entity.FinReport;
import com.finance.module.report.mapper.FinReportMapper;
import com.finance.module.report.service.ReportServiceImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource private ReportServiceImpl reportService;
    @Resource private FinReportMapper finReportMapper;

    @PostMapping("/balanceSheet")
    public Result<Map<String, Object>> balanceSheet(@RequestBody Map<String, Object> body) {
        String year = body.get("fiscalYear") != null ? body.get("fiscalYear").toString()
                : body.get("year") != null ? body.get("year").toString() : null;
        Object periodObj = body.get("fiscalPeriod") != null ? body.get("fiscalPeriod") : body.get("period");
        Integer period = periodObj == null ? null : Integer.valueOf(periodObj.toString());
        return Result.success(reportService.balanceSheet(year, period));
    }

    @PostMapping("/incomeStatement")
    public Result<Map<String, Object>> incomeStatement(@RequestBody Map<String, Object> body) {
        String year = body.get("fiscalYear") != null ? body.get("fiscalYear").toString()
                : body.get("year") != null ? body.get("year").toString() : null;
        Object periodObj = body.get("fiscalPeriod") != null ? body.get("fiscalPeriod") : body.get("period");
        Integer period = periodObj == null ? null : Integer.valueOf(periodObj.toString());
        return Result.success(reportService.incomeStatement(year, period));
    }

    @PostMapping("/cashFlow")
    public Result<Map<String, Object>> cashFlow(@RequestBody Map<String, Object> body) {
        String year = body.get("fiscalYear") != null ? body.get("fiscalYear").toString()
                : body.get("year") != null ? body.get("year").toString() : null;
        Object periodObj = body.get("fiscalPeriod") != null ? body.get("fiscalPeriod") : body.get("period");
        Integer period = periodObj == null ? null : Integer.valueOf(periodObj.toString());
        return Result.success(reportService.cashFlow(year, period));
    }

    // ==================== Excel 导出 ====================

    @PostMapping("/export/excel/balanceSheet")
    public void exportBalanceExcel(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.balanceSheet(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        exportExcel(response, "资产负债表", data);
    }

    @PostMapping("/export/excel/incomeStatement")
    public void exportIncomeExcel(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.incomeStatement(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        exportExcel(response, "利润表", data);
    }

    @PostMapping("/export/excel/cashFlow")
    public void exportCashflowExcel(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.cashFlow(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        exportExcel(response, "现金流量表", data);
    }

    // ==================== CSV 导出 ====================

    @PostMapping("/export/balanceSheet")
    public void exportBalance(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.balanceSheet(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        exportCsv(response, "资产负债表", data);
    }

    @PostMapping("/export/incomeStatement")
    public void exportIncome(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.incomeStatement(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        exportCsv(response, "利润表", data);
    }

    @PostMapping("/export/cashFlow")
    public void exportCashflow(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.cashFlow(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        exportCsv(response, "现金流量表", data);
    }

    // ==================== 打印用 HTML (可直接 Ctrl+P) ====================

    @PostMapping("/export/print/balanceSheet")
    public void printBalance(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.balanceSheet(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        printHtml(response, "资产负债表", data);
    }

    @PostMapping("/export/print/incomeStatement")
    public void printIncome(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.incomeStatement(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        printHtml(response, "利润表", data);
    }

    @PostMapping("/export/print/cashFlow")
    public void printCashflow(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.cashFlow(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        printHtml(response, "现金流量表", data);
    }

    // ==================== 内部工具方法 ====================

    @SuppressWarnings("unchecked")
    private void printHtml(HttpServletResponse response, String title, Map<String, Object> data) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter w = response.getWriter();

        String year = String.valueOf(data.getOrDefault("year", ""));
        String period = String.format("%02d", Integer.valueOf(String.valueOf(data.getOrDefault("period", "0"))));

        w.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>" + title + "</title>");
        w.println("<style>");
        w.println("*{margin:0;padding:0;box-sizing:border-box;}");
        w.println("body{font-family:'SimSun','宋体',serif;font-size:12pt;color:#000;padding:20px;}");
        w.println("h1{text-align:center;font-size:18pt;margin-bottom:4px;}");
        w.println(".subtitle{text-align:center;font-size:10pt;color:#666;margin-bottom:10px;}");
        w.println("table{width:100%;border-collapse:collapse;margin-bottom:8px;}");
        w.println("th,td{border:1px solid #000;padding:3px 6px;font-size:10pt;text-align:left;}");
        w.println("th{background:#f0f0f0;text-align:center;font-weight:bold;}");
        w.println(".num{text-align:right;font-variant-numeric:tabular-nums;}");
        w.println(".section{background:#e8e8e8;font-weight:bold;}");
        w.println(".total{font-weight:bold;border-top:2px solid #000;}");
        w.println("@media print{");
        w.println("  body{padding:0;}");
        w.println("  @page{size:A4;margin:12mm;}");
        w.println("  .no-print{display:none;}");
        w.println("}");
        w.println("@media screen{");
        w.println("  body{max-width:210mm;margin:0 auto;background:#fff;box-shadow:0 0 10px rgba(0,0,0,0.1);min-height:297mm;}");
        w.println("}");
        w.println("</style></head><body>");

        // 打印按钮
        w.println("<div class='no-print' style='text-align:center;margin-bottom:12px;'>");
        w.println("<button onclick='window.print()' style='padding:8px 24px;font-size:14px;cursor:pointer;'>🖨️ 打印</button>");
        w.println("</div>");

        w.println("<h1>" + title + "</h1>");
        w.println("<div class='subtitle'>" + year + "年 " + period + "月 &nbsp; 单位：元</div>");

        if ("资产负债表".equals(title)) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
            java.util.Map<String, Map<String, Object>> codeMap = new java.util.HashMap<>();
            if (items != null) for (Map<String, Object> it : items) codeMap.put(String.valueOf(it.get("code")), it);

            String[][] assetSections = {
                {"流动资产：","1001","1002","1012","1101","1102","1121","1122","1123","1124","1131","1132","1221","1231","1301","1401","1402","1403","1404","1405","1406","1407","1408","1411","1412","1413","1421","1431","1441","1461","1471","1481","1482"},
                {"非流动资产：","1501","1502","1503","1504","1511","1512","1521","1522","1523","1531","1532","1541","1601","1602","1603","1604","1605","1606","1611","1621","1622","1631","1632","1701","1702","1703","1711","1712","1801","1802","1811","1901"},
            };
            String[][] liabSections = {
                {"流动负债：","2001","2002","2101","2111","2201","2202","2203","2204","2211","22210101","22210102","22210103","22210104","22210105","22210106","22210107","22210108","222102","222103","222104","222105","222106","222107","222108","222109","222110","222111","222112","222113","222114","222115","2231","2232","2233","2234","2241","2401","2402","2712"},
                {"非流动负债：","2501","2502","2701","2702","2711","2801"},
                {"所有者权益：","4001","4002","4003","4101","4102","4103","4104","4105","4201","4301"},
            };

            java.util.List<String[]> assetRows = new java.util.ArrayList<>();
            int lnA = 0; double at = 0;
            for (String[] sec : assetSections) {
                assetRows.add(new String[]{sec[0], "", "", "", "section"});
                double sub = 0;
                for (int i = 1; i < sec.length; i++) {
                    Map<String, Object> it = codeMap.get(sec[i]);
                    if (it == null) continue;
                    lnA++;
                    double eb = Math.abs(toAmount(it.get("endBalance")));
                    double bb = Math.abs(toAmount(it.get("beginBalance")));
                    sub += eb;
                    assetRows.add(new String[]{"　" + it.get("name"), String.valueOf(lnA), fmtNum(eb), fmtNum(bb), "item"});
                }
                assetRows.add(new String[]{sec[0].replace("：", "合计"), "", fmtNum(sub), "", "total"});
                at += sub;
            }
            assetRows.add(new String[]{"资产总计", "", fmtNum(at), "", "total grand"});

            java.util.List<String[]> liabRows = new java.util.ArrayList<>();
            int lnL = 0; double lt = 0;
            for (String[] sec : liabSections) {
                liabRows.add(new String[]{sec[0], "", "", "", "section"});
                double sub = 0;
                for (int i = 1; i < sec.length; i++) {
                    Map<String, Object> it = codeMap.get(sec[i]);
                    if (it == null) continue;
                    lnL++;
                    double eb = Math.abs(toAmount(it.get("endBalance")));
                    double bb = Math.abs(toAmount(it.get("beginBalance")));
                    sub += eb;
                    liabRows.add(new String[]{"　" + it.get("name"), String.valueOf(lnL), fmtNum(eb), fmtNum(bb), "item"});
                }
                liabRows.add(new String[]{sec[0].replace("：", "合计"), "", fmtNum(sub), "", "total"});
                lt += sub;
            }
            liabRows.add(new String[]{"负债和所有者权益总计", "", fmtNum(lt), "", "total grand"});

            w.println("<table>");
            w.println("<tr><th>资产</th><th style='width:40px'>行次</th><th style='width:100px'>期末余额</th><th style='width:100px'>年初余额</th>" +
                "<th style='width:10px'></th><th>负债和所有者权益</th><th style='width:40px'>行次</th><th style='width:100px'>期末余额</th><th style='width:100px'>年初余额</th></tr>");
            int maxR = Math.max(assetRows.size(), liabRows.size());
            for (int i = 0; i < maxR; i++) {
                String[] al = i < assetRows.size() ? assetRows.get(i) : new String[]{"", "", "", "", ""};
                String[] ll = i < liabRows.size() ? liabRows.get(i) : new String[]{"", "", "", "", ""};
                String cls = al[4].contains("total") || ll[4].contains("total") ? "total" :
                    al[4].equals("section") || ll[4].equals("section") ? "section" : "";
                String grand = al[4].contains("grand") || ll[4].contains("grand") ? " border-bottom:double" : "";
                w.println("<tr class='" + cls + "' style='" + grand + "'>");
                w.println("<td>" + al[0] + "</td><td style='text-align:center'>" + al[1] + "</td><td class='num'>" + al[2] + "</td><td class='num'>" + al[3] + "</td>");
                w.println("<td></td>");
                w.println("<td>" + ll[0] + "</td><td style='text-align:center'>" + ll[1] + "</td><td class='num'>" + ll[2] + "</td><td class='num'>" + ll[3] + "</td>");
                w.println("</tr>");
            }
            w.println("</table>");
        } else if ("利润表".equals(title)) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
            java.util.Map<String, Map<String, Object>> cm2 = new java.util.HashMap<>();
            if (items != null) for (Map<String, Object> it : items) cm2.put(String.valueOf(it.get("code")), it);


            int ln = 0;
            java.util.List<String[]> isRows = new java.util.ArrayList<>();
            double rev = isAbsCur(cm2,"6001") + isAbsCur(cm2,"6051");
            double revC = isAbsCum(cm2,"6001") + isAbsCum(cm2,"6051");
            // 一、营业收入
            isRows.add(new String[]{"一、营业收入", "", "", "", "section"});
            isRows.add(new String[]{"　主营业务收入", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6001")), fmtNum(isAbsCum(cm2,"6001")), "item"});
            isRows.add(new String[]{"　其他业务收入", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6051")), fmtNum(isAbsCum(cm2,"6051")), "item"});
            isRows.add(new String[]{"营业收入合计", "", fmtNum(rev), fmtNum(revC), "subtotal"});

            double costC = isAbsCur(cm2,"5401") + isAbsCur(cm2,"5402");
            double costCc = isAbsCum(cm2,"5401") + isAbsCum(cm2,"5402");
            isRows.add(new String[]{"　减：营业成本", String.valueOf(++ln), fmtNum(costC), fmtNum(costCc), "item"});
            isRows.add(new String[]{"　　主营业务成本", "", fmtNum(isAbsCur(cm2,"5401")), fmtNum(isAbsCum(cm2,"5401")), "item"});
            isRows.add(new String[]{"　　其他业务成本", "", fmtNum(isAbsCur(cm2,"5402")), fmtNum(isAbsCum(cm2,"5402")), "item"});

            double taxC = isAbsCur(cm2,"6401");
            isRows.add(new String[]{"　税金及附加", String.valueOf(++ln), fmtNum(taxC), fmtNum(isAbsCum(cm2,"6401")), "item"});
            isRows.add(new String[]{"　销售费用", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6601")), fmtNum(isAbsCum(cm2,"6601")), "item"});
            isRows.add(new String[]{"　管理费用", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6602")), fmtNum(isAbsCum(cm2,"6602")), "item"});
            isRows.add(new String[]{"　研发费用", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"5301")), fmtNum(isAbsCum(cm2,"5301")), "item"});
            isRows.add(new String[]{"　财务费用", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6603")), fmtNum(isAbsCum(cm2,"6603")), "item"});
            isRows.add(new String[]{"　加：投资收益", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6111")), fmtNum(isAbsCum(cm2,"6111")), "item"});
            isRows.add(new String[]{"　加：公允价值变动损益", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6101")), fmtNum(isAbsCum(cm2,"6101")), "item"});
            isRows.add(new String[]{"　加：汇兑收益", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6061")), fmtNum(isAbsCum(cm2,"6061")), "item"});
            isRows.add(new String[]{"　减：信用减值损失", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6702")), fmtNum(isAbsCum(cm2,"6702")), "item"});
            isRows.add(new String[]{"　减：资产减值损失", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6701")), fmtNum(isAbsCum(cm2,"6701")), "item"});
            isRows.add(new String[]{"　加：资产处置收益", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6115")), fmtNum(isAbsCum(cm2,"6115")), "item"});

            double opP = rev - costC - taxC - isAbsCur(cm2,"6601") - isAbsCur(cm2,"6602") - isAbsCur(cm2,"5301") - isAbsCur(cm2,"6603")
                + isAbsCur(cm2,"6111") + isAbsCur(cm2,"6101") + isAbsCur(cm2,"6061") - isAbsCur(cm2,"6702") - isAbsCur(cm2,"6701") + isAbsCur(cm2,"6115");
            double opPC = revC - costCc - isAbsCum(cm2,"6401") - isAbsCum(cm2,"6601") - isAbsCum(cm2,"6602") - isAbsCum(cm2,"5301") - isAbsCum(cm2,"6603")
                + isAbsCum(cm2,"6111") + isAbsCum(cm2,"6101") + isAbsCum(cm2,"6061") - isAbsCum(cm2,"6702") - isAbsCum(cm2,"6701") + isAbsCum(cm2,"6115");
            isRows.add(new String[]{"二、营业利润", String.valueOf(++ln), fmtNum(opP), fmtNum(opPC), "subtotal"});

            isRows.add(new String[]{"　加：营业外收入", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6301")), fmtNum(isAbsCum(cm2,"6301")), "item"});
            isRows.add(new String[]{"　减：营业外支出", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6711")), fmtNum(isAbsCum(cm2,"6711")), "item"});
            double tp = opP + isAbsCur(cm2,"6301") - isAbsCur(cm2,"6711");
            double tpC = opPC + isAbsCum(cm2,"6301") - isAbsCum(cm2,"6711");
            isRows.add(new String[]{"三、利润总额", String.valueOf(++ln), fmtNum(tp), fmtNum(tpC), "subtotal"});

            isRows.add(new String[]{"　减：所得税费用", String.valueOf(++ln), fmtNum(isAbsCur(cm2,"6801")), fmtNum(isAbsCum(cm2,"6801")), "item"});
            double np = tp - isAbsCur(cm2,"6801");
            double npC = tpC - isAbsCum(cm2,"6801");
            isRows.add(new String[]{"四、净利润", String.valueOf(++ln), fmtNum(np), fmtNum(npC), "total"});

            w.println("<table>");
            w.println("<tr><th>项目</th><th style='width:40px'>行次</th><th style='width:110px'>本月金额</th><th style='width:110px'>本年累计</th></tr>");
            for (String[] row : isRows) {
                String cls = row[4].equals("total") ? "total grand" : row[4].equals("subtotal") ? "total" :
                    row[4].equals("section") ? "section" : "";
                w.println("<tr class='" + cls + "'>");
                w.println("<td>" + row[0] + "</td><td style='text-align:center'>" + row[1] + "</td><td class='num'>" + row[2] + "</td><td class='num'>" + row[3] + "</td>");
                w.println("</tr>");
            }
            w.println("</table>");
        } else if ("现金流量表".equals(title)) {
            int ln2 = 0;
            double netTotal = 0;
            java.util.List<String[]> cfRows = new java.util.ArrayList<>();

            String[][] cfSecs = {
                {"一、经营活动产生的现金流量", "operatingItems", "经营活动"},
                {"二、投资活动产生的现金流量", "investingItems", "投资活动"},
                {"三、筹资活动产生的现金流量", "financingItems", "筹资活动"},
            };
            for (String[] sec : cfSecs) {
                cfRows.add(new String[]{sec[0], "", "", "section"});
                Object obj = data.get(sec[1]);
                double sub = 0;
                if (obj instanceof List) {
                    List<Map<String, Object>> fitems = (List<Map<String, Object>>) obj;
                    for (Map<String, Object> it : fitems) {
                        double amt = toAmountOrZero(it.get("amount"));
                        sub += amt;
                        cfRows.add(new String[]{"　" + it.getOrDefault("name", ""), String.valueOf(++ln2), fmtNum(amt), "item"});
                    }
                }
                netTotal += sub;
                String label = sec[0].replace("经营活动产生的现金流量", "经营活动产生的现金流量净额")
                    .replace("投资活动产生的现金流量", "投资活动产生的现金流量净额")
                    .replace("筹资活动产生的现金流量", "筹资活动产生的现金流量净额");
                cfRows.add(new String[]{label, "", fmtNum(sub), "subtotal"});
            }

            double exch = toAmountOrZero(data.get("exchangeEffect"));
            cfRows.add(new String[]{"四、汇率变动对现金的影响", "", "", "section"});
            cfRows.add(new String[]{"　汇率变动对现金的影响", String.valueOf(++ln2), fmtNum(exch), "item"});
            netTotal += exch;

            cfRows.add(new String[]{"五、现金及现金等价物净增加额", "", fmtNum(netTotal), "net"});
            double begC = toAmountOrZero(data.get("beginCash"));
            cfRows.add(new String[]{"　加：期初现金及现金等价物余额", "", fmtNum(begC), "item"});
            double endC = begC + netTotal;
            cfRows.add(new String[]{"六、期末现金及现金等价物余额", "", fmtNum(endC), "grand"});

            w.println("<table>");
            w.println("<tr><th>项目</th><th style='width:40px'>行次</th><th style='width:120px'>本期金额</th></tr>");
            for (String[] row : cfRows) {
                String cls = row[3].equals("grand") ? "total grand" : row[3].equals("subtotal")||row[3].equals("net") ? "total" :
                    row[3].equals("section") ? "section" : "";
                w.println("<tr class='" + cls + "'>");
                w.println("<td>" + row[0] + "</td><td style='text-align:center'>" + row[1] + "</td><td class='num'>" + row[2] + "</td>");
                w.println("</tr>");
            }
            w.println("</table>");
        }

        w.println("</body></html>");
    }

    @SuppressWarnings("unchecked")
    private void printCashFlowSection(PrintWriter w, String section, Object itemsObj) {
        if (!(itemsObj instanceof List)) return;
        List<Map<String, Object>> items = (List<Map<String, Object>>) itemsObj;
        boolean first = true;
        for (Map<String, Object> it : items) {
            w.println("<tr>");
            if (first) {
                w.println("<td class='section' rowspan='" + items.size() + "'>" + section + "</td>");
                first = false;
            }
            w.println("<td>" + it.getOrDefault("name", "") + "</td>");
            w.println("<td class='num'>" + fmt(it.get("amount")) + "</td>");
            w.println("</tr>");
        }
    }

    private boolean isBoldRow(Map<String, Object> item) {
        if (item == null) return false;
        Object n = item.get("name");
        if (n == null) return false;
        String name = n.toString();
        return name.contains("合计") || name.contains("总计") || name.contains("净额") || name.contains("净利润");
    }

    private int indent(Map<String, Object> item) {
        if (item == null) return 10;
        Object n = item.get("name");
        if (n == null) return 10;
        String name = n.toString();
        // 有缩进的科目缩进更多
        if (name.startsWith("减：") || name.startsWith("其中：")) return 20;
        if (name.contains("准备") || name.contains("折旧") || name.contains("摊销")) return 15;
        return 10;
    }

    private String fmtNum(double v) {
        if (v == 0) return "";
        return String.format("%,.2f", v);
    }

    private String fmt(Object val) {
        if (val == null) return "";
        if (val instanceof Number) {
            BigDecimal bd = new BigDecimal(val.toString());
            if (bd.compareTo(BigDecimal.ZERO) == 0) return "";
            return String.format("%,.2f", bd.doubleValue());
        }
        return String.valueOf(val);
    }

    private void exportCsv(HttpServletResponse response, String title, Map<String, Object> data) throws Exception {
        String fileName = java.net.URLEncoder.encode(title + ".csv", "UTF-8").replace("+", "%20");
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        OutputStreamWriter w = new OutputStreamWriter(out, "UTF-8");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");

        if ("资产负债表".equals(title)) {
            w.write("项目,期初余额,期末余额\n");
            if (items != null) {
                for (Map<String, Object> item : items) {
                    w.write(String.format("%s,%s,%s\n",
                            item.getOrDefault("name", ""),
                            item.getOrDefault("beginBalance", ""),
                            item.getOrDefault("endBalance", "")));
                }
            }
        } else if ("现金流量表".equals(title)) {
            w.write("分类,项目,本期金额\n");
            writeCashFlowSection(w, "经营活动", data.get("operatingItems"));
            writeCashFlowSection(w, "投资活动", data.get("investingItems"));
            writeCashFlowSection(w, "筹资活动", data.get("financingItems"));
        } else {
            w.write("项目,本期金额,本年累计\n");
            if (items != null) {
                for (Map<String, Object> item : items) {
                    w.write(String.format("%s,%s,%s\n",
                            item.getOrDefault("name", ""),
                            item.getOrDefault("currentAmount", ""),
                            item.getOrDefault("cumulativeAmount", "")));
                }
            }
        }
        w.flush();
    }

    @SuppressWarnings("unchecked")
    private void writeCashFlowSection(OutputStreamWriter w, String section, Object itemsObj) throws Exception {
        if (itemsObj instanceof List) {
            List<Map<String, Object>> sectionItems = (List<Map<String, Object>>) itemsObj;
            w.write(section + ",,\n");
            for (Map<String, Object> item : sectionItems) {
                w.write(String.format(",%s,%s\n",
                        item.getOrDefault("name", ""),
                        item.getOrDefault("amount", "")));
            }
        }
    }

    // ==================== Excel 导出核心 ====================

    @SuppressWarnings("unchecked")
    private void exportExcel(HttpServletResponse response, String title, Map<String, Object> data) throws Exception {
        String fileName = java.net.URLEncoder.encode(title + ".xlsx", "UTF-8").replace("+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(title);

        // 列宽
        sheet.setColumnWidth(0, 18 * 256);
        sheet.setColumnWidth(1, 16 * 256);
        sheet.setColumnWidth(2, 16 * 256);

        CellStyle headerStyle = headerStyle(wb);
        CellStyle titleStyle = titleStyle(wb);
        CellStyle normalCenter = normalStyle(wb);
        CellStyle amountStyle = amountStyle(wb);
        CellStyle boldStyle = boldStyle(wb);

        int rowIdx = 0;

        // 标题行
        Row titleRow = sheet.createRow(rowIdx++);
        Cell tc = titleRow.createCell(0);
        tc.setCellValue(title);
        tc.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

        // 副标题
        String year = String.valueOf(data.getOrDefault("year", ""));
        String period = String.format("%02d", Integer.valueOf(String.valueOf(data.getOrDefault("period", "0"))));
        Row subRow = sheet.createRow(rowIdx++);
        Cell sc = subRow.createCell(0);
        sc.setCellValue(year + "年 " + period + "月   单位：元");
        sc.setCellStyle(normalCenter);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

        rowIdx++; // 空行

        // 打印设置：横向，缩放为一页宽
        sheet.getPrintSetup().setLandscape(true);
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 0);
        sheet.setFitToPage(true);

        if ("资产负债表".equals(title)) {
            buildBalanceSheetExcel(sheet, rowIdx, headerStyle, amountStyle, boldStyle, (List<Map<String, Object>>) data.get("items"));
        } else if ("利润表".equals(title)) {
            buildIncomeSheetExcel(sheet, rowIdx, headerStyle, amountStyle, boldStyle, (List<Map<String, Object>>) data.get("items"));
        } else if ("现金流量表".equals(title)) {
            buildCashFlowSheetExcel(sheet, rowIdx, headerStyle, amountStyle, boldStyle, data);
        }

        wb.write(response.getOutputStream());
        wb.close();
    }

    private void buildBalanceSheetExcel(Sheet sheet, int startRow, CellStyle header, CellStyle amount, CellStyle bold, List<Map<String, Object>> items) {
        // 国标双列格式: 资产 | 行次 | 期末余额 | 年初余额 || 负债和所有者权益 | 行次 | 期末余额 | 年初余额
        // 列: 0资产 1行次 2期末 3年初 4空白分隔 5负债权益 6行次 7期末 8年初
        sheet.setColumnWidth(0, 22 * 256);
        sheet.setColumnWidth(1, 7 * 256);
        sheet.setColumnWidth(2, 14 * 256);
        sheet.setColumnWidth(3, 14 * 256);
        sheet.setColumnWidth(4, 3 * 256);
        sheet.setColumnWidth(5, 22 * 256);
        sheet.setColumnWidth(6, 7 * 256);
        sheet.setColumnWidth(7, 14 * 256);
        sheet.setColumnWidth(8, 14 * 256);

        String[][] assetSections = {
            {"流动资产：", "1001","1002","1012","1101","1102","1121","1122","1123","1124","1131","1132","1221","1231","1301","1401","1402","1403","1404","1405","1406","1407","1408","1411","1412","1413","1421","1431","1441","1461","1471","1481","1482"},
            {"非流动资产：", "1501","1502","1503","1504","1511","1512","1521","1522","1523","1531","1532","1541","1601","1602","1603","1604","1605","1606","1611","1621","1622","1631","1632","1701","1702","1703","1711","1712","1801","1802","1811","1901"},
        };
        String[][] liabSections = {
            {"流动负债：", "2001","2002","2101","2111","2201","2202","2203","2204","2211","22210101","22210102","22210103","22210104","22210105","22210106","22210107","22210108","222102","222103","222104","222105","222106","222107","222108","222109","222110","222111","222112","222113","222114","222115","2231","2232","2233","2234","2241","2401","2402","2712"},
            {"非流动负债：", "2501","2502","2701","2702","2711","2801"},
            {"所有者权益：", "4001","4002","4003","4101","4102","4103","4104","4105","4201","4301"},
        };

        // 建立 code→item 映射
        java.util.Map<String, Map<String, Object>> codeMap = new java.util.HashMap<>();
        if (items != null) for (Map<String, Object> it : items) codeMap.put(String.valueOf(it.get("code")), it);

        // 表头行
        Row hr = sheet.createRow(startRow);
        createCell(hr, 0, "资产", header); createCell(hr, 1, "行次", header);
        createCell(hr, 2, "期末余额", header); createCell(hr, 3, "年初余额", header);
        createCell(hr, 5, "负债和所有者权益", header); createCell(hr, 6, "行次", header);
        createCell(hr, 7, "期末余额", header); createCell(hr, 8, "年初余额", header);

        int r = startRow + 1;
        int lnAsset = 0;
        double assetTotal = 0;
        java.util.List<java.util.Map<String, Object>> assetList = new java.util.ArrayList<>();
        for (String[] sec : assetSections) {
            assetList.add(rowItem(sec[0], null, null, null, true));
            double sub = 0;
            for (int i = 1; i < sec.length; i++) {
                Map<String, Object> it = codeMap.get(sec[i]);
                if (it == null) continue;
                lnAsset++;
                double eb = Math.abs(toAmount(it.get("endBalance")));
                double bb = Math.abs(toAmount(it.get("beginBalance")));
                sub += eb;
                assetList.add(rowItem("　" + it.get("name"), lnAsset, eb, bb, false));
            }
            assetList.add(rowItem(sec[0].replace("：","合计"), null, sub, null, true));
            assetTotal += sub;
        }
        assetList.add(rowItem("资产总计", null, assetTotal, null, true));

        int lnLiab = 0;
        double liabEquityTotal = 0;
        java.util.List<java.util.Map<String, Object>> liabList = new java.util.ArrayList<>();
        for (String[] sec : liabSections) {
            liabList.add(rowItem(sec[0], null, null, null, true));
            double sub = 0;
            for (int i = 1; i < sec.length; i++) {
                Map<String, Object> it = codeMap.get(sec[i]);
                if (it == null) continue;
                lnLiab++;
                double eb = Math.abs(toAmount(it.get("endBalance")));
                double bb = Math.abs(toAmount(it.get("beginBalance")));
                sub += eb;
                liabList.add(rowItem("　" + it.get("name"), lnLiab, eb, bb, false));
            }
            liabList.add(rowItem(sec[0].replace("：","合计"), null, sub, null, true));
            liabEquityTotal += sub;
        }
        liabList.add(rowItem("负债和所有者权益总计", null, liabEquityTotal, null, true));

        int maxRow = Math.max(assetList.size(), liabList.size());
        for (int i = 0; i < maxRow; i++) {
            Map<String, Object> al = i < assetList.size() ? assetList.get(i) : rowItem("", null, null, null, false);
            Map<String, Object> ll = i < liabList.size() ? liabList.get(i) : rowItem("", null, null, null, false);
            boolean isBold = Boolean.TRUE.equals(al.get("isBold")) || Boolean.TRUE.equals(ll.get("isBold"));
            boolean isTotal = isBold && (String.valueOf(al.get("name")).contains("总计") || String.valueOf(ll.get("name")).contains("总计"));
            CellStyle nameStyle = isBold ? bold : null;
            CellStyle amtStyle = isTotal ? totalAmountStyle(sheet.getWorkbook()) : amount;
            Row row = sheet.createRow(r++);
            createCell(row, 0, String.valueOf(al.getOrDefault("name", "")), nameStyle);
            createCell(row, 1, toStr(al.get("lineNo")), headerStyleSmall(sheet.getWorkbook()));
            createNumericCell(row, 2, toAmount(al.get("endBalance")), amtStyle);
            createNumericCell(row, 3, toAmount(al.get("beginBalance")), amount);
            createCell(row, 5, String.valueOf(ll.getOrDefault("name", "")), nameStyle);
            createCell(row, 6, toStr(ll.get("lineNo")), headerStyleSmall(sheet.getWorkbook()));
            createNumericCell(row, 7, toAmount(ll.get("endBalance")), amtStyle);
            createNumericCell(row, 8, toAmount(ll.get("beginBalance")), amount);
        }
    }

    private java.util.Map<String, Object> rowItem(String name, Object lineNo, Object endBalance, Object beginBalance, boolean isBold) {
        java.util.Map<String, Object> m = new java.util.HashMap<>();
        m.put("name", name); m.put("lineNo", lineNo); m.put("endBalance", endBalance);
        m.put("beginBalance", beginBalance); m.put("isBold", isBold);
        return m;
    }

    private String toStr(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    private CellStyle headerStyleSmall(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setBorderTop(BorderStyle.THIN); s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN); s.setBorderRight(BorderStyle.THIN);
        return s;
    }

    private CellStyle totalAmountStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont(); f.setBold(true);
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.RIGHT);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setBorderTop(BorderStyle.THIN); s.setBorderBottom(BorderStyle.DOUBLE);
        s.setBorderLeft(BorderStyle.THIN); s.setBorderRight(BorderStyle.THIN);
        s.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
        return s;
    }

    private void buildIncomeSheetExcel(Sheet sheet, int startRow, CellStyle header, CellStyle amount, CellStyle bold, List<Map<String, Object>> items) {
        sheet.setColumnWidth(0, 32 * 256);
        sheet.setColumnWidth(1, 8 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);

        Row hr = sheet.createRow(startRow);
        createCell(hr, 0, "项目", header);
        createCell(hr, 1, "行次", header);
        createCell(hr, 2, "本月金额", header);
        createCell(hr, 3, "本年累计", header);

        java.util.Map<String, Map<String, Object>> codeMap = new java.util.HashMap<>();
        if (items != null) for (Map<String, Object> it : items) codeMap.put(String.valueOf(it.get("code")), it);


        java.util.List<String[]> rows = new java.util.ArrayList<>();
        int ln = 0;
        double rev = isAbsCur(codeMap,"6001") + isAbsCur(codeMap,"6051");
        double revC = isAbsCum(codeMap,"6001") + isAbsCum(codeMap,"6051");
        addIS(rows, "一、营业收入", null, null, null, "section");
        addIS(rows, "　主营业务收入", ++ln, isAbsCur(codeMap,"6001"), isAbsCum(codeMap,"6001"), "item");
        addIS(rows, "　其他业务收入", ++ln, isAbsCur(codeMap,"6051"), isAbsCum(codeMap,"6051"), "item");
        addIS(rows, "营业收入合计", null, rev, revC, "subtotal");

        double costC = isAbsCur(codeMap,"5401") + isAbsCur(codeMap,"5402");
        double costCc = isAbsCum(codeMap,"5401") + isAbsCum(codeMap,"5402");
        addIS(rows, "　减：营业成本", ++ln, costC, costCc, "item");
        addIS(rows, "　　主营业务成本", null, isAbsCur(codeMap,"5401"), isAbsCum(codeMap,"5401"), "item");
        addIS(rows, "　　其他业务成本", null, isAbsCur(codeMap,"5402"), isAbsCum(codeMap,"5402"), "item");

        double taxC = isAbsCur(codeMap,"6401");
        addIS(rows, "　税金及附加", ++ln, taxC, isAbsCum(codeMap,"6401"), "item");
        addIS(rows, "　销售费用", ++ln, isAbsCur(codeMap,"6601"), isAbsCum(codeMap,"6601"), "item");
        addIS(rows, "　管理费用", ++ln, isAbsCur(codeMap,"6602"), isAbsCum(codeMap,"6602"), "item");
        addIS(rows, "　研发费用", ++ln, isAbsCur(codeMap,"5301"), isAbsCum(codeMap,"5301"), "item");
        addIS(rows, "　财务费用", ++ln, isAbsCur(codeMap,"6603"), isAbsCum(codeMap,"6603"), "item");
        addIS(rows, "　加：投资收益", ++ln, isAbsCur(codeMap,"6111"), isAbsCum(codeMap,"6111"), "item");
        addIS(rows, "　加：公允价值变动损益", ++ln, isAbsCur(codeMap,"6101"), isAbsCum(codeMap,"6101"), "item");
        addIS(rows, "　加：汇兑收益", ++ln, isAbsCur(codeMap,"6061"), isAbsCum(codeMap,"6061"), "item");
        addIS(rows, "　减：信用减值损失", ++ln, isAbsCur(codeMap,"6702"), isAbsCum(codeMap,"6702"), "item");
        addIS(rows, "　减：资产减值损失", ++ln, isAbsCur(codeMap,"6701"), isAbsCum(codeMap,"6701"), "item");
        addIS(rows, "　加：资产处置收益", ++ln, isAbsCur(codeMap,"6115"), isAbsCum(codeMap,"6115"), "item");

        double opP = rev - costC - taxC - isAbsCur(codeMap,"6601") - isAbsCur(codeMap,"6602") - isAbsCur(codeMap,"5301") - isAbsCur(codeMap,"6603")
            + isAbsCur(codeMap,"6111") + isAbsCur(codeMap,"6101") + isAbsCur(codeMap,"6061") - isAbsCur(codeMap,"6702") - isAbsCur(codeMap,"6701") + isAbsCur(codeMap,"6115");
        double opPC = revC - costCc - isAbsCum(codeMap,"6401") - isAbsCum(codeMap,"6601") - isAbsCum(codeMap,"6602") - isAbsCum(codeMap,"5301") - isAbsCum(codeMap,"6603")
            + isAbsCum(codeMap,"6111") + isAbsCum(codeMap,"6101") + isAbsCum(codeMap,"6061") - isAbsCum(codeMap,"6702") - isAbsCum(codeMap,"6701") + isAbsCum(codeMap,"6115");
        addIS(rows, "二、营业利润", ++ln, opP, opPC, "subtotal");

        addIS(rows, "　加：营业外收入", ++ln, isAbsCur(codeMap,"6301"), isAbsCum(codeMap,"6301"), "item");
        addIS(rows, "　减：营业外支出", ++ln, isAbsCur(codeMap,"6711"), isAbsCum(codeMap,"6711"), "item");

        double tp = opP + isAbsCur(codeMap,"6301") - isAbsCur(codeMap,"6711");
        double tpC = opPC + isAbsCum(codeMap,"6301") - isAbsCum(codeMap,"6711");
        addIS(rows, "三、利润总额", ++ln, tp, tpC, "subtotal");

        addIS(rows, "　减：所得税费用", ++ln, isAbsCur(codeMap,"6801"), isAbsCum(codeMap,"6801"), "item");
        double np = tp - isAbsCur(codeMap,"6801");
        double npC = tpC - isAbsCum(codeMap,"6801");
        addIS(rows, "四、净利润", ++ln, np, npC, "total");

        int r = startRow + 1;
        for (String[] row : rows) {
            Row erow = sheet.createRow(r++);
            CellStyle ns = row[4].equals("section") ? bold : (row[4].equals("subtotal")||row[4].equals("total")) ? bold : null;
            CellStyle as = row[4].equals("total") ? totalAmountStyle(sheet.getWorkbook()) : amount;
            createCell(erow, 0, row[0], ns);
            createCell(erow, 1, row[1] != null ? row[1] : "", headerStyleSmall(sheet.getWorkbook()));
            if (row[2] != null) createNumericCell(erow, 2, Double.parseDouble(row[2]), as);
            if (row[3] != null) createNumericCell(erow, 3, Double.parseDouble(row[3]), as);
        }
    }

    private void addIS(java.util.List<String[]> rows, String name, Object line, Object cur, Object cum, String cls) {
        String lns = line instanceof Integer ? String.valueOf(line) : (line != null ? line.toString() : "");
        String cs = cur instanceof Double ? String.valueOf(cur) : (cur != null ? cur.toString() : null);
        String cus = cum instanceof Double ? String.valueOf(cum) : (cum != null ? cum.toString() : null);
        rows.add(new String[]{name, lns, cs, cus, cls, "ignore"});
    }

    private void addIS(java.util.List<String[]> rows, String name, Integer line, Double cur, Double cum, String cls) {
        addIS(rows, name, (Object)line, cur, cum, cls);
    }

    @SuppressWarnings("unchecked")
    private void buildCashFlowSheetExcel(Sheet sheet, int startRow, CellStyle header, CellStyle amount, CellStyle bold, Map<String, Object> data) {
        sheet.setColumnWidth(0, 34 * 256);
        sheet.setColumnWidth(1, 8 * 256);
        sheet.setColumnWidth(2, 15 * 256);

        Row hr = sheet.createRow(startRow);
        createCell(hr, 0, "项目", header);
        createCell(hr, 1, "行次", header);
        createCell(hr, 2, "本期金额", header);

        java.util.List<String[]> rows = new java.util.ArrayList<>();
        int ln = 0;
        double netTotal = 0;

        String[][] sections = {
            {"一、经营活动产生的现金流量", "operatingItems"},
            {"二、投资活动产生的现金流量", "investingItems"},
            {"三、筹资活动产生的现金流量", "financingItems"},
        };
        for (String[] sec : sections) {
            rows.add(new String[]{sec[0], "", null, "section", "ignore"});
            Object obj = data.get(sec[1]);
            double sub = 0;
            if (obj instanceof List) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) obj;
                for (Map<String, Object> it : items) {
                    double amt = toAmountOrZero(it.get("amount"));
                    sub += amt;
                    rows.add(new String[]{"　" + it.getOrDefault("name", ""), String.valueOf(++ln), fmtStr(amt), "item"});
                }
            }
            netTotal += sub;
            rows.add(new String[]{sec[0].replace("经营活动产生的现金流量", "经营活动产生的现金流量净额")
                .replace("投资活动产生的现金流量", "投资活动产生的现金流量净额")
                .replace("筹资活动产生的现金流量", "筹资活动产生的现金流量净额"), null, fmtStr(sub), "subtotal"});
        }

        // 汇率变动
        double exch = toAmountOrZero(data.get("exchangeEffect"));
        rows.add(new String[]{"四、汇率变动对现金的影响", null, null, "section", "ignore"});
        rows.add(new String[]{"　汇率变动对现金的影响", String.valueOf(++ln), fmtStr(exch), "item"});
        netTotal += exch;

        // 净增加额
        rows.add(new String[]{"五、现金及现金等价物净增加额", null, fmtStr(netTotal), "net"});

        double beg = toAmountOrZero(data.get("beginCash"));
        rows.add(new String[]{"　加：期初现金及现金等价物余额", null, fmtStr(beg), "item"});

        double end = beg + netTotal;
        rows.add(new String[]{"六、期末现金及现金等价物余额", null, fmtStr(end), "grand"});

        int r = startRow + 1;
        for (String[] row : rows) {
            Row erow = sheet.createRow(r++);
            boolean isBold = row[3].equals("section") || row[3].equals("subtotal") || row[3].equals("net") || row[3].equals("grand");
            CellStyle ns = isBold ? bold : null;
            CellStyle as = row[3].equals("grand") ? totalAmountStyle(sheet.getWorkbook()) : amount;
            createCell(erow, 0, row[0], ns);
            createCell(erow, 1, row[1] != null ? row[1] : "", headerStyleSmall(sheet.getWorkbook()));
            if (row[2] != null) {
                try { createNumericCell(erow, 2, Double.parseDouble(row[2]), as); }
                catch (Exception e) { createCell(erow, 2, row[2], as); }
            }
        }
    }

    private String fmtStr(double v) {
        return String.valueOf(v);
    }

    // === Excel 样式工厂 ===

    private CellStyle headerStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont(); f.setBold(true); f.setFontHeightInPoints((short) 11);
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setBorderTop(BorderStyle.THIN); s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN); s.setBorderRight(BorderStyle.THIN);
        s.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return s;
    }

    private CellStyle titleStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont(); f.setBold(true); f.setFontHeightInPoints((short) 16);
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        return s;
    }

    private CellStyle normalStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        return s;
    }

    private CellStyle amountStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setAlignment(HorizontalAlignment.RIGHT);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setBorderTop(BorderStyle.THIN); s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN); s.setBorderRight(BorderStyle.THIN);
        s.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
        return s;
    }

    private CellStyle boldStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont(); f.setBold(true);
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.LEFT);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setBorderTop(BorderStyle.THIN); s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN); s.setBorderRight(BorderStyle.THIN);
        return s;
    }

    // === 辅助 ===

    private void createCell(Row row, int col, String value, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(value != null ? value : "");
        if (style != null) c.setCellStyle(style);
    }

    private void createNumericCell(Row row, int col, Double value, CellStyle style) {
        Cell c = row.createCell(col);
        if (value != null) c.setCellValue(value);
        if (style != null) c.setCellStyle(style);
    }

    private Double toAmount(Object val) {
        if (val == null) return null;
        if (val instanceof Number) return ((Number) val).doubleValue();
        try { return Double.parseDouble(val.toString()); } catch (Exception e) { return null; }
    }

    private double toAmountOrZero(Object val) {
        Double v = toAmount(val);
        return v == null ? 0 : v;
    }

    @GetMapping("/custom/list")
    public Result<List<FinReport>> list() {
        return Result.success(reportService.listCustom());
    }

    @PostMapping("/custom")
    public Result<FinReport> save(@RequestBody FinReport r) {
        return Result.success(reportService.save(r));
    }

    @DeleteMapping("/custom/{id}")
    public Result<Boolean> del(@PathVariable Long id) {
        return Result.success(finReportMapper.deleteById(id) > 0);
    }

    private double isAbsCur(java.util.Map<String, java.util.Map<String, Object>> map, String code) {
        if (map == null || !map.containsKey(code)) return 0;
        return Math.abs(toAmount(map.get(code).get("current")));
    }
    private double isAbsCum(java.util.Map<String, java.util.Map<String, Object>> map, String code) {
        if (map == null || !map.containsKey(code)) return 0;
        return Math.abs(toAmount(map.get(code).get("cumulative")));
    }
}