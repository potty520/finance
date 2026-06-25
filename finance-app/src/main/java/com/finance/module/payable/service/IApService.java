package com.finance.module.payable.service;

import com.finance.module.payable.entity.ApInvoice;
import com.finance.module.payable.entity.ApPayment;
import com.finance.module.payable.entity.ApWriteoff;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IApService {
    boolean saveInvoice(ApInvoice invoice);
    boolean auditInvoice(Long id);
    boolean savePayment(ApPayment payment);
    boolean auditPayment(Long id);
    boolean writeoff(Long paymentId, Long invoiceId, BigDecimal amount, String remark);
    List<ApWriteoff> writeoffsByPayment(Long paymentId);
    List<ApWriteoff> writeoffsByInvoice(Long invoiceId);
    BigDecimal getSupplierUnpaid(Long supplierId);
    BigDecimal getSupplierUnapplied(Long supplierId);
    List<Map<String, Object>> agingAnalysis(Long supplierId);
}
