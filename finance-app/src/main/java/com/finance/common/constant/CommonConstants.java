package com.finance.common.constant;

/**
 * 公共常量
 */
public class CommonConstants {

    /** 默认页码 */
    public static final long DEFAULT_PAGE_NUM = 1L;
    /** 默认页大小 */
    public static final long DEFAULT_PAGE_SIZE = 10L;
    /** 最大页大小 */
    public static final long MAX_PAGE_SIZE = 500L;

    /** 逻辑删除 - 未删除 */
    public static final int NOT_DELETED = 0;
    /** 逻辑删除 - 已删除 */
    public static final int DELETED = 1;

    /** 状态 - 禁用 */
    public static final int STATUS_DISABLED = 0;
    /** 状态 - 启用 */
    public static final int STATUS_ENABLED = 1;

    /** 树形根节点父ID */
    public static final Long ROOT_PARENT_ID = 0L;

    /** 系统默认租户 */
    public static final Long DEFAULT_TENANT_ID = 1L;

    private CommonConstants() {}
}
