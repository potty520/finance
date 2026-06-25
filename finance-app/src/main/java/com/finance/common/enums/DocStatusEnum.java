package com.finance.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 单据状态枚举
 */
@Getter
@AllArgsConstructor
public enum DocStatusEnum {

    DRAFT("DRAFT", "草稿"),
    PENDING("PENDING", "待审核"),
    APPROVED("APPROVED", "已审核"),
    REJECTED("REJECTED", "已驳回"),
    POSTED("POSTED", "已过账"),
    CANCELLED("CANCELLED", "已作废"),
    CLOSED("CLOSED", "已关闭");

    private final String code;
    private final String desc;

    public static DocStatusEnum of(String code) {
        for (DocStatusEnum e : values()) {
            if (e.code.equals(code)) return e;
        }
        return null;
    }
}
