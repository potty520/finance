package com.finance.module.contract.service.impl;

import com.finance.module.contract.entity.Contract;
import com.finance.module.contract.entity.PurchaseContract;
import com.finance.module.contract.mapper.ContractMapper;
import com.finance.module.contract.mapper.PurchaseContractMapper;
import com.finance.module.contract.service.IContractService;
import com.finance.module.workflow.service.IWorkflowService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class ContractServiceImpl implements IContractService {

    @Resource private ContractMapper contractMapper;
    @Resource private PurchaseContractMapper purchaseContractMapper;
    @Resource private IWorkflowService wfService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Contract save(Contract c) {
        if (c.getContractNo() == null) c.setContractNo("CT-" + System.currentTimeMillis());
        if (c.getPartyId() == null) c.setPartyId(0L);
        if (c.getAmount() == null) c.setAmount(BigDecimal.ZERO);
        String dbStatus = toDbStatus(c.getStatus());
        if (dbStatus == null) dbStatus = "DRAFT";

        if ("BUY".equalsIgnoreCase(c.getContractType())) {
            PurchaseContract pc = new PurchaseContract();
            BeanUtils.copyProperties(c, pc);
            pc.setStatus(dbStatus);
            if (pc.getId() == null) {
                purchaseContractMapper.insert(pc);
                wfService.start("contract", pc.getId(), pc.getContractNo(),
                        pc.getOwnerId(), null, pc.getAmount());
            } else {
                purchaseContractMapper.updateById(pc);
            }
            return toContract(pc);
        }

        c.setStatus(dbStatus);
        if (c.getId() == null) {
            contractMapper.insert(c);
            wfService.start("contract", c.getId(), c.getContractNo(),
                    c.getOwnerId(), c.getOwnerName(), c.getAmount());
        } else {
            contractMapper.updateById(c);
        }
        c.setContractType("SELL");
        c.setStatus(toFrontendStatus(c.getStatus()));
        return c;
    }

    @Override
    public boolean terminate(Long id, String contractType) {
        if ("BUY".equalsIgnoreCase(contractType)) {
            PurchaseContract pc = purchaseContractMapper.selectById(id);
            if (pc == null) return false;
            pc.setStatus("CANCEL");
            return purchaseContractMapper.updateById(pc) > 0;
        }
        Contract c = contractMapper.selectById(id);
        if (c == null) return false;
        c.setStatus("CANCEL");
        return contractMapper.updateById(c) > 0;
    }

    private Contract toContract(PurchaseContract pc) {
        Contract c = new Contract();
        BeanUtils.copyProperties(pc, c);
        c.setContractType("BUY");
        c.setStatus(toFrontendStatus(pc.getStatus()));
        return c;
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
