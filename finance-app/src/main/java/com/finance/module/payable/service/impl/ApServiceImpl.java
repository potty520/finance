package com.finance.module.payable.service.impl;

import cn.hutool.core.util.StrUtil;
import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import com.finance.module.payable.entity.ApInvoice;
import com.finance.module.payable.entity.ApPayment;
import com.finance.module.payable.entity.ApWriteoff;
import com.finance.module.payable.mapper.ApInvoiceMapper;
import com.finance.module.payable.mapper.ApPaymentMapper;
import com.finance.module.payable.mapper.ApWriteoffMapper;
import com.finance.module.payable.service.IApService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ApServiceImpl implements IApService {

    @Resource private ApInvoiceMapper invoiceMapper;
    @Resource private ApPaymentMapper paymentMapper;
    @Resource private ApWriteoffMapper writeoffMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveInvoice(ApInvoice invoice) {
        if (StrUtil.isBlank(invoice.getBillNo())) {
            invoice.setBillNo(generateBillNo("AP-IV"));
        }
        if (invoice.getStatus() == null) invoice.setStatus("D");
        if (invoice.getUnpaidAmount() == null) {
            invoice.setUnpaidAmount(invoice.getTotalAmount());
        }
        return invoiceMapper.insert(invoice) > 0;
    }

    @Override
    public boolean auditInvoice(Long id) {
        ApInvoice i = invoiceMapper.selectById(id);
        if (i == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"D".equals(i.getStatus())) throw new BusinessException("仅草稿可审核");
        i.setStatus("A");
        return invoiceMapper.updateById(i) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePayment(ApPayment payment) {
        if (StrUtil.isBlank(payment.getBillNo())) {
            payment.setBillNo(generateBillNo("AP-PM"));
        }
        if (payment.getStatus() == null) payment.setStatus("DRAFT");
        if (payment.getAppliedAmount() == null) payment.setAppliedAmount(BigDecimal.ZERO);
        if (payment.getDeleted() == null) payment.setDeleted(0);
        if (payment.getPaymentType() == null) payment.setPaymentType("BANK");
        return paymentMapper.insert(payment) > 0;
    }

    @Override
    public boolean auditPayment(Long id) {
        ApPayment p = paymentMapper.selectById(id);
        if (p == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"D".equals(p.getStatus())) throw new BusinessException("仅草稿可审核");
        p.setStatus("A");
        return paymentMapper.updateById(p) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean writeoff(Long paymentId, Long invoiceId, BigDecimal amount, String remark) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("核销金额必须大于 0");
        }
        ApPayment p = paymentMapper.selectById(paymentId);
        ApInvoice i = invoiceMapper.selectById(invoiceId);
        if (p == null || i == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        BigDecimal unapplied = unappliedAmount(p);
        if (unapplied.compareTo(amount) < 0) {
            throw new BusinessException("付款单可用余额不足");
        }
        if (i.getUnpaidAmount() == null || i.getUnpaidAmount().compareTo(amount) < 0) {
            throw new BusinessException("发票未付金额不足");
        }
        ApWriteoff w = new ApWriteoff();
        w.setPaymentId(paymentId);
        w.setPaymentNo(p.getBillNo());
        w.setInvoiceId(invoiceId);
        w.setInvoiceNo(i.getBillNo());
        w.setWriteoffAmount(amount);
        w.setRemark(remark);
        w.setOperator(1L);
        w.setOperatorName("系统用户");
        w.setCreateTime(LocalDateTime.now());
        writeoffMapper.insert(w);
        p.setAppliedAmount((p.getAppliedAmount() == null ? BigDecimal.ZERO : p.getAppliedAmount()).add(amount));
        paymentMapper.updateById(p);
        i.setPaidAmount((i.getPaidAmount() == null ? BigDecimal.ZERO : i.getPaidAmount()).add(amount));
        i.setUnpaidAmount(i.getUnpaidAmount().subtract(amount));
        if (i.getUnpaidAmount().compareTo(BigDecimal.ZERO) == 0) {
            i.setStatus("C");
        }
        invoiceMapper.updateById(i);
        return true;
    }

    @Override
    public List<ApWriteoff> writeoffsByPayment(Long paymentId) {
        return writeoffMapper.selectByPayment(paymentId);
    }

    @Override
    public List<ApWriteoff> writeoffsByInvoice(Long invoiceId) {
        return writeoffMapper.selectByInvoice(invoiceId);
    }

    @Override
    public BigDecimal getSupplierUnpaid(Long supplierId) {
        return invoiceMapper.sumUnpaid(supplierId);
    }

    @Override
    public BigDecimal getSupplierUnapplied(Long supplierId) {
        return paymentMapper.sumUnapplied(supplierId);
    }

    @Override
    public List<Map<String, Object>> agingAnalysis(Long supplierId) {
        return invoiceMapper.agingAnalysis(supplierId);
    }

    private String generateBillNo(String prefix) {
        return prefix + "-" + System.currentTimeMillis();
    }

    private BigDecimal unappliedAmount(ApPayment payment) {
        BigDecimal amount = payment.getAmount() == null ? BigDecimal.ZERO : payment.getAmount();
        BigDecimal applied = payment.getAppliedAmount() == null ? BigDecimal.ZERO : payment.getAppliedAmount();
        return amount.subtract(applied);
    }
}
