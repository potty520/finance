package com.finance.module.ledger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.common.response.PageResult;
import com.finance.module.ledger.entity.GlVoucher;

import java.util.List;
import java.util.Map;

public interface IGlVoucherService extends IService<GlVoucher> {

    PageResult<GlVoucher> pageQuery(Long pageNum, Long pageSize, String voucherNo, String status,
                                    String startDate, String endDate, String fiscalYear, Integer fiscalPeriod);

    GlVoucher getWithEntries(Long id);

    boolean saveWithEntries(GlVoucher voucher);

    boolean updateWithEntries(GlVoucher voucher);

    boolean submit(Long id);

    boolean audit(Long id, Long auditorId, String auditorName, boolean pass, String reason);

    boolean approve(Long id, Long auditorId, String auditorName);

    boolean post(Long id, Long posterId, String posterName);

    boolean unPost(Long id);

    boolean reverseVoucher(Long id, String reason);

    Map<String, Object> getVoucherStatistics(String fiscalYear, Integer fiscalPeriod);

    List<GlVoucher> listQuery(String status, String fiscalYear, Integer fiscalPeriod);

    String generateNextVoucherNo(String fiscalYear, Integer fiscalPeriod);
}
