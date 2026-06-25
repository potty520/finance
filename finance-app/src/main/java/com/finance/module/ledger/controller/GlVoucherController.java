package com.finance.module.ledger.controller;

import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.module.ledger.entity.GlVoucher;
import com.finance.module.ledger.service.IGlVoucherService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 凭证
 */
@RestController
@RequestMapping("/ledger/voucher")
public class GlVoucherController {

    @Resource
    private IGlVoucherService voucherService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('gl:voucher:list')")
    public Result<PageResult<GlVoucher>> page(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String voucherNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String fiscalYear,
            @RequestParam(required = false) Integer fiscalPeriod) {
        return Result.success(voucherService.pageQuery(pageNum, pageSize, voucherNo, status,
                startDate, endDate, fiscalYear, fiscalPeriod));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('gl:voucher:list')")
    public Result<List<GlVoucher>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String fiscalYear,
            @RequestParam(required = false) Integer fiscalPeriod) {
        return Result.success(voucherService.listQuery(status, fiscalYear, fiscalPeriod));
    }

    @GetMapping("/{id}")
    public Result<GlVoucher> get(@PathVariable Long id) {
        return Result.success(voucherService.getWithEntries(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('gl:voucher:add', 'gl:voucher:list')")
    public Result<Boolean> add(@RequestBody GlVoucher voucher) {
        return Result.success(voucherService.saveWithEntries(voucher));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('gl:voucher:edit', 'gl:voucher:list')")
    public Result<Boolean> edit(@RequestBody GlVoucher voucher) {
        return Result.success(voucherService.updateWithEntries(voucher));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('gl:voucher:delete', 'gl:voucher:list')")
    public Result<Boolean> delete(@PathVariable Long id) {
        GlVoucher v = voucherService.getById(id);
        if (v != null && ("POSTED".equals(v.getStatus()) || "P".equals(v.getStatus()))) {
            return Result.error("已过账凭证不可删除");
        }
        return Result.success(voucherService.removeById(id));
    }

    @PostMapping("/submit/{id}")
    public Result<Boolean> submit(@PathVariable Long id) {
        return Result.success(voucherService.submit(id));
    }

    @PostMapping("/audit")
    @PreAuthorize("hasAnyAuthority('gl:voucher:audit', 'gl:voucher:list')")
    public Result<Boolean> audit(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        boolean pass = Boolean.parseBoolean(body.get("pass").toString());
        String reason = (String) body.get("reason");
        return Result.success(voucherService.audit(id, 1L, "系统用户", pass, reason));
    }

    /** 一键审核：草稿/驳回先提交，再审核通过 */
    @PostMapping("/approve/{id}")
    @PreAuthorize("hasAnyAuthority('gl:voucher:audit', 'gl:voucher:list')")
    public Result<Boolean> approve(@PathVariable Long id) {
        return Result.success(voucherService.approve(id, 1L, "系统用户"));
    }

    @PostMapping("/post/{id}")
    @PreAuthorize("hasAnyAuthority('gl:voucher:post', 'gl:voucher:list')")
    public Result<Boolean> post(@PathVariable Long id) {
        return Result.success(voucherService.post(id, 1L, "系统用户"));
    }

    @PostMapping("/unpost/{id}")
    @PreAuthorize("hasAnyAuthority('gl:voucher:post', 'gl:voucher:list')")
    public Result<Boolean> unpost(@PathVariable Long id) {
        return Result.success(voucherService.unPost(id));
    }

    @PostMapping("/reverse/{id}")
    @PreAuthorize("hasAnyAuthority('gl:voucher:reverse', 'gl:voucher:list')")
    public Result<Boolean> reverse(@PathVariable Long id, @RequestParam(required = false) String reason) {
        return Result.success(voucherService.reverseVoucher(id, reason));
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics(@RequestParam String fiscalYear,
                                                  @RequestParam Integer fiscalPeriod) {
        return Result.success(voucherService.getVoucherStatistics(fiscalYear, fiscalPeriod));
    }

    @GetMapping("/nextNo")
    @PreAuthorize("hasAnyAuthority('gl:voucher:add', 'gl:voucher:list')")
    public Result<String> nextNo(@RequestParam String fiscalYear, @RequestParam Integer fiscalPeriod) {
        return Result.success(voucherService.generateNextVoucherNo(fiscalYear, fiscalPeriod));
    }
}
