package com.github.makewheels.universaluserservice.userservice.user;

import com.github.makewheels.universaluserservice.common.bean.User;
import com.github.makewheels.universaluserservice.userservice.response.Result;
import com.github.makewheels.universaluserservice.userservice.response.login.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("user")
@Slf4j
//@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
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
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PostMapping("authLoginToken")
    public Boolean authLoginToken(@RequestParam String loginToken) {
        return userService.authLoginToken(loginToken);
    }

    @PostMapping("getByLoginToken")
    public User getByLoginToken(@RequestParam String loginToken) {
        return userService.getUserByLoginToken(loginToken);
    }
}
