package com.finance.module.ledger.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.common.exception.BusinessException;
import com.finance.common.response.PageResult;
import com.finance.common.response.ResultCode;
import com.finance.common.util.CommonUtil;
import com.finance.module.ledger.entity.GlPeriod;
import com.finance.module.ledger.entity.GlSubject;
import com.finance.module.ledger.entity.GlVoucher;
import com.finance.module.ledger.entity.GlVoucherEntry;
import com.finance.module.ledger.mapper.GlPeriodMapper;
import com.finance.module.ledger.mapper.GlSubjectMapper;
import com.finance.module.ledger.mapper.GlVoucherEntryMapper;
import com.finance.module.ledger.mapper.GlVoucherMapper;
import com.finance.module.ledger.service.IGlVoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 凭证服务
 */
@Slf4j
@Service
public class GlVoucherServiceImpl extends ServiceImpl<GlVoucherMapper, GlVoucher> implements IGlVoucherService {

    @Resource
    private GlVoucherEntryMapper entryMapper;
    @Resource
    private GlSubjectMapper subjectMapper;
    @Resource
    private GlPeriodMapper periodMapper;

    @Override
    public PageResult<GlVoucher> pageQuery(Long pageNum, Long pageSize, String voucherNo, String status,
                                            String startDate, String endDate, String fiscalYear, Integer fiscalPeriod) {
        LambdaQueryWrapper<GlVoucher> qw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(voucherNo)) qw.like(GlVoucher::getVoucherNo, voucherNo);
        if (StrUtil.isNotBlank(status)) qw.eq(GlVoucher::getStatus, status);
        if (StrUtil.isNotBlank(startDate)) qw.ge(GlVoucher::getVoucherDate, LocalDate.parse(startDate));
        if (StrUtil.isNotBlank(endDate)) qw.le(GlVoucher::getVoucherDate, LocalDate.parse(endDate));
        String periodCode = CommonUtil.buildPeriodCode(fiscalYear, fiscalPeriod);
        if (StrUtil.isNotBlank(periodCode)) qw.eq(GlVoucher::getPeriodCode, periodCode);
        qw.orderByDesc(GlVoucher::getVoucherDate, GlVoucher::getId);
        Page<GlVoucher> page = page(new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)), qw);
        page.getRecords().forEach(this::fillPeriodFields);
        return CommonUtil.toPageResult(page);
    }

    @Override
    public List<GlVoucher> listQuery(String status, String fiscalYear, Integer fiscalPeriod) {
        LambdaQueryWrapper<GlVoucher> qw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(status)) qw.eq(GlVoucher::getStatus, status);
        String periodCode = CommonUtil.buildPeriodCode(fiscalYear, fiscalPeriod);
        if (StrUtil.isNotBlank(periodCode)) qw.eq(GlVoucher::getPeriodCode, periodCode);
        qw.orderByDesc(GlVoucher::getVoucherDate, GlVoucher::getId);
        List<GlVoucher> list = list(qw);
        list.forEach(this::fillPeriodFields);
        return list;
    }

    @Override
    public GlVoucher getWithEntries(Long id) {
        GlVoucher v = getById(id);
        if (v != null) {
            fillPeriodFields(v);
            List<GlVoucherEntry> entries = entryMapper.selectByVoucherId(id);
            if (entries != null) {
                entries.forEach(GlVoucherEntry::afterLoad);
            }
            v.setEntries(entries);
        }
        return v;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveWithEntries(GlVoucher voucher) {
        validateVoucher(voucher);
        resolvePeriodCode(voucher);
        GlPeriod period = periodMapper.selectByYearPeriod(
                String.valueOf(voucher.getFiscalYear()), voucher.getFiscalPeriod());
        if (period != null && "CLOSED".equals(period.getStatus())) {
            throw new BusinessException(ResultCode.PERIOD_CLOSED);
        }
        if (StrUtil.isBlank(voucher.getVoucherNo())) {
            voucher.setVoucherNo(generateVoucherNo(voucher.getFiscalYear(), voucher.getFiscalPeriod()));
        }
        if (voucher.getStatus() == null) voucher.setStatus("DRAFT");
        voucher.setCreateTime(java.time.LocalDateTime.now());
        boolean ok = save(voucher);
        if (ok && voucher.getEntries() != null) {
            for (GlVoucherEntry e : voucher.getEntries()) {
                e.setVoucherId(voucher.getId());
                e.prepareForPersist();
                entryMapper.insert(e);
            }
        }
        return ok;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWithEntries(GlVoucher voucher) {
        GlVoucher exist = getById(voucher.getId());
        if (exist == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"DRAFT".equals(exist.getStatus()) && !"REJECTED".equals(exist.getStatus())
                && !"D".equals(exist.getStatus()) && !"R".equals(exist.getStatus())) {
            throw new BusinessException("已审核或已过账的凭证不可修改");
        }
        validateVoucher(voucher);
        voucher.setUpdateTime(java.time.LocalDateTime.now());
        boolean ok = updateById(voucher);
        if (ok) {
            entryMapper.deleteByVoucherId(voucher.getId());
            if (voucher.getEntries() != null) {
                for (GlVoucherEntry e : voucher.getEntries()) {
                    e.setVoucherId(voucher.getId());
                    e.prepareForPersist();
                    entryMapper.insert(e);
                }
            }
        }
        return ok;
    }

    @Override
    public boolean submit(Long id) {
        GlVoucher v = getById(id);
        if (v == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"DRAFT".equals(v.getStatus()) && !"REJECTED".equals(v.getStatus())
                && !"D".equals(v.getStatus()) && !"R".equals(v.getStatus())) {
            throw new BusinessException("仅草稿/驳回状态凭证可提交");
        }
        v.setStatus("APPROVING");
        v.setUpdateTime(java.time.LocalDateTime.now());
        return updateById(v);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean audit(Long id, Long auditorId, String auditorName, boolean pass, String reason) {
        GlVoucher v = getById(id);
        if (v == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"APPROVING".equals(v.getStatus()) && !"A".equals(v.getStatus())) {
            throw new BusinessException("仅待审核凭证可审核");
        }
        if (pass) {
            v.setStatus("APPROVED");
            v.setAuditBy(auditorId);
            v.setAuditByName(auditorName);
            v.setAuditTime(java.time.LocalDateTime.now());
        } else {
            v.setStatus("REJECTED");
            v.setRemark((v.getRemark() == null ? "" : v.getRemark()) + " [驳回: " + reason + "]");
        }
        v.setUpdateTime(java.time.LocalDateTime.now());
        return updateById(v);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approve(Long id, Long auditorId, String auditorName) {
        GlVoucher v = getById(id);
        if (v == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if ("POSTED".equals(v.getStatus()) || "P".equals(v.getStatus())) {
            throw new BusinessException("已过账凭证不可审核");
        }
        if ("APPROVED".equals(v.getStatus()) || "A".equals(v.getStatus())) {
            throw new BusinessException("凭证已审核");
        }
        if ("DRAFT".equals(v.getStatus()) || "REJECTED".equals(v.getStatus())
                || "D".equals(v.getStatus()) || "R".equals(v.getStatus())) {
            submit(id);
        }
        return audit(id, auditorId, auditorName, true, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean post(Long id, Long posterId, String posterName) {
        GlVoucher v = getById(id);
        if (v == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if ("POSTED".equals(v.getStatus()) || "P".equals(v.getStatus())) {
            throw new BusinessException(ResultCode.VOUCHER_ALREADY_POSTED);
        }
        if (!"APPROVED".equals(v.getStatus()) && !"A".equals(v.getStatus())) {
            throw new BusinessException("仅审核通过凭证可过账");
        }
        v.setStatus("POSTED");
        v.setPostBy(posterId);
        v.setPostTime(java.time.LocalDateTime.now());
        v.setUpdateTime(java.time.LocalDateTime.now());
        return updateById(v);
    }

    @Override
    public boolean unPost(Long id) {
        GlVoucher v = getById(id);
        if (v == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"POSTED".equals(v.getStatus()) && !"P".equals(v.getStatus())) {
            throw new BusinessException("仅已过账凭证可反过账");
        }
        v.setStatus("APPROVED");
        v.setPostTime(null);
        v.setUpdateTime(java.time.LocalDateTime.now());
        return updateById(v);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reverseVoucher(Long id, String reason) {
        GlVoucher v = getWithEntries(id);
        if (v == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"POSTED".equals(v.getStatus()) && !"P".equals(v.getStatus())) {
            throw new BusinessException("仅已过账凭证可冲销");
        }
        GlVoucher reverse = new GlVoucher();
        reverse.setVoucherDate(LocalDate.now());
        reverse.setFiscalYear(String.valueOf(LocalDate.now().getYear()));
        reverse.setFiscalPeriod(LocalDate.now().getMonthValue());
        resolvePeriodCode(reverse);
        reverse.setSummary("冲销 " + v.getVoucherNo() + (StrUtil.isBlank(reason) ? "" : " - " + reason));
        BigDecimal td = BigDecimal.ZERO, tc = BigDecimal.ZERO;
        if (v.getEntries() != null) {
            for (GlVoucherEntry e : v.getEntries()) {
                GlVoucherEntry ne = new GlVoucherEntry();
                org.springframework.beans.BeanUtils.copyProperties(e, ne);
                ne.setId(null);
                ne.setDebitAmount(e.getCreditAmount());
                ne.setCreditAmount(e.getDebitAmount());
                td = td.add(ne.getDebitAmount() == null ? BigDecimal.ZERO : ne.getDebitAmount());
                tc = tc.add(ne.getCreditAmount() == null ? BigDecimal.ZERO : ne.getCreditAmount());
            }
        }
        reverse.setTotalDebit(td);
        reverse.setTotalCredit(tc);
        reverse.setStatus("APPROVED");
        reverse.setSource("REVERSE");
        reverse.setSourceId(v.getId());
        reverse.setCreateTime(java.time.LocalDateTime.now());
        save(reverse);
        return true;
    }

    @Override
    public Map<String, Object> getVoucherStatistics(String fiscalYear, Integer fiscalPeriod) {
        Map<String, Object> result = new HashMap<>();
        String periodCode = CommonUtil.buildPeriodCode(fiscalYear, fiscalPeriod);
        QueryWrapper<GlVoucher> qw = new QueryWrapper<>();
        qw.eq("period_code", periodCode);
        long total = count(qw);
        QueryWrapper<GlVoucher> postedQw = new QueryWrapper<>();
        postedQw.eq("period_code", periodCode).in("status", "POSTED", "P");
        long posted = count(postedQw);
        QueryWrapper<GlVoucher> auditingQw = new QueryWrapper<>();
        auditingQw.eq("period_code", periodCode).in("status", "APPROVING", "A");
        long auditing = count(auditingQw);
        QueryWrapper<GlVoucher> draftQw = new QueryWrapper<>();
        draftQw.eq("period_code", periodCode).in("status", "DRAFT", "D");
        long draft = count(draftQw);
        result.put("total", total);
        result.put("posted", posted);
        result.put("auditing", auditing);
        result.put("draft", draft);
        return result;
    }

    private void validateVoucher(GlVoucher v) {
        if (v.getEntries() == null || v.getEntries().isEmpty()) {
            throw new BusinessException("凭证分录不能为空");
        }
        BigDecimal td = BigDecimal.ZERO, tc = BigDecimal.ZERO;
        int idx = 1;
        for (GlVoucherEntry e : v.getEntries()) {
            if (StrUtil.isBlank(e.getSubjectCode())) {
                throw new BusinessException("第" + idx + "行分录科目不能为空");
            }
            GlSubject s = subjectMapper.selectByCode(e.getSubjectCode());
            if (s == null) {
                throw new BusinessException("第" + idx + "行科目不存在: " + e.getSubjectCode());
            }
            if (s.getIsLeaf() != null && s.getIsLeaf() == 0) {
                throw new BusinessException("第" + idx + "行科目为汇总科目,不能录入分录: " + s.getSubjectCode());
            }
            td = td.add(e.getDebitAmount() == null ? BigDecimal.ZERO : e.getDebitAmount());
            tc = tc.add(e.getCreditAmount() == null ? BigDecimal.ZERO : e.getCreditAmount());
            e.setSubjectName(s.getSubjectName());
            e.setEntryNo(idx++);
        }
        if (td.compareTo(tc) != 0) {
            throw new BusinessException(ResultCode.VOUCHER_NOT_BALANCED);
        }
        v.setTotalDebit(td);
        v.setTotalCredit(tc);
    }

    private String generateVoucherNo(String year, Integer period) {
        return generateNextVoucherNo(year, period);
    }

    @Override
    public String generateNextVoucherNo(String year, Integer period) {
        String periodCode = CommonUtil.buildPeriodCode(year, period);
        long count = count(new LambdaQueryWrapper<GlVoucher>().eq(GlVoucher::getPeriodCode, periodCode));
        return String.format("%s-%02d-%04d", year, period, count + 1);
    }

    private void resolvePeriodCode(GlVoucher voucher) {
        if (StrUtil.isNotBlank(voucher.getPeriodCode())) {
            fillPeriodFields(voucher);
            return;
        }
        if (StrUtil.isNotBlank(voucher.getFiscalYear()) && voucher.getFiscalPeriod() != null) {
            voucher.setPeriodCode(CommonUtil.buildPeriodCode(voucher.getFiscalYear(), voucher.getFiscalPeriod()));
        }
    }

    private void fillPeriodFields(GlVoucher voucher) {
        if (StrUtil.isBlank(voucher.getPeriodCode()) || voucher.getPeriodCode().length() < 6) {
            return;
        }
        if (StrUtil.isBlank(voucher.getFiscalYear())) {
            voucher.setFiscalYear(voucher.getPeriodCode().substring(0, 4));
        }
        if (voucher.getFiscalPeriod() == null) {
            voucher.setFiscalPeriod(Integer.valueOf(voucher.getPeriodCode().substring(4, 6)));
        }
    }
}
