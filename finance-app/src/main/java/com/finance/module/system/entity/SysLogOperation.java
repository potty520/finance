package com.finance.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_log_operation")
public class SysLogOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String module;

    private String action;

    private String method;

    private String requestUrl;

    private String requestData;

    private String response;

    private Long userId;

    private String username;

    private String ip;

    private Long costTime;

    private Integer status;

    private String errorMsg;

    private LocalDateTime createTime;
}
