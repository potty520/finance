package com.finance.module.contract.service;

import com.finance.module.contract.entity.Contract;

public interface IContractService {
    Contract save(Contract c);
    boolean terminate(Long id, String contractType);
}
