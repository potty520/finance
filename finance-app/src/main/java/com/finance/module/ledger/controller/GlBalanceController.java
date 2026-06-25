package com.finance.module.ledger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.ledger.entity.GlBalance;
import com.finance.module.ledger.entity.GlSubject;
import com.finance.module.ledger.mapper.GlBalanceMapper;
import com.finance.module.ledger.mapper.GlSubjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 总账余额表
 */
@RestController
@RequestMapping("/ledger/balance")
public class GlBalanceController {

    @Resource
    private GlBalanceMapper balanceMapper;

    @Resource
    private GlSubjectMapper subjectMapper;

    @GetMapping("/list")
    public Result<List<GlBalance>> list(@RequestParam String fiscalYear, @RequestParam Integer fiscalPeriod) {
        List<GlBalance> balances = balanceMapper.selectByPeriod(fiscalYear, fiscalPeriod);
        fillSubjectName(balances);
        return Result.success(balances);
    }

    @GetMapping("/summary")
    public Result<List<Map<String, Object>>> summary(@RequestParam String fiscalYear, @RequestParam Integer fiscalPeriod) {
        return Result.success(balanceMapper.sumBySubjectType(fiscalYear, fiscalPeriod));
    }

    @GetMapping("/bySubject")
    public Result<List<GlBalance>> bySubject(@RequestParam String fiscalYear,
                                             @RequestParam Integer fiscalPeriod,
                                             @RequestParam String subjectCode) {
        LambdaQueryWrapper<GlBalance> qw = new LambdaQueryWrapper<>();
        qw.eq(GlBalance::getPeriodCode, CommonUtil.buildPeriodCode(fiscalYear, fiscalPeriod))
                .likeRight(GlBalance::getSubjectCode, subjectCode)
                .orderByAsc(GlBalance::getSubjectCode);
        List<GlBalance> balances = balanceMapper.selectList(qw);
        fillSubjectName(balances);
        return Result.success(balances);
    }

    private void fillSubjectName(List<GlBalance> balances) {
        if (balances == null || balances.isEmpty()) return;
        List<GlSubject> subjects = subjectMapper.selectList(null);
        Map<String, String> nameMap = subjects.stream()
                .collect(Collectors.toMap(GlSubject::getSubjectCode, GlSubject::getSubjectName, (a, b) -> a));
        for (GlBalance b : balances) {
            String name = nameMap.get(b.getSubjectCode());
            if (name != null) b.setSubjectName(name);
        }
    }
}
