package com.finance.module.workflow.service;

import com.finance.module.workflow.entity.WfInstance;
import com.finance.module.workflow.entity.WfTask;

import java.util.List;
import java.util.Map;

public interface IWorkflowService {
    /**
     * 启动流程
     * @param businessType 业务类型 voucher/contract/expense/budget
     * @param businessId 业务 ID
     * @param businessNo 业务单号
     * @param initiatorId 发起人
     * @param initiatorName 发起人名称
     * @param amount 金额（用于金额阈值决定审批层级）
     */
    WfInstance start(String businessType, Long businessId, String businessNo,
                     Long initiatorId, String initiatorName, java.math.BigDecimal amount);

    /**
     * 通过
     */
    boolean approve(Long taskId, Long userId, String userName, String opinion);

    /**
     * 驳回
     */
    boolean reject(Long taskId, Long userId, String userName, String opinion);

    /**
     * 转交
     */
    boolean transfer(Long taskId, Long fromUserId, String fromUserName, Long toUserId, String toUserName, String opinion);

    List<WfTask> myTasks(Long userId);

    Map<String, Object> instanceDetail(Long instanceId);
}
