package com.github.makewheels.universaluserservice.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class User {
    @Id
    private String mongoId;
    private String appId;

    @Indexed
    private Long snowflakeId;
    private String username;
    private Password password;
    private String registerMethod;
    private Phone phone;
    private Date createTime;
    private String loginToken;

}
