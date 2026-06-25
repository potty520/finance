package com.finance.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页响应数据
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private Long total;

    /** 当前页数据 */
    private List<T> records;

    /** 当前页码 */
    private Long current;

    /** 每页大小 */
    private Long size;

    /** 总页数 */
    private Long pages;

    public PageResult() {
        this.records = Collections.emptyList();
    }

    public PageResult(Long current, Long size, Long total, List<T> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = records == null ? Collections.emptyList() : records;
        this.pages = (total + size - 1) / size;
    }

    public static <T> PageResult<T> of(Long current, Long size, Long total, List<T> records) {
        return new PageResult<>(current, size, total, records);
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(1L, 10L, 0L, Collections.emptyList());
    }
}
