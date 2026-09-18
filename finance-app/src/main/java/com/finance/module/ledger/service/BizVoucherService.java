package com.finance.module.ledger.service;

/**
 * 业财一体：业务单据生成记账凭证
 * <p>所有方法在单据已审核且尚未生成凭证时创建 DRAFT 凭证，并把凭证号回写到业务单据，
 * 凭证仍需按正常流程审核、过账后才会影响总账余额。</p>
 */
public interface BizVoucherService {

    /** 销售发票 -> 借 应收账款 / 贷 收入 + 销项税额，返回凭证ID */
    Long fromArInvoice(Long invoiceId);

    /** 收款单 -> 借 现金银行 / 贷 应收账款，返回凭证ID */
    Long fromArReceipt(Long receiptId);

    /** 采购发票 -> 借 成本费用 + 进项税额 / 贷 应付账款，返回凭证ID */
    Long fromApInvoice(Long invoiceId);

    /** 付款单 -> 借 应付账款 / 贷 现金银行，返回凭证ID */
    Long fromApPayment(Long paymentId);

    /** 费用报销单 -> 借 费用科目 / 贷 现金银行，返回凭证ID */
    Long fromExpense(Long applyId);
}
