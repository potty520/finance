package com.finance.module.receivable.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.receivable.entity.ArCustomer;
import com.finance.module.receivable.entity.ArInvoice;
import com.finance.module.receivable.entity.ArReceipt;
import com.finance.module.receivable.entity.ArWriteoff;
import com.finance.module.receivable.mapper.ArCustomerMapper;
import com.finance.module.receivable.mapper.ArInvoiceMapper;
import com.finance.module.receivable.mapper.ArReceiptMapper;
import com.finance.module.receivable.mapper.ArWriteoffMapper;
import com.finance.module.receivable.service.IArService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/receivable")
public class ReceivableController {

    @Resource private IArService arService;
    @Resource private ArCustomerMapper customerMapper;
    @Resource private ArInvoiceMapper invoiceMapper;
    @Resource private ArReceiptMapper receiptMapper;
    @Resource private ArWriteoffMapper writeoffMapper;

    // 客户
    @GetMapping("/customer/page")
    public Result<PageResult<ArCustomer>> customerPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<ArCustomer> qw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            qw.and(w -> w.like(ArCustomer::getCustomerCode, keyword)
                    .or().like(ArCustomer::getCustomerName, keyword));
        }
        qw.eq(ArCustomer::getStatus, 1).orderByAsc(ArCustomer::getCustomerCode);
        Page<ArCustomer> p = customerMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/customer/list")
    public Result<List<ArCustomer>> customerList() {
        return Result.success(customerMapper.selectList(
                new LambdaQueryWrapper<ArCustomer>()
                        .eq(ArCustomer::getStatus, 1)
                        .orderByAsc(ArCustomer::getCustomerCode)));
    }

    // 发票
    @GetMapping("/invoice/page")
    public Result<PageResult<ArInvoice>> invoicePage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String billNo,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String status) {
        LambdaQueryWrapper<ArInvoice> qw = new LambdaQueryWrapper<>();
        if (billNo != null && !billNo.isEmpty()) qw.like(ArInvoice::getBillNo, billNo);
        if (customerId != null) qw.eq(ArInvoice::getCustomerId, customerId);
        if (status != null && !status.isEmpty()) qw.eq(ArInvoice::getStatus, status);
        qw.orderByDesc(ArInvoice::getInvoiceDate);
        Page<ArInvoice> p = invoiceMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/invoice/{id}")
    public Result<ArInvoice> getInvoice(@PathVariable Long id) {
        return Result.success(arService.getInvoice(id));
    }

    @PostMapping("/invoice")
    public Result<Boolean> addInvoice(@RequestBody ArInvoice invoice) {
        return Result.success(arService.saveInvoice(invoice));
    }

    @PutMapping("/invoice")
    public Result<Boolean> editInvoice(@RequestBody ArInvoice invoice) {
        return Result.success(invoiceMapper.updateById(invoice) > 0);
    }

    @DeleteMapping("/invoice/{id}")
    public Result<Boolean> delInvoice(@PathVariable Long id) {
        ArInvoice i = invoiceMapper.selectById(id);
        if (i != null && i.getCollectedAmount() != null && i.getCollectedAmount().compareTo(BigDecimal.ZERO) > 0) {
            return Result.error("已收款的发票不可删除");
        }
        return Result.success(invoiceMapper.deleteById(id) > 0);
    }

    @PostMapping("/invoice/audit/{id}")
    public Result<Boolean> auditInvoice(@PathVariable Long id) {
        return Result.success(arService.auditInvoice(id));
    }

    // 收款
    @GetMapping("/receipt/page")
    public Result<PageResult<ArReceipt>> receiptPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String billNo,
            @RequestParam(required = false) Long customerId) {
        LambdaQueryWrapper<ArReceipt> qw = new LambdaQueryWrapper<>();
        if (billNo != null && !billNo.isEmpty()) qw.like(ArReceipt::getBillNo, billNo);
        if (customerId != null) qw.eq(ArReceipt::getCustomerId, customerId);
        qw.orderByDesc(ArReceipt::getReceiptDate);
        Page<ArReceipt> p = receiptMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @PostMapping("/receipt")
    public Result<Boolean> addReceipt(@RequestBody ArReceipt receipt) {
        return Result.success(arService.saveReceipt(receipt));
    }

    @PutMapping("/receipt")
    public Result<Boolean> editReceipt(@RequestBody ArReceipt receipt) {
        return Result.success(receiptMapper.updateById(receipt) > 0);
    }

    @DeleteMapping("/receipt/{id}")
    public Result<Boolean> delReceipt(@PathVariable Long id) {
        return Result.success(receiptMapper.deleteById(id) > 0);
    }

    @PostMapping("/receipt/audit/{id}")
    public Result<Boolean> auditReceipt(@PathVariable Long id) {
        return Result.success(arService.auditReceipt(id));
    }

    // 核销
    @PostMapping("/writeoff")
    public Result<Boolean> writeoff(@RequestBody Map<String, Object> body) {
        Long receiptId = Long.valueOf(body.get("receiptId").toString());
        Long invoiceId = Long.valueOf(body.get("invoiceId").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        String remark = (String) body.get("remark");
        return Result.success(arService.writeoff(receiptId, invoiceId, amount, remark));
    }

    @GetMapping("/writeoff/receipt/{receiptId}")
    public Result<List<ArWriteoff>> writeoffByReceipt(@PathVariable Long receiptId) {
        return Result.success(writeoffMapper.selectByReceipt(receiptId));
    }

    @GetMapping("/writeoff/invoice/{invoiceId}")
    public Result<List<ArWriteoff>> writeoffByInvoice(@PathVariable Long invoiceId) {
        return Result.success(writeoffMapper.selectByInvoice(invoiceId));
    }

    // 客户余额
    @GetMapping("/customer/{customerId}/uncollected")
    public Result<BigDecimal> uncollected(@PathVariable Long customerId) {
        return Result.success(arService.getCustomerUncollected(customerId));
    }

    @GetMapping("/customer/{customerId}/unapplied")
    public Result<BigDecimal> unapplied(@PathVariable Long customerId) {
        return Result.success(arService.getCustomerUnapplied(customerId));
    }

    // 账龄分析
    @GetMapping("/aging/{customerId}")
    public Result<List<Map<String, Object>>> aging(@PathVariable Long customerId) {
        return Result.success(arService.agingAnalysis(customerId));
    }
}
