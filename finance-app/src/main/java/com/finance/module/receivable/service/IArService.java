package com.finance.module.receivable.service;

import com.finance.module.receivable.entity.ArInvoice;
import com.finance.module.receivable.entity.ArReceipt;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IArService {
    // 发票
    boolean saveInvoice(ArInvoice invoice);
    ArInvoice getInvoice(Long id);
    boolean auditInvoice(Long id);

    // 收款
    boolean saveReceipt(ArReceipt receipt);
    boolean auditReceipt(Long id);

    // 核销
    boolean writeoff(Long receiptId, Long invoiceId, BigDecimal amount, String remark);
    BigDecimal getCustomerUncollected(Long customerId);
    BigDecimal getCustomerUnapplied(Long customerId);

    // 账龄分析
    List<Map<String, Object>> agingAnalysis(Long customerId);
}
