package com.github.makewheels.universaluserservice.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Phone {
    private String areaCode;
    private String number;
    private Boolean isVerified;
    private Date createTime;
    private Date verifyTime;
}
