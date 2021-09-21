package com.github.makewheels.universaluserservice;

import com.github.makewheels.universaluserservice.bean.User;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

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
    public User getUserBySnowflakeId(@RequestParam String snowflakeId) {
        return userService.getUserBySnowflakeId(snowflakeId);
    }
}
