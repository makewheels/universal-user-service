package com.github.makewheels.universaluserservice.userservice.user;

import com.github.makewheels.universaluserservice.common.bean.User;
import com.github.makewheels.universaluserservice.userservice.response.Result;
import com.github.makewheels.universaluserservice.userservice.response.login.LoginResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("createEmpty")
    public User createUser(@RequestParam String appId) {
        return userService.createUser(appId);
    }

    @PostMapping("createByUsernameAndPassword")
    public User createUser(@RequestParam String appId, @RequestParam String username,
                           @RequestParam String password) {
        return userService.createUser(appId, username, password);
    }

    @PostMapping("getUserByMongoId")
    public User getUserByMongoId(@RequestParam String mongoId) {
        return userService.getUserByMongoId(mongoId);
    }

    @PostMapping("getUserBySnowflakeId")
    public User getUserBySnowflakeId(@RequestParam long snowflakeId) {
        return userService.getUserBySnowflakeId(snowflakeId);
    }

    @PostMapping("login")
    public Result<LoginResponse> login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }

    @PostMapping("authLoginToken")
    public Boolean authLoginToken(@RequestParam String loginToken) {
        return userService.authLoginToken(loginToken);
    }

}
