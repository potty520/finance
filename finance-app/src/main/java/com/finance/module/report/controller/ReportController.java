package com.finance.module.report.controller;

import com.finance.common.response.Result;
import com.finance.module.report.entity.FinReport;
import com.finance.module.report.mapper.FinReportMapper;
import com.finance.module.report.service.ReportServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
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

    /** CSV 导出 - 资产负债表 */
    @PostMapping("/export/balanceSheet")
    public void exportBalance(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.balanceSheet(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        exportCsv(response, "资产负债表", data);
    }

    /** CSV 导出 - 利润表 */
    @PostMapping("/export/incomeStatement")
    public void exportIncome(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.incomeStatement(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        exportCsv(response, "利润表", data);
    }

    /** CSV 导出 - 现金流量表 */
    @PostMapping("/export/cashFlow")
    public void exportCashflow(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        Map<String, Object> data = reportService.cashFlow(
                body.getOrDefault("fiscalYear", body.get("year")).toString(),
                Integer.valueOf(body.getOrDefault("fiscalPeriod", body.get("period")).toString()));
        exportCsv(response, "现金流量表", data);
    }

    private void exportCsv(HttpServletResponse response, String title, Map<String, Object> data) throws Exception {
        String fileName = java.net.URLEncoder.encode(title + ".csv", "UTF-8").replace("+", "%20");
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setCharacterEncoding("UTF-8");
        // 写 BOM 让 Excel 正确识别 UTF-8
        ServletOutputStream out = response.getOutputStream();
        out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        OutputStreamWriter w = new OutputStreamWriter(out, "UTF-8");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
        w.write("项目,期初余额,期末余额\n");
        if (items != null) {
            for (Map<String, Object> item : items) {
                w.write(String.format("%s,%s,%s\n",
                        item.getOrDefault("name", ""),
                        item.getOrDefault("beginBalance", ""),
                        item.getOrDefault("endBalance", "")));
            }
        }
        w.flush();
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
}
