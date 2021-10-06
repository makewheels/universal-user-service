package com.github.makewheels.universaluserservice.userservice.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
