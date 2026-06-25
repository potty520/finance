package com.finance.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解 - 用于 Controller 方法上做细粒度权限控制
 *
 * 用法: @PreAuthorize("hasAuthority('system:user:add')")
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {

    /** 权限标识，如 system:user:add */
    String value() default "";

    /** 角色，可以是多个（任一即可） */
    String[] roles() default {};

    /** 描述 */
    String desc() default "";
}
