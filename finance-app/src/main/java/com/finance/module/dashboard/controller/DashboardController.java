package com.finance.module.dashboard.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.finance.common.response.Result;
import com.finance.module.ledger.entity.GlVoucher;
import com.finance.module.ledger.mapper.GlVoucherMapper;
import com.finance.module.payable.entity.ApInvoice;
import com.finance.module.payable.entity.ApSupplier;
import com.finance.module.payable.mapper.ApInvoiceMapper;
import com.finance.module.payable.mapper.ApSupplierMapper;
import com.finance.module.receivable.entity.ArCustomer;
import com.finance.module.receivable.entity.ArInvoice;
import com.finance.module.receivable.mapper.ArCustomerMapper;
import com.finance.module.receivable.mapper.ArInvoiceMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作台统计聚合接口
 * 供前端 Dashboard 页面使用，聚合凭证、应收应付、客户供应商等真实数据
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Resource private GlVoucherMapper voucherMapper;
    @Resource private ArCustomerMapper customerMapper;
    @Resource private ApSupplierMapper supplierMapper;
    @Resource private ArInvoiceMapper arInvoiceMapper;
    @Resource private ApInvoiceMapper apInvoiceMapper;

    private static final DateTimeFormatter YM = DateTimeFormatter.ofPattern("yyyy-MM");

    /** 工作台汇总卡片数据 */
    @GetMapping("/summary")
    public Result<Map<String, Object>> summary() {
        Map<String, Object> data = new HashMap<>();
        LocalDate now = LocalDate.now();
        String ym = now.format(YM);

        // 本月凭证汇总（借贷发生额 + 凭证数）
        QueryWrapper<GlVoucher> vqw = new QueryWrapper<>();
        vqw.select("IFNULL(SUM(total_debit),0) AS debit", "IFNULL(SUM(total_credit),0) AS credit", "COUNT(*) AS cnt")
           .eq("deleted", 0)
           .apply("DATE_FORMAT(voucher_date,'%Y-%m') = {0}", ym);
        Map<String, Object> month = voucherMapper.selectMaps(vqw).isEmpty()
                ? new HashMap<>() : voucherMapper.selectMaps(vqw).get(0);
        data.put("monthDebit", num(month.get("debit")));
        data.put("monthCredit", num(month.get("credit")));
        data.put("monthVoucherCount", month.getOrDefault("cnt", 0));

        // 凭证总数
        data.put("voucherTotal", voucherMapper.selectCount(
                new LambdaQueryWrapper<GlVoucher>().eq(GlVoucher::getDeleted, 0)));

        // 客户/供应商数
        data.put("customerCount", customerMapper.selectCount(
                new LambdaQueryWrapper<ArCustomer>()
                        .eq(ArCustomer::getStatus, 1)
                        .eq(ArCustomer::getDeleted, 0)));
        data.put("supplierCount", supplierMapper.selectCount(
                new LambdaQueryWrapper<ApSupplier>()
                        .eq(ApSupplier::getStatus, 1)
                        .eq(ApSupplier::getDeleted, 0)));

        // 应收未收 / 应付未付
        data.put("arUncollected", sumColumn(arInvoiceMapper.selectMaps(new QueryWrapper<ArInvoice>()
                .select("IFNULL(SUM(uncollected_amount),0) AS v").eq("deleted", 0).gt("uncollected_amount", 0))));
        data.put("apUnpaid", sumColumn(apInvoiceMapper.selectMaps(new QueryWrapper<ApInvoice>()
                .select("IFNULL(SUM(unpaid_amount),0) AS v").eq("deleted", 0).gt("unpaid_amount", 0))));

        // 待审核凭证数
        data.put("pendingVoucherCount", voucherMapper.selectCount(
                new LambdaQueryWrapper<GlVoucher>()
                        .eq(GlVoucher::getDeleted, 0)
                        .in(GlVoucher::getStatus, "SUBMITTED", "APPROVING", "A")));

        return Result.success(data);
    }

    /** 近 N 月借贷发生额趋势（用于趋势图） */
    @GetMapping("/trend")
    public Result<List<Map<String, Object>>> trend(@RequestParam(defaultValue = "6") Integer months) {
        QueryWrapper<GlVoucher> qw = new QueryWrapper<>();
        qw.select("DATE_FORMAT(voucher_date,'%Y-%m') AS ym",
                  "IFNULL(SUM(total_debit),0) AS debit",
                  "IFNULL(SUM(total_credit),0) AS credit",
                  "COUNT(*) AS cnt")
          .eq("deleted", 0)
          .apply("voucher_date >= DATE_SUB(CURDATE(), INTERVAL {0} MONTH)", months)
          .groupBy("ym")
          .orderByAsc("ym");
        return Result.success(voucherMapper.selectMaps(qw));
    }

    /** 最近凭证 */
    @GetMapping("/recentVouchers")
    public Result<List<GlVoucher>> recentVouchers(@RequestParam(defaultValue = "8") Integer limit) {
        List<GlVoucher> list = voucherMapper.selectList(
                new LambdaQueryWrapper<GlVoucher>()
                        .eq(GlVoucher::getDeleted, 0)
                        .orderByDesc(GlVoucher::getVoucherDate)
                        .orderByDesc(GlVoucher::getId)
                        .last("LIMIT " + Math.min(Math.max(limit, 1), 50)));
        return Result.success(list == null ? new ArrayList<>() : list);
    }

    /** 预警列表：应收逾期 / 应付到期 / 待审核凭证 */
    @GetMapping("/alerts")
    public Result<List<Map<String, Object>>> alerts() {
        List<Map<String, Object>> list = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // 待审核凭证
        List<GlVoucher> pendings = voucherMapper.selectList(
                new LambdaQueryWrapper<GlVoucher>()
                        .eq(GlVoucher::getDeleted, 0)
                        .in(GlVoucher::getStatus, "SUBMITTED", "APPROVING", "A")
                        .orderByDesc(GlVoucher::getVoucherDate)
                        .last("LIMIT 5"));
        for (GlVoucher v : pendings) {
            Map<String, Object> m = new HashMap<>();
            m.put("type", "pending");
            m.put("level", "warning");
            m.put("title", "待审核凭证 " + v.getVoucherNo());
            m.put("desc", (v.getVoucherDate() == null ? "" : v.getVoucherDate().toString()) + " 借贷 "
                    + (v.getTotalDebit() == null ? 0 : v.getTotalDebit()));
            list.add(m);
        }

        // 应收逾期（未收且已过到期日）
        List<Map<String, Object>> arRows = arInvoiceMapper.selectMaps(
                new QueryWrapper<ArInvoice>()
                        .select("bill_no", "customer_name", "uncollected_amount", "due_date")
                        .eq("deleted", 0)
                        .gt("uncollected_amount", 0)
                        .lt("due_date", today)
                        .orderByAsc("due_date")
                        .last("LIMIT 5"));
        for (Map<String, Object> r : arRows) {
            Map<String, Object> m = new HashMap<>();
            m.put("type", "arOverdue");
            m.put("level", "danger");
            m.put("title", "应收逾期 " + safeStr(r.get("bill_no")));
            m.put("desc", safeStr(r.get("customer_name")) + " 未收 " + r.get("uncollected_amount")
                    + " 元，到期日 " + safeStr(r.get("due_date")));
            list.add(m);
        }

        // 应付 30 天内到期
        List<Map<String, Object>> apRows = apInvoiceMapper.selectMaps(
                new QueryWrapper<ApInvoice>()
                        .select("bill_no", "supplier_name", "unpaid_amount", "due_date")
                        .eq("deleted", 0)
                        .gt("unpaid_amount", 0)
                        .ge("due_date", today)
                        .le("due_date", today.plusDays(30))
                        .orderByAsc("due_date")
                        .last("LIMIT 5"));
        for (Map<String, Object> r : apRows) {
            Map<String, Object> m = new HashMap<>();
            m.put("type", "apDue");
            m.put("level", "info");
            m.put("title", "应付到期 " + safeStr(r.get("bill_no")));
            m.put("desc", safeStr(r.get("supplier_name")) + " 应付 " + r.get("unpaid_amount")
                    + " 元，到期日 " + safeStr(r.get("due_date")));
            list.add(m);
        }

        return Result.success(list);
    }

    private static BigDecimal num(Object o) {
        if (o == null) return BigDecimal.ZERO;
        try { return new BigDecimal(o.toString()); } catch (Exception e) { return BigDecimal.ZERO; }
    }

    private static BigDecimal sumColumn(List<Map<String, Object>> rows) {
        if (rows == null || rows.isEmpty()) return BigDecimal.ZERO;
        return num(rows.get(0).get("v"));
    }

    private static String safeStr(Object o) {
        return o == null ? "" : o.toString();
    }
}
