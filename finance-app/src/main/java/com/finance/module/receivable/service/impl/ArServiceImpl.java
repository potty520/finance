package com.finance.module.receivable.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import com.finance.common.service.CurrentUserResolver;
import com.finance.module.receivable.entity.ArInvoice;
import com.finance.module.receivable.entity.ArReceipt;
import com.finance.module.receivable.entity.ArWriteoff;
import com.finance.module.receivable.mapper.ArInvoiceMapper;
import com.finance.module.receivable.mapper.ArReceiptMapper;
import com.finance.module.receivable.mapper.ArWriteoffMapper;
import com.finance.module.receivable.service.IArService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArServiceImpl implements IArService {

    @Resource private ArInvoiceMapper invoiceMapper;
    @Resource private ArReceiptMapper receiptMapper;
    @Resource private ArWriteoffMapper writeoffMapper;
    @Resource private CurrentUserResolver currentUser;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveInvoice(ArInvoice invoice) {
        if (StrUtil.isBlank(invoice.getBillNo())) {
            invoice.setBillNo(generateBillNo("AR-IV"));
        }
        if (invoice.getStatus() == null) invoice.setStatus("D");
        if (invoice.getUncollectedAmount() == null) {
            invoice.setUncollectedAmount(invoice.getTotalAmount());
        }
        return invoiceMapper.insert(invoice) > 0;
    }

    @Override
    public ArInvoice getInvoice(Long id) {
        return invoiceMapper.selectById(id);
    }

    @Override
    public boolean auditInvoice(Long id) {
        ArInvoice i = invoiceMapper.selectById(id);
        if (i == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"D".equals(i.getStatus())) throw new BusinessException("仅草稿可审核");
        i.setStatus("A");
        return invoiceMapper.updateById(i) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveReceipt(ArReceipt receipt) {
        if (StrUtil.isBlank(receipt.getBillNo())) {
            receipt.setBillNo(generateBillNo("AR-RC"));
        }
        if (receipt.getStatus() == null) receipt.setStatus("D");
        if (receipt.getUnappliedAmount() == null) {
            receipt.setUnappliedAmount(receipt.getAmount());
        }
        return receiptMapper.insert(receipt) > 0;
    }

    @Override
    public boolean auditReceipt(Long id) {
        ArReceipt r = receiptMapper.selectById(id);
        if (r == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"D".equals(r.getStatus())) throw new BusinessException("仅草稿可审核");
        r.setStatus("A");
        return receiptMapper.updateById(r) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean writeoff(Long receiptId, Long invoiceId, BigDecimal amount, String remark) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("核销金额必须大于 0");
        }
        ArReceipt r = receiptMapper.selectById(receiptId);
        ArInvoice i = invoiceMapper.selectById(invoiceId);
        if (r == null || i == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"A".equals(r.getStatus()) && !"C".equals(r.getStatus())) {
            throw new BusinessException("收款单未审核，不能核销");
        }
        if (!"A".equals(i.getStatus())) {
            throw new BusinessException("发票未审核，不能核销");
        }
        if (r.getUnappliedAmount() == null || r.getUnappliedAmount().compareTo(amount) < 0) {
            throw new BusinessException("收款单可用余额不足");
        }
        if (i.getUncollectedAmount() == null || i.getUncollectedAmount().compareTo(amount) < 0) {
            throw new BusinessException("发票未收金额不足");
        }
        // 创建核销记录
        ArWriteoff w = new ArWriteoff();
        w.setReceiptId(receiptId);
        w.setReceiptNo(r.getBillNo());
        w.setInvoiceId(invoiceId);
        w.setInvoiceNo(i.getBillNo());
        w.setWriteoffAmount(amount);
        w.setRemark(remark);
        w.setOperator(currentUser.currentId());
        w.setOperatorName(currentUser.currentName());
        w.setCreateTime(LocalDateTime.now());
        writeoffMapper.insert(w);
        // 更新余额
        r.setAppliedAmount((r.getAppliedAmount() == null ? BigDecimal.ZERO : r.getAppliedAmount()).add(amount));
        r.setUnappliedAmount(r.getUnappliedAmount().subtract(amount));
        receiptMapper.updateById(r);
        i.setCollectedAmount((i.getCollectedAmount() == null ? BigDecimal.ZERO : i.getCollectedAmount()).add(amount));
        i.setUncollectedAmount(i.getUncollectedAmount().subtract(amount));
        if (i.getUncollectedAmount().compareTo(BigDecimal.ZERO) == 0) {
            i.setStatus("C");
        }
        invoiceMapper.updateById(i);
        return true;
    }

    @Override
    public BigDecimal getCustomerUncollected(Long customerId) {
        return invoiceMapper.sumUncollected(customerId);
    }

    @Override
    public BigDecimal getCustomerUnapplied(Long customerId) {
        return receiptMapper.sumUnapplied(customerId);
    }

    @Override
    public List<Map<String, Object>> agingAnalysis(Long customerId) {
        return invoiceMapper.agingAnalysis(customerId);
    }

    private String generateBillNo(String prefix) {
        // 时间戳 + 随机后缀，避免同毫秒并发撞号
        return prefix + "-" + System.currentTimeMillis() + "-"
                + java.util.concurrent.ThreadLocalRandom.current().nextInt(1000, 9999);
    }
}
