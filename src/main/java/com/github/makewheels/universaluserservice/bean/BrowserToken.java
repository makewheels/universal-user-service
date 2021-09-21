package com.github.makewheels.universaluserservice.bean;

import lombok.Data;

import java.util.Date;

@Data
public class BrowserToken {
    private String value;
    private Date expireAt;
}
