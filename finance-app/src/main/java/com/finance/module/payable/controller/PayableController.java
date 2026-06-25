package com.finance.module.payable.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.payable.entity.ApInvoice;
import com.finance.module.payable.entity.ApPayment;
import com.finance.module.payable.entity.ApSupplier;
import com.finance.module.payable.entity.ApWriteoff;
import com.finance.module.payable.mapper.ApInvoiceMapper;
import com.finance.module.payable.mapper.ApPaymentMapper;
import com.finance.module.payable.mapper.ApSupplierMapper;
import com.finance.module.payable.service.IApService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payable")
public class PayableController {

    @Resource private IApService apService;
    @Resource private ApSupplierMapper supplierMapper;
    @Resource private ApInvoiceMapper invoiceMapper;
    @Resource private ApPaymentMapper paymentMapper;

    @GetMapping("/supplier/page")
    public Result<PageResult<ApSupplier>> supplierPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<ApSupplier> qw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            qw.and(w -> w.like(ApSupplier::getSupplierCode, keyword)
                    .or().like(ApSupplier::getSupplierName, keyword));
        }
        qw.eq(ApSupplier::getStatus, 1).orderByAsc(ApSupplier::getSupplierCode);
        Page<ApSupplier> p = supplierMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/supplier/list")
    public Result<List<ApSupplier>> supplierList() {
        return Result.success(supplierMapper.selectList(
                new LambdaQueryWrapper<ApSupplier>()
                        .eq(ApSupplier::getStatus, 1)
                        .orderByAsc(ApSupplier::getSupplierCode)));
    }

    @GetMapping("/invoice/page")
    public Result<PageResult<ApInvoice>> invoicePage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String billNo,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) String status) {
        LambdaQueryWrapper<ApInvoice> qw = new LambdaQueryWrapper<>();
        if (billNo != null && !billNo.isEmpty()) qw.like(ApInvoice::getBillNo, billNo);
        if (supplierId != null) qw.eq(ApInvoice::getSupplierId, supplierId);
        if (status != null && !status.isEmpty()) qw.eq(ApInvoice::getStatus, status);
        qw.orderByDesc(ApInvoice::getInvoiceDate);
        Page<ApInvoice> p = invoiceMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/invoice/{id}")
    public Result<ApInvoice> getInvoice(@PathVariable Long id) {
        return Result.success(invoiceMapper.selectById(id));
    }

    @PostMapping("/invoice")
    public Result<Boolean> addInvoice(@RequestBody ApInvoice invoice) {
        return Result.success(apService.saveInvoice(invoice));
    }

    @PutMapping("/invoice")
    public Result<Boolean> editInvoice(@RequestBody ApInvoice invoice) {
        return Result.success(invoiceMapper.updateById(invoice) > 0);
    }

    @DeleteMapping("/invoice/{id}")
    public Result<Boolean> delInvoice(@PathVariable Long id) {
        ApInvoice i = invoiceMapper.selectById(id);
        if (i != null && i.getPaidAmount() != null && i.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
            return Result.error("已付款的发票不可删除");
        }
        return Result.success(invoiceMapper.deleteById(id) > 0);
    }

    @PostMapping("/invoice/audit/{id}")
    public Result<Boolean> auditInvoice(@PathVariable Long id) {
        return Result.success(apService.auditInvoice(id));
    }

    @GetMapping("/payment/page")
    public Result<PageResult<ApPayment>> paymentPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String billNo,
            @RequestParam(required = false) Long supplierId) {
        LambdaQueryWrapper<ApPayment> qw = new LambdaQueryWrapper<>();
        if (billNo != null && !billNo.isEmpty()) qw.like(ApPayment::getBillNo, billNo);
        if (supplierId != null) qw.eq(ApPayment::getSupplierId, supplierId);
        qw.eq(ApPayment::getDeleted, 0).orderByDesc(ApPayment::getPaymentDate);
        Page<ApPayment> p = paymentMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @PostMapping("/payment")
    public Result<Boolean> addPayment(@RequestBody ApPayment payment) {
        return Result.success(apService.savePayment(payment));
    }

    @PutMapping("/payment")
    public Result<Boolean> editPayment(@RequestBody ApPayment payment) {
        return Result.success(paymentMapper.updateById(payment) > 0);
    }

    @DeleteMapping("/payment/{id}")
    public Result<Boolean> delPayment(@PathVariable Long id) {
        return Result.success(paymentMapper.deleteById(id) > 0);
    }

    @PostMapping("/payment/audit/{id}")
    public Result<Boolean> auditPayment(@PathVariable Long id) {
        return Result.success(apService.auditPayment(id));
    }

    @PostMapping("/writeoff")
    public Result<Boolean> writeoff(@RequestBody Map<String, Object> body) {
        Long paymentId = Long.valueOf(body.get("paymentId").toString());
        Long invoiceId = Long.valueOf(body.get("invoiceId").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        String remark = (String) body.get("remark");
        return Result.success(apService.writeoff(paymentId, invoiceId, amount, remark));
    }

    @GetMapping("/writeoff/payment/{paymentId}")
    public Result<List<ApWriteoff>> writeoffByPayment(@PathVariable Long paymentId) {
        return Result.success(apService.writeoffsByPayment(paymentId));
    }

    @GetMapping("/writeoff/invoice/{invoiceId}")
    public Result<List<ApWriteoff>> writeoffByInvoice(@PathVariable Long invoiceId) {
        return Result.success(apService.writeoffsByInvoice(invoiceId));
    }

    @GetMapping("/supplier/{supplierId}/unpaid")
    public Result<BigDecimal> unpaid(@PathVariable Long supplierId) {
        return Result.success(apService.getSupplierUnpaid(supplierId));
    }

    @GetMapping("/supplier/{supplierId}/unapplied")
    public Result<BigDecimal> unapplied(@PathVariable Long supplierId) {
        return Result.success(apService.getSupplierUnapplied(supplierId));
    }

    @GetMapping("/aging/{supplierId}")
    public Result<List<Map<String, Object>>> aging(@PathVariable Long supplierId) {
        return Result.success(apService.agingAnalysis(supplierId));
    }
}
