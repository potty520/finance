package com.finance.module.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.module.contract.entity.Contract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface ContractMapper extends BaseMapper<Contract> {
    BigDecimal sumAmount(@Param("status") String status);
    List<Map<String, Object>> stats();
}
