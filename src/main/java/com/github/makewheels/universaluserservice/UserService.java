package com.github.makewheels.universaluserservice;

import com.github.makewheels.universaluserservice.bean.User;

public interface UserService {
    /**
     * 创建空用户
     *
     * @param appId
     * @return
     */
    User createUser(String appId);

    /**
     * 创建用户：根据用户名密码
     *
     * @param appId
     * @param username
     * @param password
     * @return
     */
    User createUser(String appId, String username, String password);
}
