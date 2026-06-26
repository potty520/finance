package com.finance.module.tax.controller;

import com.finance.common.response.Result;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * 增值税申报底稿
 * 
 * 逻辑：从凭证分录中汇总"应交增值税(222101)"科目的借贷方发生额
 *   借方发生额 = 进项税额（采购时取得专票可抵扣）
 *   贷方发生额 = 销项税额（销售时开票应纳税）
 *   应纳税额 = 销项 - 进项
 */
@RestController
@RequestMapping("/tax")
public class TaxController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /** 增值税申报底稿数据 */
    @GetMapping("/vat/draft")
    public Result<Map<String, Object>> vatDraft(
            @RequestParam String fiscalYear,
            @RequestParam Integer fiscalPeriod) {

        String periodCode = fiscalYear + String.format("%02d", fiscalPeriod);

        // 销项税额 = 22210105 贷方发生额 (优先) 或 222101 贷方发生额 (兜底)
        String sqlOutput = "SELECT COALESCE(SUM(e.amount), 0) " +
            "FROM gl_voucher_entry e JOIN gl_voucher v ON e.voucher_id = v.id " +
            "WHERE v.period_code = ? AND v.deleted = 0 " +
            "AND ((e.subject_code = '22210105' AND e.dc_direction = 'CREDIT') " +
            "  OR (e.subject_code = '222101' AND e.dc_direction = 'CREDIT' AND NOT EXISTS " +
            "    (SELECT 1 FROM gl_account_subject s WHERE s.subject_code = '22210105')))";

        // 进项税额 = 22210101 借方发生额 (优先) 或 222101 借方发生额 (兜底)
        String sqlInput = "SELECT COALESCE(SUM(e.amount), 0) " +
            "FROM gl_voucher_entry e JOIN gl_voucher v ON e.voucher_id = v.id " +
            "WHERE v.period_code = ? AND v.deleted = 0 " +
            "AND ((e.subject_code = '22210101' AND e.dc_direction = 'DEBIT') " +
            "  OR (e.subject_code = '222101' AND e.dc_direction = 'DEBIT' AND NOT EXISTS " +
            "    (SELECT 1 FROM gl_account_subject s WHERE s.subject_code = '22210101')))";

        // 上期留抵 (简化：查上期应纳税额，若为负则为留抵)
        String prevPeriod = getPrevPeriod(fiscalYear, fiscalPeriod);
        BigDecimal prevOutput = jdbcTemplate.queryForObject(sqlOutput, BigDecimal.class, prevPeriod);
        BigDecimal prevInput = jdbcTemplate.queryForObject(sqlInput, BigDecimal.class, prevPeriod);
        BigDecimal prevNet = prevOutput.subtract(prevInput); // 正=应缴, 负=留抵
        BigDecimal prevCredit = prevNet.compareTo(BigDecimal.ZERO) < 0 ? prevNet.abs() : BigDecimal.ZERO;

        // 本期
        BigDecimal curOutput = jdbcTemplate.queryForObject(sqlOutput, BigDecimal.class, periodCode);
        BigDecimal curInput = jdbcTemplate.queryForObject(sqlInput, BigDecimal.class, periodCode);
        BigDecimal curNet = curOutput.subtract(curInput);

        // 不征税/免税收入 (从收入科目找不含税部分 — 简化从 222101 贷方倒推)
        // 销项税率假设 13%，则 不含税收入 = 销项税 / 0.13
        BigDecimal taxRate = new BigDecimal("0.13");
        BigDecimal taxableSales = BigDecimal.ZERO;
        if (curOutput.compareTo(BigDecimal.ZERO) > 0) {
            taxableSales = curOutput.divide(taxRate, 2, java.math.RoundingMode.HALF_UP);
        }

        // 进项税额转出（简化：假设 0，实际需人工填入）
        BigDecimal inputTransferOut = BigDecimal.ZERO;

        // 应抵扣税额合计 = 进项 + 上期留抵 - 进项转出
        BigDecimal totalDeductible = curInput.add(prevCredit).subtract(inputTransferOut);

        // 期末留抵 vs 应纳税额
        BigDecimal finalTax;
        BigDecimal finalCredit;
        if (totalDeductible.compareTo(curOutput) >= 0) {
            finalTax = BigDecimal.ZERO;
            finalCredit = totalDeductible.subtract(curOutput);
        } else {
            finalTax = curOutput.subtract(totalDeductible);
            finalCredit = BigDecimal.ZERO;
        }

        // 城建税 7%、教育费附加 3%、地方教育附加 2% = 合计 12%
        BigDecimal surtaxRate = new BigDecimal("0.12");
        BigDecimal cityTax = finalTax.multiply(new BigDecimal("0.07")).setScale(2, java.math.RoundingMode.HALF_UP);
        BigDecimal eduTax = finalTax.multiply(new BigDecimal("0.03")).setScale(2, java.math.RoundingMode.HALF_UP);
        BigDecimal localEduTax = finalTax.multiply(new BigDecimal("0.02")).setScale(2, java.math.RoundingMode.HALF_UP);
        BigDecimal totalSurtax = finalTax.multiply(surtaxRate).setScale(2, java.math.RoundingMode.HALF_UP);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("fiscalYear", fiscalYear);
        data.put("fiscalPeriod", fiscalPeriod);
        data.put("periodCode", periodCode);

        // 销项
        data.put("outputTax", curOutput.doubleValue());          // 销项税额
        data.put("taxableSales", taxableSales.doubleValue());     // 应税销售额(不含税)
        data.put("taxRate", 0.13);

        // 进项
        data.put("inputTax", curInput.doubleValue());             // 进项税额
        data.put("inputTransferOut", inputTransferOut.doubleValue()); // 进项转出
        data.put("prevCredit", prevCredit.doubleValue());         // 上期留抵

        // 计算
        data.put("totalDeductible", totalDeductible.doubleValue());
        data.put("finalTax", finalTax.doubleValue());             // 应纳税额
        data.put("finalCredit", finalCredit.doubleValue());       // 期末留抵

        // 附加税
        data.put("cityTax", cityTax.doubleValue());
        data.put("eduTax", eduTax.doubleValue());
        data.put("localEduTax", localEduTax.doubleValue());
        data.put("totalSurtax", totalSurtax.doubleValue());
        data.put("totalPayable", finalTax.add(totalSurtax).doubleValue()); // 合计应缴

        // 明细
        List<Map<String, Object>> details = new ArrayList<>();
        details.add(makeRow("销项税额", "按适用税率(13%)计税销售额", taxableSales.doubleValue(), curOutput.doubleValue()));
        details.add(makeRow("进项税额", "本期认证相符的增值税专用发票", null, curInput.doubleValue()));
        if (inputTransferOut.compareTo(BigDecimal.ZERO) > 0) {
            details.add(makeRow("进项税额转出", "不得抵扣的进项税额", null, -inputTransferOut.doubleValue()));
        }
        if (prevCredit.compareTo(BigDecimal.ZERO) > 0) {
            details.add(makeRow("上期留抵税额", "上期期末留抵税额", null, prevCredit.doubleValue()));
        }
        details.add(makeRow("应抵扣税额合计", "", null, totalDeductible.doubleValue()));
        details.add(makeRow("应纳税额", "销项-实际抵扣", null, finalTax.doubleValue()));
        if (finalCredit.compareTo(BigDecimal.ZERO) > 0) {
            details.add(makeRow("期末留抵税额", "结转下期抵扣", null, finalCredit.doubleValue()));
        }
        if (finalTax.compareTo(BigDecimal.ZERO) > 0) {
            details.add(makeRow("城市维护建设税", "应纳税额×7%", null, cityTax.doubleValue()));
            details.add(makeRow("教育费附加", "应纳税额×3%", null, eduTax.doubleValue()));
            details.add(makeRow("地方教育附加", "应纳税额×2%", null, localEduTax.doubleValue()));
            details.add(makeRow("合计应缴", "增值税+附加税", null, finalTax.add(totalSurtax).doubleValue()));
        }

        data.put("details", details);
        return Result.success(data);
    }

    private Map<String, Object> makeRow(String name, String desc, Double taxBase, Double amount) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("name", name);
        row.put("desc", desc);
        row.put("taxBase", taxBase != null ? taxBase : 0);
        row.put("amount", amount != null ? amount : 0);
        return row;
    }

    private String getPrevPeriod(String year, Integer period) {
        int y = Integer.parseInt(year);
        int p = period - 1;
        if (p <= 0) { y--; p = 12; }
        return y + String.format("%02d", p);
    }

    // ==================== Excel 导出 ====================

    @GetMapping("/vat/draft/excel")
    public void exportVatDraft(
            @RequestParam String fiscalYear,
            @RequestParam Integer fiscalPeriod,
            HttpServletResponse response) throws Exception {

        Map<String, Object> data = vatDraft(fiscalYear, fiscalPeriod).getData();

        String fileName = URLEncoder.encode("增值税申报底稿.xlsx", "UTF-8").replace("+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("增值税申报底稿");

        sheet.setColumnWidth(0, 22 * 256);
        sheet.setColumnWidth(1, 12 * 256);
        sheet.setColumnWidth(2, 16 * 256);
        sheet.setColumnWidth(3, 16 * 256);

        CellStyle titleStyle = titleStyle(wb);
        CellStyle headerStyle = headerStyle(wb);
        CellStyle amountStyle = amountStyle(wb);
        CellStyle boldStyle = boldStyle(wb);

        int r = 0;
        // 标题
        Row tr = sheet.createRow(r++);
        Cell tc = tr.createCell(0); tc.setCellValue("增值税申报底稿"); tc.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        Row sr = sheet.createRow(r++);
        Cell sc = sr.createCell(0);
        sc.setCellValue(fiscalYear + "年 " + String.format("%02d", fiscalPeriod) + "月   单位：元");
        sc.setCellStyle(normalCenterStyle(wb));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));

        r++; // 空行

        // 表头
        Row hr = sheet.createRow(r++);
        createHeadCell(hr, 0, "项目", headerStyle);
        createHeadCell(hr, 1, "计税依据", headerStyle);
        createHeadCell(hr, 2, "税率", headerStyle);
        createHeadCell(hr, 3, "税额", headerStyle);

        // 数据行
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> details = (List<Map<String, Object>>) data.get("details");
        for (Map<String, Object> row : details) {
            Row dr = sheet.createRow(r++);
            String name = String.valueOf(row.get("name"));
            boolean isBold = name.contains("合计") || name.contains("应纳税额");
            CellStyle cs = isBold ? boldStyle : null;
            createSCell(dr, 0, name, cs);
            
            Object tb = row.get("taxBase");
            if (tb instanceof Number && ((Number) tb).doubleValue() > 0) {
                createNCell(dr, 1, ((Number) tb).doubleValue(), amountStyle);
            }
            // 税率列：仅销项行填
            if ("销项税额".equals(name)) {
                createSCell(dr, 2, "13%", headerStyle);
            }
            Object amt = row.get("amount");
            if (amt instanceof Number) {
                createNCell(dr, 3, ((Number) amt).doubleValue(), amountStyle);
            }
        }

        wb.write(response.getOutputStream());
        wb.close();
    }

    // === 样式 ===
    private CellStyle titleStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont(); f.setBold(true); f.setFontHeightInPoints((short) 16);
        s.setFont(f); s.setAlignment(HorizontalAlignment.CENTER); s.setVerticalAlignment(VerticalAlignment.CENTER);
        return s;
    }
    private CellStyle headerStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont(); f.setBold(true); f.setFontHeightInPoints((short) 11);
        s.setFont(f); s.setAlignment(HorizontalAlignment.CENTER); s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setBorderTop(BorderStyle.THIN); s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN); s.setBorderRight(BorderStyle.THIN);
        s.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return s;
    }
    private CellStyle amountStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setAlignment(HorizontalAlignment.RIGHT); s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setBorderTop(BorderStyle.THIN); s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN); s.setBorderRight(BorderStyle.THIN);
        s.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
        return s;
    }
    private CellStyle boldStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont(); f.setBold(true);
        s.setFont(f); s.setAlignment(HorizontalAlignment.LEFT); s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setBorderTop(BorderStyle.THIN); s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN); s.setBorderRight(BorderStyle.THIN);
        return s;
    }
    private CellStyle normalCenterStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setAlignment(HorizontalAlignment.CENTER); s.setVerticalAlignment(VerticalAlignment.CENTER);
        return s;
    }
    private void createHeadCell(Row row, int col, String value, CellStyle style) {
        Cell c = row.createCell(col); c.setCellValue(value); c.setCellStyle(style);
    }
    private void createSCell(Row row, int col, String value, CellStyle style) {
        Cell c = row.createCell(col); c.setCellValue(value);
        if (style != null) c.setCellStyle(style);
    }
    private void createNCell(Row row, int col, double value, CellStyle style) {
        Cell c = row.createCell(col); c.setCellValue(value);
        if (style != null) c.setCellStyle(style);
    }
}
