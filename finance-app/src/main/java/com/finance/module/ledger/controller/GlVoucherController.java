package com.finance.module.ledger.controller;

import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.service.CurrentUserResolver;
import com.finance.module.system.entity.SysUser;
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

    @Resource
    private CurrentUserResolver currentUserResolver;
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
    @PreAuthorize("hasAuthority('gl:voucher:add')")
    public Result<Boolean> add(@RequestBody GlVoucher voucher) {
        return Result.success(voucherService.saveWithEntries(voucher));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('gl:voucher:edit')")
    public Result<Boolean> edit(@RequestBody GlVoucher voucher) {
        return Result.success(voucherService.updateWithEntries(voucher));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('gl:voucher:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        GlVoucher v = voucherService.getById(id);
        if (v == null) return Result.success(true);
        String st = v.getStatus();
        // 只有草稿和被驳回的凭证允许删除；已审核需先取消审核，已过账需先反过账
        if ("POSTED".equals(st) || "P".equals(st)) {
            return Result.error("已过账凭证不可删除，请先反过账");
        }
        if ("APPROVED".equals(st) || "AUDITED".equals(st) || "A".equals(st) || "SUBMITTED".equals(st)) {
            return Result.error("已审核/已提交凭证不可删除，请先取消审核");
        }
        return Result.success(voucherService.removeById(id));
    }

    @PostMapping("/submit/{id}")
    public Result<Boolean> submit(@PathVariable Long id) {
        return Result.success(voucherService.submit(id));
    }

    @PostMapping("/audit")
    @PreAuthorize("hasAuthority('gl:voucher:audit')")
    public Result<Boolean> audit(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        boolean pass = Boolean.parseBoolean(body.get("pass").toString());
        String reason = (String) body.get("reason");
        SysUser cu = currentUserResolver.require();
        return Result.success(voucherService.audit(id, cu.getId(), displayName(cu), pass, reason));
    }

    /** 一键审核：草稿/驳回先提交，再审核通过 */
    @PostMapping("/approve/{id}")
    @PreAuthorize("hasAuthority('gl:voucher:audit')")
    public Result<Boolean> approve(@PathVariable Long id) {
        SysUser cu = currentUserResolver.require();
        return Result.success(voucherService.approve(id, cu.getId(), displayName(cu)));
    }

    @PostMapping("/post/{id}")
    @PreAuthorize("hasAuthority('gl:voucher:post')")
    public Result<Boolean> post(@PathVariable Long id) {
        SysUser cu = currentUserResolver.require();
        return Result.success(voucherService.post(id, cu.getId(), displayName(cu)));
    }

    @PostMapping("/unpost/{id}")
    @PreAuthorize("hasAuthority('gl:voucher:post')")
    public Result<Boolean> unpost(@PathVariable Long id) {
        return Result.success(voucherService.unPost(id));
    }

    @PostMapping("/reverse/{id}")
    @PreAuthorize("hasAuthority('gl:voucher:reverse')")
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

    private String displayName(SysUser u) {
        return u.getRealName() != null ? u.getRealName() : u.getUsername();
    }
}
