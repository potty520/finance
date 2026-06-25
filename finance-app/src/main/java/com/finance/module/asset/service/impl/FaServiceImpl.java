package com.finance.module.asset.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import com.finance.module.asset.entity.FaCard;
import com.finance.module.asset.entity.FaDepreciation;
import com.finance.module.asset.entity.FaDisposal;
import com.finance.module.asset.mapper.FaCardMapper;
import com.finance.module.asset.mapper.FaDepreciationMapper;
import com.finance.module.asset.mapper.FaDisposalMapper;
import com.finance.module.asset.service.IFaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FaServiceImpl implements IFaService {

    @Resource private FaCardMapper cardMapper;
    @Resource private FaDepreciationMapper depMapper;
    @Resource private FaDisposalMapper disposalMapper;

    @Override
    public boolean saveCard(FaCard card) {
        if (StrUtil.isBlank(card.getCardCode())) {
            card.setCardCode(generateCardCode());
        }
        if (card.getStatus() == null) card.setStatus("NORMAL");
        if (card.getUsedMonth() == null) card.setUsedMonth(0);
        if (card.getDepreciationMethod() == null) card.setDepreciationMethod("STRAIGHT");
        // 计算残值
        if (card.getResidualRate() != null && card.getOriginalValue() != null) {
            card.setResidualValue(card.getOriginalValue().multiply(card.getResidualRate())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
        }
        if (card.getNetValue() == null) card.setNetValue(card.getOriginalValue());
        return cardMapper.insert(card) > 0;
    }

    @Override
    public boolean updateCard(FaCard card) {
        FaCard exist = cardMapper.selectById(card.getId());
        if (exist == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if ("CLEARED".equals(exist.getStatus())) {
            throw new BusinessException("已处置的资产不可修改");
        }
        return cardMapper.updateById(card) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> calculateDepreciation(String year, Integer period) {
        // 查询在用资产
        List<FaCard> cards = cardMapper.selectList(new LambdaQueryWrapper<FaCard>()
                .eq(FaCard::getStatus, "NORMAL"));
        BigDecimal totalDep = BigDecimal.ZERO;
        int count = 0;
        YearMonth ym = YearMonth.of(Integer.parseInt(year), period);
        LocalDate depDate = ym.atEndOfMonth();
        String periodCode = year + String.format("%02d", period);
        for (FaCard card : cards) {
            Long existCount = depMapper.selectCount(new LambdaQueryWrapper<FaDepreciation>()
                    .eq(FaDepreciation::getCardId, card.getId())
                    .eq(FaDepreciation::getPeriodCode, periodCode));
            if (existCount > 0) continue;

            BigDecimal monthDep = calculateMonthlyDepreciation(card, ym);
            if (monthDep.compareTo(BigDecimal.ZERO) <= 0) continue;

            // 累计折旧不能超过 (原值-残值)
            BigDecimal acc = card.getNetValue() == null ? BigDecimal.ZERO
                    : card.getOriginalValue().subtract(card.getNetValue());
            BigDecimal maxDep = card.getOriginalValue().subtract(card.getResidualValue() == null ? BigDecimal.ZERO : card.getResidualValue());
            if (acc.add(monthDep).compareTo(maxDep) > 0) {
                monthDep = maxDep.subtract(acc);
                if (monthDep.compareTo(BigDecimal.ZERO) <= 0) continue;
            }

            FaDepreciation dep = new FaDepreciation();
            dep.setCardId(card.getId());
            dep.setCardCode(card.getCardCode());
            dep.setCardName(card.getCardName());
            dep.setFiscalYear(year);
            dep.setFiscalPeriod(period);
            dep.setPeriodCode(periodCode);
            dep.setDepreciationDate(depDate);
            dep.setOriginalValue(card.getOriginalValue());
            dep.setPeriodDepreciation(monthDep);
            dep.setAccumulatedDepreciation(acc.add(monthDep));
            dep.setNetValue(card.getOriginalValue().subtract(dep.getAccumulatedDepreciation()));
            dep.setStatus("A");
            dep.setOperator(1L);
            dep.setOperatorName("系统用户");
            dep.setCreateTime(LocalDateTime.now());
            dep.setDeleted(0);
            depMapper.insert(dep);

            // 更新卡片累计折旧与净值
            card.setNetValue(dep.getNetValue());
            card.setUsedMonth((card.getUsedMonth() == null ? 0 : card.getUsedMonth()) + 1);
            card.setRemainMonth(Math.max(0, (card.getUseLifeMonth() == null ? 0 : card.getUseLifeMonth()) - card.getUsedMonth()));
            cardMapper.updateById(card);

            totalDep = totalDep.add(monthDep);
            count++;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("totalDepreciation", totalDep);
        result.put("count", count);
        result.put("fiscalYear", year);
        result.put("fiscalPeriod", period);
        return result;
    }

    /**
     * 计算单月折旧
     */
    private BigDecimal calculateMonthlyDepreciation(FaCard card, YearMonth ym) {
        if (card.getOriginalValue() == null || card.getUseLifeMonth() == null || card.getUseLifeMonth() == 0) {
            return BigDecimal.ZERO;
        }
        // 仅从购入下月开始计提
        YearMonth purchaseYm = YearMonth.from(card.getPurchaseDate());
        if (ym.isBefore(purchaseYm.plusMonths(1))) {
            return BigDecimal.ZERO;
        }
        BigDecimal residual = card.getResidualValue() == null ? BigDecimal.ZERO : card.getResidualValue();
        String method = card.getDepreciationMethod();
        if (method == null) method = "1";

        switch (method) {
            case "1":
                // 平均年限法
                return card.getOriginalValue().subtract(residual)
                        .divide(new BigDecimal(card.getUseLifeMonth()), 2, RoundingMode.HALF_UP);
            case "2":
                // 工作量法 (简化)
                return card.getOriginalValue().subtract(residual)
                        .divide(new BigDecimal(card.getUseLifeMonth()), 2, RoundingMode.HALF_UP);
            case "3":
                // 双倍余额递减法
                BigDecimal rate = new BigDecimal("2").divide(new BigDecimal(card.getUseLifeMonth()), 4, RoundingMode.HALF_UP);
                BigDecimal yearly = card.getNetValue() == null ? card.getOriginalValue() : card.getNetValue();
                yearly = yearly.multiply(rate);
                return yearly.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            case "4":
                // 年数总和法
                int n = card.getUseLifeMonth() / 12;
                int yearIndex = (card.getUsedMonth() == null ? 0 : card.getUsedMonth()) / 12 + 1;
                BigDecimal total = new BigDecimal(n * (n + 1) / 2);
                BigDecimal weight = new BigDecimal(n - yearIndex + 1);
                return card.getOriginalValue().subtract(residual)
                        .multiply(weight)
                        .divide(total.multiply(new BigDecimal("12")), 2, RoundingMode.HALF_UP);
            default:
                return card.getOriginalValue().subtract(residual)
                        .divide(new BigDecimal(card.getUseLifeMonth()), 2, RoundingMode.HALF_UP);
        }
    }

    @Override
    public List<FaDepreciation> listDepreciations(String year, Integer period) {
        String periodCode = year + String.format("%02d", period);
        return depMapper.selectList(new LambdaQueryWrapper<FaDepreciation>()
                .eq(FaDepreciation::getPeriodCode, periodCode)
                .orderByDesc(FaDepreciation::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveDisposal(Long cardId, String disposalType, BigDecimal disposalAmount, String reason) {
        FaCard card = cardMapper.selectById(cardId);
        if (card == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if ("CLEARED".equals(card.getStatus())) {
            throw new BusinessException("资产已处置");
        }
        FaDisposal d = new FaDisposal();
        d.setBillNo("DP-" + System.currentTimeMillis());
        d.setCardId(cardId);
        d.setCardCode(card.getCardCode());
        d.setCardName(card.getCardName());
        d.setDisposalDate(LocalDate.now());
        d.setDisposalType(disposalType == null ? "SCRAP" : disposalType);
        d.setOriginalValue(card.getOriginalValue());
        BigDecimal acc = card.getOriginalValue().subtract(card.getNetValue() == null ? card.getOriginalValue() : card.getNetValue());
        d.setAccumulatedDepreciation(acc);
        d.setResidualValue(card.getResidualValue());
        d.setDisposalAmount(disposalAmount == null ? BigDecimal.ZERO : disposalAmount);
        BigDecimal profitLoss = d.getDisposalAmount().subtract(card.getNetValue() == null ? BigDecimal.ZERO : card.getNetValue());
        d.setProfitLoss(profitLoss);
        d.setReason(reason);
        d.setStatus("DRAFT");
        d.setOperator(1L);
        d.setOperatorName("系统用户");
        d.setCreateTime(LocalDateTime.now());
        d.setDeleted(0);
        boolean ok = disposalMapper.insert(d) > 0;
        if (ok) {
            card.setStatus("CLEARED");
            cardMapper.updateById(card);
        }
        return ok;
    }

    private String generateCardCode() {
        return "FA" + System.currentTimeMillis();
    }
}
