package com.github.makewheels.universaluserservice.common.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Password {
    private Boolean isEncrypted;
    private String method;
    private String digest;

    private Date createTime;
    private Date updateTime;
}
