package com.finance.common.response;

/**
 * 响应状态码枚举
 * 不依赖 Lombok，手写构造/getter，避免 IDE 配置问题
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "系统异常"),

    /* 1xxx 客户端错误 */
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),

    /* 11xx 认证模块 */
    LOGIN_ERROR(1101, "用户名或密码错误"),
    USER_DISABLED(1102, "账号已被禁用"),
    USER_NOT_EXIST(1103, "用户不存在"),
    TOKEN_INVALID(1104, "Token 无效"),
    TOKEN_EXPIRED(1105, "Token 已过期"),

    /* 12xx 业务通用 */
    DATA_NOT_FOUND(1201, "数据不存在"),
    DATA_EXISTED(1202, "数据已存在"),
    DATA_LOCKED(1203, "数据已被锁定"),
    OPERATION_FAILED(1204, "操作失败"),
    PERIOD_CLOSED(1205, "当前期间已结账"),
    PERIOD_NOT_OPEN(1206, "当前期间未开启"),
    UNBALANCED_ENTRY(1207, "分录借贷不平衡"),

    /* 13xx 业务模块 */
    VOUCHER_NOT_BALANCED(1301, "凭证借贷不平衡"),
    VOUCHER_ALREADY_POSTED(1302, "凭证已过账，无法再次操作"),
    VOUCHER_ALREADY_APPROVED(1303, "凭证已审核"),
    INSUFFICIENT_BUDGET(1304, "预算不足"),
    INSUFFICIENT_STOCK(1305, "库存不足"),
    ASSET_DEPRECIATED(1306, "资产已计提折旧，无法变更"),
    AMORTIZATION_DONE(1307, "摊销已完成"),
    RECONCILIATION_DONE(1308, "已对账，无法再次操作");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
