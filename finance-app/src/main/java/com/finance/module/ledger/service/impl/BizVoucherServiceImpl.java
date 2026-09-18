package com.finance.module.ledger.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import com.finance.module.expense.entity.ExpenseApply;
import com.finance.module.expense.mapper.ExpenseApplyMapper;
import com.finance.module.ledger.entity.GlPeriod;
import com.finance.module.ledger.entity.GlVoucher;
import com.finance.module.ledger.entity.GlVoucherEntry;
import com.finance.module.ledger.mapper.GlPeriodMapper;
import com.finance.module.ledger.service.BizVoucherService;
import com.finance.module.ledger.service.IGlVoucherService;
import com.finance.module.payable.entity.ApInvoice;
import com.finance.module.payable.entity.ApPayment;
import com.finance.module.payable.mapper.ApInvoiceMapper;
import com.finance.module.payable.mapper.ApPaymentMapper;
import com.finance.module.receivable.entity.ArInvoice;
import com.finance.module.receivable.entity.ArReceipt;
import com.finance.module.receivable.mapper.ArInvoiceMapper;
import com.finance.module.receivable.mapper.ArReceiptMapper;
import com.finance.module.system.entity.SysConfig;
import com.finance.module.system.mapper.SysConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 业财一体：业务单据生成记账凭证。
 *
 * <p>科目映射取自 sys_config（config_type='gl'，键前缀 gl.biz.*），可在系统参数中调整，
 * 无需改代码即可适配不同企业的科目体系。</p>
 */
@Slf4j
@Service
public class BizVoucherServiceImpl implements BizVoucherService {

    private static final DateTimeFormatter PERIOD_FMT = DateTimeFormatter.ofPattern("yyyyMM");
    private static final BigDecimal CENT = new BigDecimal("0.01");

    @Resource private IGlVoucherService voucherService;
    @Resource private GlPeriodMapper periodMapper;
    @Resource private SysConfigMapper configMapper;
    @Resource private ArInvoiceMapper arInvoiceMapper;
    @Resource private ArReceiptMapper arReceiptMapper;
    @Resource private ApInvoiceMapper apInvoiceMapper;
    @Resource private ApPaymentMapper apPaymentMapper;
    @Resource private ExpenseApplyMapper expenseApplyMapper;
    @Resource private JdbcTemplate jdbcTemplate;

    // ==================== 应收 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long fromArInvoice(Long invoiceId) {
        ArInvoice inv = arInvoiceMapper.selectById(invoiceId);
        if (inv == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        requireAudited(inv.getStatus(), "销售发票");
        if (inv.getVoucherId() != null) {
            throw new BusinessException("该发票已生成凭证 " + StrUtil.nullToEmpty(inv.getVoucherNo()) + "，请勿重复生成");
        }
        BigDecimal amount = nz(inv.getAmount());
        BigDecimal tax = nz(inv.getTaxAmount());
        BigDecimal total = nz(inv.getTotalAmount());
        if (total.signum() <= 0) throw new BusinessException("发票价税合计必须大于 0");
        if (total.subtract(amount.add(tax)).abs().compareTo(CENT) > 0) {
            throw new BusinessException("发票金额与税额之和不等于价税合计，请先修正单据");
        }
        String revenue = pickSubject(inv.getSubjectCode(),
                "2".equals(inv.getInvoiceType()) ? cfg("gl.biz.ar.other", "6051") : cfg("gl.biz.ar.revenue", "6001"),
                "销售发票收入科目");

        List<GlVoucherEntry> entries = new ArrayList<>();
        entries.add(entry(cfg("gl.biz.ar.control", "1122"), total, null,
                "应收 " + StrUtil.nullToEmpty(inv.getCustomerName()) + " 货款"));
        entries.add(entry(revenue, null, amount, "销售收入 " + StrUtil.nullToEmpty(inv.getBillNo())));
        if (tax.signum() > 0) {
            entries.add(entry(cfg("gl.biz.tax.output", "22210105"), null, tax, "销项税额 " + StrUtil.nullToEmpty(inv.getBillNo())));
        }
        GlVoucher v = build("销售发票 " + inv.getBillNo(), "AR_INVOICE", inv.getBillNo(),
                invoiceId, inv.getInvoiceDate(), entries);
        voucherService.saveWithEntries(v);
        inv.setVoucherId(v.getId());
        inv.setVoucherNo(v.getVoucherNo());
        arInvoiceMapper.updateById(inv);
        return v.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long fromArReceipt(Long receiptId) {
        ArReceipt r = arReceiptMapper.selectById(receiptId);
        if (r == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        requireAudited(r.getStatus(), "收款单");
        if (r.getVoucherId() != null) {
            throw new BusinessException("该收款单已生成凭证 " + StrUtil.nullToEmpty(r.getVoucherNo()) + "，请勿重复生成");
        }
        BigDecimal amount = nz(r.getAmount());
        if (amount.signum() <= 0) throw new BusinessException("收款金额必须大于 0");
        String cash = cashSubject(r.getSubjectCode(), r.getAccountId());

        List<GlVoucherEntry> entries = new ArrayList<>();
        entries.add(entry(cash, amount, null, "收到 " + StrUtil.nullToEmpty(r.getCustomerName()) + " 货款"));
        entries.add(entry(cfg("gl.biz.ar.control", "1122"), null, amount,
                "冲减应收 " + StrUtil.nullToEmpty(r.getBillNo())));
        GlVoucher v = build("收款单 " + r.getBillNo(), "AR_RECEIPT", r.getBillNo(),
                receiptId, r.getReceiptDate(), entries);
        voucherService.saveWithEntries(v);
        r.setVoucherId(v.getId());
        r.setVoucherNo(v.getVoucherNo());
        arReceiptMapper.updateById(r);
        return v.getId();
    }

    // ==================== 应付 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long fromApInvoice(Long invoiceId) {
        ApInvoice inv = apInvoiceMapper.selectById(invoiceId);
        if (inv == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        requireAudited(inv.getStatus(), "采购发票");
        if (inv.getVoucherId() != null) {
            throw new BusinessException("该发票已生成凭证 " + StrUtil.nullToEmpty(inv.getVoucherNo()) + "，请勿重复生成");
        }
        BigDecimal amount = nz(inv.getAmount());
        BigDecimal tax = nz(inv.getTaxAmount());
        BigDecimal total = nz(inv.getTotalAmount());
        if (total.signum() <= 0) throw new BusinessException("发票价税合计必须大于 0");
        if (total.subtract(amount.add(tax)).abs().compareTo(CENT) > 0) {
            throw new BusinessException("发票金额与税额之和不等于价税合计，请先修正单据");
        }
        String cost = pickSubject(inv.getSubjectCode(),
                "1".equals(inv.getInvoiceType()) ? cfg("gl.biz.ap.purchase", "1405") : cfg("gl.biz.ap.expense", "6602"),
                "采购发票成本费用科目");

        List<GlVoucherEntry> entries = new ArrayList<>();
        entries.add(entry(cost, amount, null, "采购/费用 " + StrUtil.nullToEmpty(inv.getBillNo())));
        if (tax.signum() > 0) {
            entries.add(entry(cfg("gl.biz.tax.input", "22210101"), tax, null, "进项税额 " + StrUtil.nullToEmpty(inv.getBillNo())));
        }
        entries.add(entry(cfg("gl.biz.ap.control", "2202"), null, total,
                "应付 " + StrUtil.nullToEmpty(inv.getSupplierName()) + " 款项"));
        GlVoucher v = build("采购发票 " + inv.getBillNo(), "AP_INVOICE", inv.getBillNo(),
                invoiceId, inv.getInvoiceDate(), entries);
        voucherService.saveWithEntries(v);
        inv.setVoucherId(v.getId());
        inv.setVoucherNo(v.getVoucherNo());
        apInvoiceMapper.updateById(inv);
        return v.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long fromApPayment(Long paymentId) {
        ApPayment p = apPaymentMapper.selectById(paymentId);
        if (p == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        requireAudited(p.getStatus(), "付款单");
        if (p.getVoucherId() != null) {
            throw new BusinessException("该付款单已生成凭证，请勿重复生成");
        }
        BigDecimal amount = nz(p.getAmount());
        if (amount.signum() <= 0) throw new BusinessException("付款金额必须大于 0");
        String cash = cashSubject(null, p.getAccountId());

        List<GlVoucherEntry> entries = new ArrayList<>();
        entries.add(entry(cfg("gl.biz.ap.control", "2202"), amount, null,
                "支付 " + StrUtil.nullToEmpty(p.getSupplierName()) + " 货款"));
        entries.add(entry(cash, null, amount, "付款 " + StrUtil.nullToEmpty(p.getBillNo())));
        GlVoucher v = build("付款单 " + p.getBillNo(), "AP_PAYMENT", p.getBillNo(),
                paymentId, p.getPaymentDate(), entries);
        voucherService.saveWithEntries(v);
        p.setVoucherId(v.getId());
        p.setVoucherNo(v.getVoucherNo());
        apPaymentMapper.updateById(p);
        return v.getId();
    }

    // ==================== 费用报销 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long fromExpense(Long applyId) {
        ExpenseApply a = expenseApplyMapper.selectById(applyId);
        if (a == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        // 0-待审 1-通过 2-驳回 3-已付款：仅审批通过或已付款可入账
        if (!"1".equals(a.getStatus()) && !"3".equals(a.getStatus())) {
            throw new BusinessException("费用报销单未审批通过，不能生成凭证");
        }
        if (a.getVoucherId() != null) {
            throw new BusinessException("该报销单已生成凭证 " + StrUtil.nullToEmpty(a.getVoucherNo()) + "，请勿重复生成");
        }
        BigDecimal amount = nz(a.getAmount());
        if (amount.signum() <= 0) throw new BusinessException("报销金额必须大于 0");
        String expense = pickSubject(a.getSubjectCode(), cfg("gl.biz.expense", "6602"), "报销费用科目");
        // 已付款（status=3）直接贷记货币资金；仅审批通过（status=1）尚未付款时贷记其他应付款，避免虚减银行存款
        boolean paid = "3".equals(a.getStatus());
        String creditSubject = paid ? cashSubject(null, null) : cfg("gl.biz.expense.payable", "2241");

        List<GlVoucherEntry> entries = new ArrayList<>();
        entries.add(entry(expense, amount, null,
                StrUtil.blankToDefault(a.getReason(), "费用报销") + " " + StrUtil.nullToEmpty(a.getApplicantName())));
        entries.add(entry(creditSubject, null, amount,
                (paid ? "报销付款 " : "计提应付报销款 ") + StrUtil.nullToEmpty(a.getBillNo())));
        GlVoucher v = build("费用报销 " + a.getBillNo(), "EXPENSE", a.getBillNo(),
                applyId, a.getApplyDate(), entries);
        voucherService.saveWithEntries(v);
        a.setVoucherId(v.getId());
        a.setVoucherNo(v.getVoucherNo());
        expenseApplyMapper.updateById(a);
        return v.getId();
    }

    // ==================== 公共构造 ====================

    private GlVoucher build(String summary, String source, String sourceBiz, Long sourceId,
                            LocalDate date, List<GlVoucherEntry> entries) {
        LocalDate d = date == null ? LocalDate.now() : date;
        GlVoucher v = new GlVoucher();
        v.setVoucherType("记");
        v.setVoucherDate(d);
        v.setPeriodCode(d.format(PERIOD_FMT));
        v.setSummary(summary);
        v.setSource(source);
        v.setSourceBiz(sourceBiz);
        v.setSourceId(sourceId);
        v.setAttachCount(0);
        v.setStatus("DRAFT");
        v.setEntries(entries);
        checkPeriodOpen(v.getPeriodCode());
        return v;
    }

    private GlVoucherEntry entry(String subjectCode, BigDecimal debit, BigDecimal credit, String summary) {
        GlVoucherEntry e = new GlVoucherEntry();
        e.setSubjectCode(subjectCode);
        e.setSummary(summary);
        e.setDebitAmount(debit == null ? BigDecimal.ZERO : debit);
        e.setCreditAmount(credit == null ? BigDecimal.ZERO : credit);
        e.setCurrencyCode("CNY");
        e.setExchangeRate(BigDecimal.ONE);
        e.setOriginalAmount(debit != null ? debit : credit);
        return e;
    }

    private void checkPeriodOpen(String periodCode) {
        GlPeriod p = periodMapper.selectOne(new LambdaQueryWrapper<GlPeriod>()
                .eq(GlPeriod::getPeriodCode, periodCode)
                .last("LIMIT 1"));
        if (p == null) {
            throw new BusinessException("会计期间 " + periodCode + " 未初始化，请先在【总账-期间管理】中创建");
        }
        if ("CLOSED".equals(p.getStatus())) {
            throw new BusinessException(ResultCode.PERIOD_CLOSED);
        }
    }

    /** 单据上的科目若为可用的明细科目则优先采用，否则回落到参数默认值 */
    private String pickSubject(String docSubject, String fallback, String label) {
        if (StrUtil.isNotBlank(docSubject) && existsLeaf(docSubject) && !isControl(docSubject)) {
            return docSubject;
        }
        if (!existsLeaf(fallback)) {
            throw new BusinessException(label + "未配置或不是明细科目：" + fallback + "，请在系统参数中调整");
        }
        return fallback;
    }

    /** 往来科目（1122/2202 等）不能作为收入或费用科目使用 */
    private boolean isControl(String code) {
        return code.startsWith("1122") || code.startsWith("2202")
                || code.equals(cfg("gl.biz.ar.control", "1122"))
                || code.equals(cfg("gl.biz.ap.control", "2202"));
    }

    /** 收付款账户对应的货币资金科目：优先账户设置，其次单据科目，最后按账户类型回落 */
    private String cashSubject(String docSubject, Long accountId) {
        if (accountId != null) {
            try {
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                        "SELECT subject_code, account_type FROM cash_account WHERE id = ? AND deleted = 0", accountId);
                if (!rows.isEmpty()) {
                    Object sc = rows.get(0).get("subject_code");
                    if (sc != null && StrUtil.isNotBlank(sc.toString()) && existsLeaf(sc.toString())) {
                        return sc.toString();
                    }
                    Object t = rows.get(0).get("account_type");
                    if (t != null && "CASH".equalsIgnoreCase(t.toString())) {
                        return cfg("gl.biz.cash", "1001");
                    }
                }
            } catch (Exception e) {
                log.warn("查询现金账户 {} 失败，回落默认科目: {}", accountId, e.getMessage());
            }
        }
        if (StrUtil.isNotBlank(docSubject) && existsLeaf(docSubject) && docSubject.startsWith("10")) {
            return docSubject;
        }
        String bank = cfg("gl.biz.bank", "1002");
        if (!existsLeaf(bank)) {
            throw new BusinessException("银行存款科目未配置或不是明细科目：" + bank);
        }
        return bank;
    }

    private boolean existsLeaf(String code) {
        if (StrUtil.isBlank(code)) return false;
        try {
            Integer n = jdbcTemplate.queryForObject(
                    "SELECT COUNT(1) FROM gl_account_subject WHERE subject_code = ? AND is_leaf = 1 AND deleted = 0",
                    Integer.class, code);
            return n != null && n > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private String cfg(String key, String def) {
        try {
            SysConfig c = configMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
                    .eq(SysConfig::getConfigKey, key)
                    .eq(SysConfig::getDeleted, 0)
                    .last("LIMIT 1"));
            if (c != null && StrUtil.isNotBlank(c.getConfigValue())) {
                return c.getConfigValue().trim();
            }
        } catch (Exception e) {
            log.warn("读取系统参数 {} 失败，使用默认值 {}", key, def);
        }
        return def;
    }

    private void requireAudited(String status, String docName) {
        if (!"A".equals(status)) {
            throw new BusinessException(docName + "未审核，不能生成凭证");
        }
    }

    private BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
