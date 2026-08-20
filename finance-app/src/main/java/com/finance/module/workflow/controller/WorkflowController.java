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
import com.finance.module.system.entity.SysUser;
import com.finance.module.system.mapper.SysUserMapper;
import com.finance.module.workflow.service.IWorkflowService;
import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Resource private SysUserMapper userMapper;

    @PostMapping("/start")
    public Result<WfInstance> start(@RequestBody Map<String, Object> body) {
        String businessType = (String) body.get("businessType");
        Long businessId = Long.valueOf(body.get("businessId").toString());
        String businessNo = (String) body.get("businessNo");
        SysUser currentUser = currentUser();
        Long initiatorId = currentUser.getId();
        String initiatorName = currentUser.getRealName();
        java.math.BigDecimal amount = body.get("amount") == null ? null
                : new java.math.BigDecimal(body.get("amount").toString());
        return Result.success(wfService.start(businessType, businessId, businessNo, initiatorId, initiatorName, amount));
    }

    @PostMapping("/approve")
    public Result<Boolean> approve(@RequestBody Map<String, Object> body) {
        Long taskId = Long.valueOf(body.get("taskId").toString());
        SysUser currentUser = currentUser();
        String opinion = (String) body.get("opinion");
        return Result.success(wfService.approve(taskId, currentUser.getId(), currentUser.getRealName(), opinion));
    }

    @PostMapping("/reject")
    public Result<Boolean> reject(@RequestBody Map<String, Object> body) {
        Long taskId = Long.valueOf(body.get("taskId").toString());
        SysUser currentUser = currentUser();
        String opinion = (String) body.get("opinion");
        return Result.success(wfService.reject(taskId, currentUser.getId(), currentUser.getRealName(), opinion));
    }

    @PostMapping("/transfer")
    public Result<Boolean> transfer(@RequestBody Map<String, Object> body) {
        Long taskId = Long.valueOf(body.get("taskId").toString());
        SysUser currentUser = currentUser();
        Long toUserId = Long.valueOf(body.get("toUserId").toString());
        SysUser targetUser = userMapper.selectById(toUserId);
        if (targetUser == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        String opinion = (String) body.get("opinion");
        return Result.success(wfService.transfer(taskId, currentUser.getId(), currentUser.getRealName(),
                toUserId, targetUser.getRealName(), opinion));
    }

    @GetMapping("/myTasks")
    public Result<List<WfTask>> myTasks(@RequestParam(required = false) Long userId) {
        return Result.success(wfService.myTasks(currentUser().getId()));
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

    private SysUser currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        SysUser user = userMapper.selectByUsername(auth.getName());
        if (user == null || user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return user;
    }
}
