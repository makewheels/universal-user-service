package com.github.makewheels.universaluserservice;

import com.github.makewheels.universaluserservice.bean.User;
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
    public String login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }

}
