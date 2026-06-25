package com.finance.module.asset.service;

import com.finance.module.asset.entity.FaCard;
import com.finance.module.asset.entity.FaDepreciation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IFaService {
    boolean saveCard(FaCard card);
    boolean updateCard(FaCard card);
    Map<String, Object> calculateDepreciation(String year, Integer period);
    List<FaDepreciation> listDepreciations(String year, Integer period);
    boolean saveDisposal(Long cardId, String disposalType, BigDecimal disposalAmount, String reason);
}
