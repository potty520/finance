package com.finance.module.ledger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.module.ledger.entity.GlBalance;
import com.finance.module.ledger.entity.GlVoucher;
import com.finance.module.ledger.entity.GlVoucherEntry;
import com.finance.module.ledger.mapper.GlBalanceMapper;
import com.finance.module.ledger.mapper.GlVoucherEntryMapper;
import com.finance.module.ledger.mapper.GlVoucherMapper;
import com.finance.module.ledger.service.GlBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 总账余额引擎
 *
 * gl_balance 每行 = 某科目某期间的 [期初借/贷, 本期借/贷发生, 本年累计借/贷, 期末借/贷]。
 * 凭证过账(sign=+1)/反过账(sign=-1)时按分录聚合更新对应行，并沿时间链刷新该科目
 * 后续期间的期初与本年累计，保证任意时点报表口径一致。
 */
@Slf4j
@Service
public class GlBalanceServiceImpl implements GlBalanceService {

    @Resource private GlBalanceMapper balanceMapper;
    @Resource private GlVoucherMapper voucherMapper;
    @Resource private GlVoucherEntryMapper entryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyVoucher(GlVoucher voucher, List<GlVoucherEntry> entries, int sign) {
        if (entries == null || entries.isEmpty()) return;
        String periodCode = periodCodeOf(voucher);
        if (periodCode == null) {
            log.warn("凭证 {} 无法确定会计期间，跳过余额更新", voucher.getId());
            return;
        }
        // 按科目聚合本张凭证的借贷发生额
        Map<String, BigDecimal[]> delta = new HashMap<>();
        for (GlVoucherEntry e : entries) {
            if (e.getSubjectCode() == null) continue;
            BigDecimal d = debitOf(e), c = creditOf(e);
            if (d.signum() == 0 && c.signum() == 0) continue;
            BigDecimal[] cur = delta.computeIfAbsent(e.getSubjectCode(),
                    k -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            cur[0] = cur[0].add(d);
            cur[1] = cur[1].add(c);
        }
        for (Map.Entry<String, BigDecimal[]> en : delta.entrySet()) {
            GlBalance row = getOrInit(periodCode, en.getKey());
            row.setPeriodDebit(nz(row.getPeriodDebit()).add(en.getValue()[0].multiply(BigDecimal.valueOf(sign))));
            row.setPeriodCredit(nz(row.getPeriodCredit()).add(en.getValue()[1].multiply(BigDecimal.valueOf(sign))));
            balanceMapper.updateById(row);
            refreshChain(en.getKey(), periodCode);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int carryForward(String periodCode) {
        String next = nextPeriod(periodCode);
        List<GlBalance> rows = balanceMapper.selectList(
                new LambdaQueryWrapper<GlBalance>().eq(GlBalance::getPeriodCode, periodCode));
        int n = 0;
        for (GlBalance r : rows) {
            // 期末无余额且本期无发生额的科目不结转
            if (isZero(r.getEndingDebit()) && isZero(r.getEndingCredit())
                    && isZero(r.getPeriodDebit()) && isZero(r.getPeriodCredit())) {
                continue;
            }
            getOrInit(next, r.getSubjectCode());
            n++;
        }
        if (n > 0) {
            // 重新初始化过的新行需要整链刷新（期初=上期期末）
            Set<String> subjects = new LinkedHashSet<>();
            rows.forEach(r -> subjects.add(r.getSubjectCode()));
            for (String s : subjects) refreshChain(s, next);
        }
        return n;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int rebuildAll() {
        // 清空重算：以全部已过账凭证为准
        List<GlBalance> all = balanceMapper.selectList(null);
        for (GlBalance b : all) balanceMapper.deleteById(b.getId());
        List<GlVoucher> posted = voucherMapper.selectList(new LambdaQueryWrapper<GlVoucher>()
                .in(GlVoucher::getStatus, "POSTED", "P")
                .orderByAsc(GlVoucher::getPeriodCode, GlVoucher::getId));
        int n = 0;
        for (GlVoucher v : posted) {
            List<GlVoucherEntry> entries = entryMapper.selectByVoucherId(v.getId());
            if (entries != null) entries.forEach(GlVoucherEntry::afterLoad);
            applyVoucher(v, entries, 1);
            n++;
        }
        log.info("gl_balance 重建完成，重放过账凭证 {} 张", n);
        return n;
    }

    /** 取（或创建）科目在某期间的余额行，期初继承自最近的前一期行 */
    private GlBalance getOrInit(String periodCode, String subjectCode) {
        GlBalance row = balanceMapper.selectOne(new LambdaQueryWrapper<GlBalance>()
                .eq(GlBalance::getPeriodCode, periodCode)
                .eq(GlBalance::getSubjectCode, subjectCode)
                .last("LIMIT 1"));
        if (row != null) return row;
        GlBalance prev = balanceMapper.selectOne(new LambdaQueryWrapper<GlBalance>()
                .eq(GlBalance::getSubjectCode, subjectCode)
                .lt(GlBalance::getPeriodCode, periodCode)
                .orderByDesc(GlBalance::getPeriodCode)
                .last("LIMIT 1"));
        row = new GlBalance();
        row.setPeriodCode(periodCode);
        row.setSubjectCode(subjectCode);
        row.setCurrencyCode("CNY");
        BigDecimal od = prev == null ? BigDecimal.ZERO : nz(prev.getEndingDebit());
        BigDecimal oc = prev == null ? BigDecimal.ZERO : nz(prev.getEndingCredit());
        row.setOpeningDebit(od);
        row.setOpeningCredit(oc);
        row.setPeriodDebit(BigDecimal.ZERO);
        row.setPeriodCredit(BigDecimal.ZERO);
        recomputeEnding(row);
        recomputeYear(row, prev);
        balanceMapper.insert(row);
        return row;
    }

    /** 沿时间链刷新某科目从 fromPeriod 起所有行的期初/期末/本年累计 */
    private void refreshChain(String subjectCode, String fromPeriod) {
        List<GlBalance> rows = balanceMapper.selectList(new LambdaQueryWrapper<GlBalance>()
                .eq(GlBalance::getSubjectCode, subjectCode)
                .orderByAsc(GlBalance::getPeriodCode));
        GlBalance prev = null;
        for (GlBalance r : rows) {
            if (prev == null) {
                // 链条起点：保留已有期初（可能来自初始化数据）
                recomputeEnding(r);
                r.setYearDebit(nz(r.getPeriodDebit()));
                r.setYearCredit(nz(r.getPeriodCredit()));
            } else {
                boolean sameYear = r.getPeriodCode().substring(0, 4).equals(prev.getPeriodCode().substring(0, 4));
                r.setOpeningDebit(nz(prev.getEndingDebit()));
                r.setOpeningCredit(nz(prev.getEndingCredit()));
                recomputeEnding(r);
                if (sameYear) {
                    r.setYearDebit(nz(prev.getYearDebit()).add(nz(r.getPeriodDebit())));
                    r.setYearCredit(nz(nz(prev.getYearCredit()).add(nz(r.getPeriodCredit()))));
                } else {
                    r.setYearDebit(nz(r.getPeriodDebit()));
                    r.setYearCredit(nz(r.getPeriodCredit()));
                }
            }
            if (r.getPeriodCode().compareTo(fromPeriod) >= 0) {
                balanceMapper.updateById(r);
            }
            prev = r;
        }
    }

    private void recomputeEnding(GlBalance r) {
        BigDecimal net = nz(r.getOpeningDebit()).subtract(nz(r.getOpeningCredit()))
                .add(nz(r.getPeriodDebit())).subtract(nz(r.getPeriodCredit()));
        if (net.signum() >= 0) {
            r.setEndingDebit(net);
            r.setEndingCredit(BigDecimal.ZERO);
        } else {
            r.setEndingDebit(BigDecimal.ZERO);
            r.setEndingCredit(net.negate());
        }
    }

    private void recomputeYear(GlBalance row, GlBalance prev) {
        boolean sameYear = prev != null
                && row.getPeriodCode().substring(0, 4).equals(prev.getPeriodCode().substring(0, 4));
        row.setYearDebit(sameYear ? nz(prev.getYearDebit()).add(nz(row.getPeriodDebit())) : nz(row.getPeriodDebit()));
        row.setYearCredit(sameYear ? nz(prev.getYearCredit()).add(nz(row.getPeriodCredit())) : nz(row.getPeriodCredit()));
    }

    private String periodCodeOf(GlVoucher v) {
        if (v.getPeriodCode() != null && v.getPeriodCode().length() >= 6) return v.getPeriodCode();
        if (v.getFiscalYear() != null && v.getFiscalPeriod() != null) {
            return v.getFiscalYear() + String.format("%02d", v.getFiscalPeriod());
        }
        if (v.getVoucherDate() != null) {
            return v.getVoucherDate().format(DateTimeFormatter.ofPattern("yyyyMM"));
        }
        return null;
    }

    private String nextPeriod(String periodCode) {
        int year = Integer.parseInt(periodCode.substring(0, 4));
        int p = Integer.parseInt(periodCode.substring(4, 6));
        if (p >= 12) return (year + 1) + "01";
        return year + String.format("%02d", p + 1);
    }

    private BigDecimal debitOf(GlVoucherEntry e) {
        if (e.getDebitAmount() != null) return e.getDebitAmount();
        if ("DEBIT".equalsIgnoreCase(e.getDcDirection())) return nz(e.getAmount());
        return BigDecimal.ZERO;
    }

    private BigDecimal creditOf(GlVoucherEntry e) {
        if (e.getCreditAmount() != null) return e.getCreditAmount();
        if ("CREDIT".equalsIgnoreCase(e.getDcDirection())) return nz(e.getAmount());
        return BigDecimal.ZERO;
    }

    private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }

    private static boolean isZero(BigDecimal v) { return v == null || v.signum() == 0; }
}
