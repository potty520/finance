package com.finance.module.contract.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.contract.entity.Contract;
import com.finance.module.contract.entity.PurchaseContract;
import com.finance.module.contract.mapper.ContractMapper;
import com.finance.module.contract.mapper.PurchaseContractMapper;
import com.finance.module.contract.service.IContractService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Resource private IContractService contractService;
    @Resource private ContractMapper contractMapper;
    @Resource private PurchaseContractMapper purchaseContractMapper;

    @GetMapping("/page")
    public Result<PageResult<Contract>> page(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String contractType) {
        long pn = CommonUtil.safePageNum(pageNum);
        long ps = CommonUtil.safePageSize(pageSize);
        String dbStatus = toDbStatus(status);

        if ("BUY".equalsIgnoreCase(contractType)) {
            return Result.success(queryPurchasePage(pn, ps, dbStatus));
        }
        if ("SELL".equalsIgnoreCase(contractType)) {
            return Result.success(querySalesPage(pn, ps, dbStatus));
        }

        PageResult<Contract> sales = querySalesPage(pn, ps, dbStatus);
        PageResult<Contract> purchase = queryPurchasePage(pn, ps, dbStatus);
        List<Contract> merged = new ArrayList<>(sales.getRecords());
        merged.addAll(purchase.getRecords());
        merged.sort(Comparator.comparing(Contract::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder())));
        long total = sales.getTotal() + purchase.getTotal();
        return Result.success(PageResult.of(pn, ps, total, merged));
    }

    @GetMapping("/{id}")
    public Result<Contract> get(@PathVariable Long id, @RequestParam(required = false) String contractType) {
        if ("BUY".equalsIgnoreCase(contractType)) {
            PurchaseContract pc = purchaseContractMapper.selectById(id);
            return Result.success(pc == null ? null : toContract(pc));
        }
        Contract c = contractMapper.selectById(id);
        if (c != null) {
            c.setContractType("SELL");
            normalizeStatus(c);
        }
        return Result.success(c);
    }

    @PostMapping
    public Result<Contract> save(@RequestBody Contract c) {
        return Result.success(contractService.save(c));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> del(@PathVariable Long id, @RequestParam(required = false) String contractType) {
        if ("BUY".equalsIgnoreCase(contractType)) {
            return Result.success(purchaseContractMapper.deleteById(id) > 0);
        }
        return Result.success(contractMapper.deleteById(id) > 0);
    }

    @PostMapping("/terminate/{id}")
    public Result<Boolean> terminate(@PathVariable Long id, @RequestParam(required = false) String contractType) {
        return Result.success(contractService.terminate(id, contractType));
    }

    @GetMapping("/stats")
    public Result<List<Map<String, Object>>> stats() {
        return Result.success(contractMapper.stats());
    }

    private PageResult<Contract> querySalesPage(long pageNum, long pageSize, String dbStatus) {
        LambdaQueryWrapper<Contract> qw = new LambdaQueryWrapper<>();
        if (dbStatus != null) qw.eq(Contract::getStatus, dbStatus);
        qw.orderByDesc(Contract::getCreateTime);
        Page<Contract> p = contractMapper.selectPage(new Page<>(pageNum, pageSize), qw);
        p.getRecords().forEach(c -> {
            c.setContractType("SELL");
            normalizeStatus(c);
        });
        return CommonUtil.toPageResult(p);
    }

    private PageResult<Contract> queryPurchasePage(long pageNum, long pageSize, String dbStatus) {
        LambdaQueryWrapper<PurchaseContract> qw = new LambdaQueryWrapper<>();
        if (dbStatus != null) qw.eq(PurchaseContract::getStatus, dbStatus);
        qw.orderByDesc(PurchaseContract::getCreateTime);
        Page<PurchaseContract> p = purchaseContractMapper.selectPage(new Page<>(pageNum, pageSize), qw);
        List<Contract> records = p.getRecords().stream().map(this::toContract).collect(Collectors.toList());
        return PageResult.of(p.getCurrent(), p.getSize(), p.getTotal(), records);
    }

    private Contract toContract(PurchaseContract pc) {
        Contract c = new Contract();
        BeanUtils.copyProperties(pc, c);
        c.setContractType("BUY");
        normalizeStatus(c);
        return c;
    }

    private void normalizeStatus(Contract c) {
        c.setStatus(toFrontendStatus(c.getStatus()));
    }

    private String toDbStatus(String frontendStatus) {
        if (frontendStatus == null || frontendStatus.isEmpty()) return null;
        switch (frontendStatus) {
            case "0": return "DRAFT";
            case "1": return "SIGNED";
            case "2": return "CANCEL";
            case "3": return "COMPLETED";
            default: return frontendStatus;
        }
    }

    private String toFrontendStatus(String dbStatus) {
        if (dbStatus == null) return "0";
        switch (dbStatus) {
            case "DRAFT": return "0";
            case "SIGNED":
            case "EXECUTING": return "1";
            case "CANCEL": return "2";
            case "COMPLETED": return "3";
            default: return dbStatus;
        }
    }
}
