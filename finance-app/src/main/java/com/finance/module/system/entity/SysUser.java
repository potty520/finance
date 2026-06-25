package com.finance.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户
 */
@Data
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String realName;

    private String avatar;

    private String phone;

    private String email;

    private Integer gender;

    private Long deptId;

    private String deptName;

    private String userType;

    private Integer status;

    private LocalDateTime lastLogin;

    private String lastIp;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @JsonIgnore
    private Integer deleted;
}
