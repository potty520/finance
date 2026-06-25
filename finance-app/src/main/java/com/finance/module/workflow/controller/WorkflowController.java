package com.finance.module.workflow.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.finance.common.response.PageResult;
import com.finance.common.response.Result;
import com.finance.common.util.CommonUtil;
import com.finance.module.workflow.entity.WfInstance;
import com.finance.module.workflow.entity.WfTask;
import com.finance.module.workflow.mapper.WfInstanceMapper;
import com.finance.module.workflow.mapper.WfTaskMapper;
import com.finance.module.workflow.service.IWorkflowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    @Resource private IWorkflowService wfService;
    @Resource private WfInstanceMapper instanceMapper;
    @Resource private WfTaskMapper taskMapper;

    @PostMapping("/start")
    public Result<WfInstance> start(@RequestBody Map<String, Object> body) {
        String businessType = (String) body.get("businessType");
        Long businessId = Long.valueOf(body.get("businessId").toString());
        String businessNo = (String) body.get("businessNo");
        Long initiatorId = Long.valueOf(body.get("initiatorId").toString());
        String initiatorName = (String) body.get("initiatorName");
        java.math.BigDecimal amount = body.get("amount") == null ? null
                : new java.math.BigDecimal(body.get("amount").toString());
        return Result.success(wfService.start(businessType, businessId, businessNo, initiatorId, initiatorName, amount));
    }

    @PostMapping("/approve")
    public Result<Boolean> approve(@RequestBody Map<String, Object> body) {
        Long taskId = Long.valueOf(body.get("taskId").toString());
        Long userId = Long.valueOf(body.get("userId").toString());
        String userName = (String) body.get("userName");
        String opinion = (String) body.get("opinion");
        return Result.success(wfService.approve(taskId, userId, userName, opinion));
    }

    @PostMapping("/reject")
    public Result<Boolean> reject(@RequestBody Map<String, Object> body) {
        Long taskId = Long.valueOf(body.get("taskId").toString());
        Long userId = Long.valueOf(body.get("userId").toString());
        String userName = (String) body.get("userName");
        String opinion = (String) body.get("opinion");
        return Result.success(wfService.reject(taskId, userId, userName, opinion));
    }

    @PostMapping("/transfer")
    public Result<Boolean> transfer(@RequestBody Map<String, Object> body) {
        Long taskId = Long.valueOf(body.get("taskId").toString());
        Long fromUserId = Long.valueOf(body.get("fromUserId").toString());
        String fromUserName = (String) body.get("fromUserName");
        Long toUserId = Long.valueOf(body.get("toUserId").toString());
        String toUserName = (String) body.get("toUserName");
        String opinion = (String) body.get("opinion");
        return Result.success(wfService.transfer(taskId, fromUserId, fromUserName, toUserId, toUserName, opinion));
    }

    @GetMapping("/myTasks")
    public Result<List<WfTask>> myTasks(@RequestParam Long userId) {
        return Result.success(wfService.myTasks(userId));
    }

    @GetMapping("/detail/{instanceId}")
    public Result<Map<String, Object>> detail(@PathVariable Long instanceId) {
        return Result.success(wfService.instanceDetail(instanceId));
    }

    @GetMapping("/instance/page")
    public Result<PageResult<WfInstance>> instancePage(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize) {
        Page<WfInstance> p = instanceMapper.selectPage(
                new Page<>(CommonUtil.safePageNum(pageNum), CommonUtil.safePageSize(pageSize)),
                new LambdaQueryWrapper<WfInstance>().orderByDesc(WfInstance::getCreateTime));
        return Result.success(CommonUtil.toPageResult(p));
    }
}
