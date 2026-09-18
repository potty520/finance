package com.finance.module.workflow.service.impl;

import com.finance.common.exception.BusinessException;
import com.finance.common.response.ResultCode;
import com.finance.module.workflow.entity.WfInstance;
import com.finance.module.workflow.entity.WfTask;
import com.finance.module.workflow.mapper.WfInstanceMapper;
import com.finance.module.workflow.mapper.WfTaskMapper;
import com.finance.module.workflow.service.IWorkflowService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 审批流实现。
 *
 * 实例状态口径与前端 Instance.vue 一致：0 = 审批中，1 = 已通过，2 = 已驳回。
 * 任务状态口径：0 = 待处理，1 = 已同意，2 = 已驳回，3 = 已转交。
 */
@Service
public class WorkflowServiceImpl implements IWorkflowService {

    /** 超过该金额的单据需要二级审批 */
    private static final BigDecimal SECOND_LEVEL_THRESHOLD = new BigDecimal("10000");

    @Resource private WfInstanceMapper instanceMapper;
    @Resource private WfTaskMapper taskMapper;
    @Resource private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WfInstance start(String businessType, Long businessId, String businessNo,
                            Long initiatorId, String initiatorName, BigDecimal amount) {
        String docType = (businessType != null && !businessType.trim().isEmpty()) ? businessType : "common";
        List<long[]> approvers = resolveApprovers(docType);

        WfInstance inst = new WfInstance();
        inst.setDocType(docType);
        inst.setDocTypeName(docTypeName(docType));
        inst.setFlowCode("WF-" + System.currentTimeMillis());
        inst.setFlowName(docTypeName(docType));
        inst.setBusinessType(businessType);
        inst.setBusinessNo(businessNo);
        inst.setBusinessId(businessId);
        inst.setCurrentNode("L1审批中");
        inst.setStatus(0);
        inst.setInitiator(initiatorId);
        inst.setInitiatorName(initiatorName);
        inst.setCurrentLevel(1);
        inst.setCreateTime(LocalDateTime.now());
        instanceMapper.insert(inst);

        // 金额超过阈值追加一级审批；审批人不足时复用配置的最后一位
        int levels = (amount != null && amount.compareTo(SECOND_LEVEL_THRESHOLD) > 0) ? 2 : 1;

        for (int i = 1; i <= levels; i++) {
            long[] approver = approvers.get(Math.min(i - 1, approvers.size() - 1));
            String approverName = approverName(approver);
            WfTask t = new WfTask();
            t.setDocType(docType);
            t.setDocId(businessId);
            t.setDocNo(businessNo);
            t.setStepNo(i);
            t.setSubmitterId(initiatorId);
            t.setSubmitterName(initiatorName);
            t.setApproverId(approver[0]);
            t.setApproverName(approverName);
            t.setInstanceId(inst.getId());
            t.setFlowCode(inst.getFlowCode());
            t.setBusinessType(businessType);
            t.setBusinessNo(businessNo);
            t.setBusinessId(businessId);
            t.setLevel(i);
            t.setNodeName("L" + i + "审批人");
            t.setAssignee(approver[0]);
            t.setAssigneeName(approverName);
            t.setStatus("0");
            t.setCreateTime(LocalDateTime.now());
            taskMapper.insert(t);
        }
        return inst;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approve(Long taskId, Long userId, String userName, String opinion) {
        WfTask t = loadPendingTask(taskId, userId);
        t.setStatus("1");
        t.setOpinion(opinion);
        t.setHandleTime(LocalDateTime.now());
        taskMapper.updateById(t);

        WfInstance inst = instanceMapper.selectById(t.getInstanceId());
        List<WfTask> tasks = taskMapper.selectByInstance(inst.getId());
        boolean allDone = tasks.stream().allMatch(x -> "1".equals(x.getStatus()));
        if (allDone) {
            inst.setStatus(1);
            inst.setCurrentNode("已通过");
            inst.setFinishTime(LocalDateTime.now());
            instanceMapper.updateById(inst);
            writeBackBusinessStatus(t, "1");
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
        WfTask t = loadPendingTask(taskId, userId);
        t.setStatus("2");
        t.setOpinion(opinion);
        t.setHandleTime(LocalDateTime.now());
        taskMapper.updateById(t);

        WfInstance inst = instanceMapper.selectById(t.getInstanceId());
        inst.setStatus(2);
        inst.setCurrentNode("已驳回");
        inst.setFinishTime(LocalDateTime.now());
        instanceMapper.updateById(inst);
        writeBackBusinessStatus(t, "2");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean transfer(Long taskId, Long fromUserId, String fromUserName, Long toUserId, String toUserName, String opinion) {
        WfTask t = loadPendingTask(taskId, fromUserId);
        t.setStatus("3");
        t.setOpinion(opinion);
        t.setHandleTime(LocalDateTime.now());
        taskMapper.updateById(t);

        WfTask newT = new WfTask();
        newT.setDocType(t.getDocType());
        newT.setDocId(t.getDocId());
        newT.setDocNo(t.getDocNo());
        newT.setStepNo(t.getStepNo());
        newT.setInstanceId(t.getInstanceId());
        newT.setFlowCode(t.getFlowCode());
        newT.setBusinessType(t.getBusinessType());
        newT.setBusinessNo(t.getBusinessNo());
        newT.setBusinessId(t.getBusinessId());
        newT.setLevel(t.getLevel());
        newT.setNodeName("L" + t.getLevel() + "审批(转交)");
        newT.setAssignee(toUserId);
        newT.setAssigneeName(toUserName);
        newT.setApproverId(toUserId);
        newT.setApproverName(toUserName);
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

    private WfTask loadPendingTask(Long taskId, Long userId) {
        WfTask t = taskMapper.selectById(taskId);
        if (t == null) throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        assertAssignee(t, userId);
        if (!"0".equals(t.getStatus())) throw new BusinessException("该任务已处理");
        return t;
    }

    /**
     * 审批人取自 wf_approval_config（doc_type -> approver_ids 逗号分隔）；
     * 未配置时回落到管理员账号，避免任务无人可审。
     */
    private List<long[]> resolveApprovers(String docType) {
        List<long[]> result = new ArrayList<>();
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                // flow_code IS NULL 用于区分"审批人配置行"与复用同表存放的流程实例行
                "SELECT approver_ids FROM wf_approval_config " +
                "WHERE doc_type = ? AND deleted = 0 AND status = 1 AND flow_code IS NULL " +
                "ORDER BY id LIMIT 1", docType);
            if (!rows.isEmpty()) {
                Object ids = rows.get(0).get("approver_ids");
                if (ids != null) {
                    for (String part : String.valueOf(ids).split(",")) {
                        String s = part.trim();
                        if (s.isEmpty()) continue;
                        try {
                            result.add(new long[]{Long.parseLong(s)});
                        } catch (NumberFormatException ignore) {
                            // 跳过非法配置项
                        }
                    }
                }
            }
        } catch (Exception ignore) {
            // 配置表缺失时走兜底
        }
        if (result.isEmpty()) {
            result.add(new long[]{1L});
        }
        return result;
    }

    /** 审批人姓名从 sys_user 补齐，查不到时退化为 审批人{id} */
    private String approverName(long[] approver) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT real_name, username FROM sys_user WHERE id = ? AND deleted = 0 LIMIT 1", approver[0]);
            if (!rows.isEmpty()) {
                Object real = rows.get(0).get("real_name");
                Object uname = rows.get(0).get("username");
                if (real != null && !String.valueOf(real).trim().isEmpty()) return String.valueOf(real);
                if (uname != null && !String.valueOf(uname).trim().isEmpty()) return String.valueOf(uname);
            }
        } catch (Exception ignore) {
            // 忽略查询异常
        }
        return "审批人" + approver[0];
    }

    private String docTypeName(String docType) {
        if ("expense".equals(docType)) return "费用报销审批";
        if ("purchase".equals(docType)) return "采购付款审批";
        if ("receipt".equals(docType)) return "收款审批";
        if ("voucher".equals(docType)) return "凭证审批";
        return docType + "审批";
    }

    /** 审批结束后回写业务单据状态，避免单据与流程状态不一致 */
    private void writeBackBusinessStatus(WfTask task, String status) {
        if (task.getBusinessId() == null) return;
        String type = task.getBusinessType() != null ? task.getBusinessType() : task.getDocType();
        if ("expense".equals(type)) {
            jdbcTemplate.update(
                "UPDATE exp_application SET status = ?, update_time = NOW() WHERE id = ? AND deleted = 0",
                status, task.getBusinessId());
        }
    }

    private void assertAssignee(WfTask task, Long userId) {
        if (userId == null || (!Objects.equals(task.getAssignee(), userId)
                && !Objects.equals(task.getApproverId(), userId))) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}
