package com.finance.module.asset.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.asset.entity.*;
import com.finance.module.asset.mapper.*;
import com.finance.module.asset.service.IFaService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/asset")
public class AssetController {

    @Resource private IFaService faService;
    @Resource private FaCardMapper cardMapper;
    @Resource private FaCategoryMapper categoryMapper;
    @Resource private FaDepreciationMapper depMapper;
    @Resource private FaDisposalMapper disposalMapper;
    @Resource private FaInventoryMapper inventoryMapper;

    // 卡片
    @GetMapping("/card/page")
    public Result<PageResult<FaCard>> cardPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long deptId) {
        LambdaQueryWrapper<FaCard> qw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            qw.and(w -> w.like(FaCard::getCardCode, keyword).or().like(FaCard::getCardName, keyword));
        }
        if (categoryCode != null && !categoryCode.isEmpty()) qw.eq(FaCard::getCategoryId, categoryCode);
        if (status != null && !status.isEmpty()) qw.eq(FaCard::getStatus, status);
        if (deptId != null) qw.eq(FaCard::getDeptId, deptId);
        qw.orderByDesc(FaCard::getCreateTime);
        Page<FaCard> p = cardMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    @GetMapping("/card/{id}")
    public Result<FaCard> getCard(@PathVariable Long id) {
        return Result.success(cardMapper.selectById(id));
    }

    @PostMapping("/card")
    public Result<Boolean> addCard(@RequestBody FaCard card) {
        return Result.success(faService.saveCard(card));
    }

    @PutMapping("/card")
    public Result<Boolean> editCard(@RequestBody FaCard card) {
        return Result.success(faService.updateCard(card));
    }

    @DeleteMapping("/card/{id}")
    public Result<Boolean> delCard(@PathVariable Long id) {
        return Result.success(cardMapper.deleteById(id) > 0);
    }

    // 类别
    @GetMapping("/category/list")
    public Result<List<FaCategory>> categoryList() {
        return Result.success(categoryMapper.selectList(
                new LambdaQueryWrapper<FaCategory>().orderByAsc(FaCategory::getCategoryCode)));
    }

    @PostMapping("/category")
    public Result<Boolean> addCategory(@RequestBody FaCategory c) {
        if (c.getStatus() == null) c.setStatus(1);
        return Result.success(categoryMapper.insert(c) > 0);
    }

    @PutMapping("/category")
    public Result<Boolean> editCategory(@RequestBody FaCategory c) {
        return Result.success(categoryMapper.updateById(c) > 0);
    }

    @DeleteMapping("/category/{id}")
    public Result<Boolean> delCategory(@PathVariable Long id) {
        return Result.success(categoryMapper.deleteById(id) > 0);
    }

    // 折旧
    @PostMapping("/depreciation/calculate")
    public Result<Map<String, Object>> calculateDepreciation(@RequestBody Map<String, Object> body) {
        String year = (String) body.get("fiscalYear");
        Integer period = Integer.valueOf(body.get("fiscalPeriod").toString());
        return Result.success(faService.calculateDepreciation(year, period));
    }

    @GetMapping("/depreciation/list")
    public Result<List<FaDepreciation>> listDepreciations(
            @RequestParam String fiscalYear,
            @RequestParam Integer fiscalPeriod) {
        return Result.success(faService.listDepreciations(fiscalYear, fiscalPeriod));
    }

    @GetMapping("/depreciation/page")
    public Result<PageResult<FaDepreciation>> depreciationPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String fiscalYear,
            @RequestParam(required = false) Integer fiscalPeriod) {
        LambdaQueryWrapper<FaDepreciation> qw = new LambdaQueryWrapper<>();
        if (fiscalYear != null && fiscalPeriod != null) {
            qw.eq(FaDepreciation::getPeriodCode, fiscalYear + String.format("%02d", fiscalPeriod));
        }
        qw.orderByDesc(FaDepreciation::getCreateTime);
        Page<FaDepreciation> p = depMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        return Result.success(CommonUtil.toPageResult(p));
    }

    // 处置
    @PostMapping("/disposal")
    public Result<Boolean> disposal(@RequestBody Map<String, Object> body) {
        Long cardId = Long.valueOf(body.get("cardId").toString());
        String disposalType = (String) body.get("disposalType");
        Object amountObj = body.get("disposalAmount");
        java.math.BigDecimal disposalAmount = amountObj == null ? java.math.BigDecimal.ZERO
                : new java.math.BigDecimal(amountObj.toString());
        String reason = body.get("reason") != null ? body.get("reason").toString() : null;
        return Result.success(faService.saveDisposal(cardId, disposalType, disposalAmount, reason));
    }

    @GetMapping("/disposal/page")
    public Result<PageResult<FaDisposal>> disposalPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize) {
        Page<FaDisposal> p = disposalMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)),
                new LambdaQueryWrapper<FaDisposal>().orderByDesc(FaDisposal::getCreateTime));
        return Result.success(CommonUtil.toPageResult(p));
    }

    // 盘点
    @GetMapping("/inventory/page")
    public Result<PageResult<FaInventory>> inventoryPage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize) {
        Page<FaInventory> p = inventoryMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)),
                new LambdaQueryWrapper<FaInventory>().orderByDesc(FaInventory::getCreateTime));
        return Result.success(CommonUtil.toPageResult(p));
    }

    @PostMapping("/inventory")
    public Result<Boolean> addInventory(@RequestBody FaInventory inv) {
        if (inv.getStatus() == null) inv.setStatus("0");
        return Result.success(inventoryMapper.insert(inv) > 0);
    }

    // 报表
    @GetMapping("/report/byCategory")
    public Result<List<Map<String, Object>>> reportByCategory() {
        return Result.success(cardMapper.sumByCategory());
    }
}
