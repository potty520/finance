package com.finance.module.ledger.service;

import com.finance.module.ledger.entity.GlVoucher;
import com.finance.module.ledger.entity.GlVoucherEntry;

import java.util.List;

/**
 * 总账余额引擎：凭证过账/反过账时维护 gl_balance，期末结转生成下期期初。
 */
public interface GlBalanceService {

    /**
     * 应用（sign=+1）或回冲（sign=-1）凭证分录到科目余额
     */
    void applyVoucher(GlVoucher voucher, List<GlVoucherEntry> entries, int sign);

    /**
     * 期末结转：将 periodCode 各科目期末余额结转为下期期初
     */
    int carryForward(String periodCode);

    /**
     * 按已过账凭证重算某年（或全部）余额，用于数据修复
     */
    int rebuildAll();
}
