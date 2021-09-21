package com.github.makewheels.universaluserservice;

import com.github.makewheels.universaluserservice.bean.User;
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

    @RequestMapping("getUser")
    public User getUser(@RequestParam String userId) {
        User user = new User();
        user.setMongoId("com.github.makewheels.universaluserservice.UserController=" + userId);
        user.setUserName(UUID.randomUUID().toString());
        return user;
    }

    /**
     * 创建空用户
     *
     * @return
     */
    @PostMapping("createEmpty")
    public User createEmpty(@RequestParam String appId) {
        return userService.createUser(appId);
    }

    /**
     * 创建空用户
     *
     * @return
     */
    @PostMapping("createByUsernameAndPassword")
    public User createByUsernameAndPassword(@RequestParam String appId, @RequestParam String username,
                           @RequestParam String password) {
        return userService.createUser(appId, username, password);
    }
}
