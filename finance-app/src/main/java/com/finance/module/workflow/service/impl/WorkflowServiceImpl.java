package com.finance.module.workflow.service.impl;

import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import com.finance.module.workflow.entity.WfInstance;
import com.finance.module.workflow.entity.WfTask;
import com.finance.module.workflow.mapper.WfInstanceMapper;
import com.finance.module.workflow.mapper.WfTaskMapper;
import com.finance.module.workflow.service.IWorkflowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkflowServiceImpl implements IWorkflowService {

    @Resource private WfInstanceMapper instanceMapper;
    @Resource private WfTaskMapper taskMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WfInstance start(String businessType, Long businessId, String businessNo,
                            Long initiatorId, String initiatorName, BigDecimal amount) {
        WfInstance inst = new WfInstance();
        inst.setFlowCode("WF-" + System.currentTimeMillis());
        inst.setFlowName(businessType + "审批");
        inst.setBusinessType(businessType);
        inst.setBusinessNo(businessNo);
        inst.setBusinessId(businessId);
        inst.setCurrentNode("一级审批");
        inst.setStatus(1);
        inst.setInitiator(initiatorId);
        inst.setInitiatorName(initiatorName);
        inst.setCreateTime(LocalDateTime.now());
        instanceMapper.insert(inst);

        // 根据金额决定层级：>10000 二级审批
        int levels = (amount != null && amount.compareTo(new BigDecimal("10000")) > 0) ? 2 : 1;
        inst.setCurrentLevel(1);

        for (int i = 1; i <= levels; i++) {
            WfTask t = new WfTask();
            t.setInstanceId(inst.getId());
            t.setFlowCode(inst.getFlowCode());
            t.setBusinessType(businessType);
            t.setBusinessNo(businessNo);
            t.setBusinessId(businessId);
            t.setLevel(i);
            t.setNodeName("L" + i + "审批人");
            t.setAssignee(2L);
            t.setAssigneeName("审批员" + i);
            t.setStatus("0");
            t.setCreateTime(LocalDateTime.now());
            taskMapper.insert(t);
        }
        return inst;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approve(Long taskId, Long userId, String userName, String opinion) {
        WfTask t = taskMapper.selectById(taskId);
        if (t == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"0".equals(t.getStatus())) throw new BusinessException("该任务已处理");
        t.setStatus("1");
        t.setOpinion(opinion);
        t.setHandleTime(LocalDateTime.now());
        taskMapper.updateById(t);

        WfInstance inst = instanceMapper.selectById(t.getInstanceId());
        List<WfTask> tasks = taskMapper.selectByInstance(inst.getId());
        boolean allDone = tasks.stream().allMatch(x -> "1".equals(x.getStatus()));
        if (allDone) {
            inst.setStatus(1);
            inst.setFinishTime(LocalDateTime.now());
            instanceMapper.updateById(inst);
        } else {
            int nextLevel = t.getLevel() + 1;
            inst.setCurrentLevel(nextLevel);
            inst.setCurrentNode("L" + nextLevel + "审批中");
            instanceMapper.updateById(inst);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reject(Long taskId, Long userId, String userName, String opinion) {
        WfTask t = taskMapper.selectById(taskId);
        if (t == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"0".equals(t.getStatus())) throw new BusinessException("该任务已处理");
        t.setStatus("2");
        t.setOpinion(opinion);
        t.setHandleTime(LocalDateTime.now());
        taskMapper.updateById(t);

        WfInstance inst = instanceMapper.selectById(t.getInstanceId());
        inst.setStatus(0);
        inst.setCurrentNode("已驳回");
        inst.setFinishTime(LocalDateTime.now());
        instanceMapper.updateById(inst);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean transfer(Long taskId, Long fromUserId, String fromUserName, Long toUserId, String toUserName, String opinion) {
        WfTask t = taskMapper.selectById(taskId);
        if (t == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        if (!"0".equals(t.getStatus())) throw new BusinessException("该任务已处理");
        t.setStatus("3");
        t.setOpinion(opinion);
        t.setHandleTime(LocalDateTime.now());
        taskMapper.updateById(t);

        WfTask newT = new WfTask();
        newT.setInstanceId(t.getInstanceId());
        newT.setFlowCode(t.getFlowCode());
        newT.setBusinessType(t.getBusinessType());
        newT.setBusinessNo(t.getBusinessNo());
        newT.setBusinessId(t.getBusinessId());
        newT.setLevel(t.getLevel());
        newT.setNodeName("L" + t.getLevel() + "审批(转交)");
        newT.setAssignee(toUserId);
        newT.setAssigneeName(toUserName);
        newT.setStatus("0");
        newT.setCreateTime(LocalDateTime.now());
        taskMapper.insert(newT);

        WfInstance inst = instanceMapper.selectById(t.getInstanceId());
        inst.setCurrentNode("L" + t.getLevel() + "转交审批中");
        instanceMapper.updateById(inst);
        return true;
    }

    @Override
    public List<WfTask> myTasks(Long userId) {
        return taskMapper.selectPendingByUser(userId);
    }

    @Override
    public Map<String, Object> instanceDetail(Long instanceId) {
        WfInstance inst = instanceMapper.selectById(instanceId);
        List<WfTask> tasks = taskMapper.selectByInstance(instanceId);
        Map<String, Object> map = new HashMap<>();
        map.put("instance", inst);
        map.put("tasks", tasks);
        return map;
    }
}
