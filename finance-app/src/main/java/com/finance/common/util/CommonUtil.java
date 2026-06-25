package com.finance.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.finance.common.constant.CommonConstants;
import com.finance.common.response.PageResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用工具
 */
public class CommonUtil {

    /**
     * MyBatis-Plus IPage 转换为统一分页结果
     */
    public static <T> PageResult<T> toPageResult(IPage<T> page) {
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }

    /**
     * MyBatis-Plus IPage<S> 转换为 PageResult<T>
     */
    public static <S, T> PageResult<T> toPageResult(IPage<S> page, java.util.function.Function<S, T> mapper) {
        List<T> records = page.getRecords().stream().map(mapper).collect(Collectors.toList());
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), records);
    }

    /**
     * 获取当前请求
     */
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    /**
     * 安全获取页码
     */
    public static long safePageNum(Long pageNum) {
        return pageNum == null || pageNum < 1 ? CommonConstants.DEFAULT_PAGE_NUM : pageNum;
    }

    /**
     * 安全获取页大小
     */
    public static long safePageSize(Long pageSize) {
        if (pageSize == null || pageSize < 1) return CommonConstants.DEFAULT_PAGE_SIZE;
        return Math.min(pageSize, CommonConstants.MAX_PAGE_SIZE);
    }

    /**
     * 字符串是否为空
     */
    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * 字符串是否非空
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /** 会计期间编码，如 202606 */
    public static String buildPeriodCode(String year, Integer period) {
        if (isEmpty(year) || period == null) {
            return null;
        }
        return year + String.format("%02d", period);
    }
}
