package com.github.makewheels.universaluserservice.userservice.user;

import com.alibaba.fastjson.JSON;
import com.github.makewheels.universaluserservice.common.bean.User;
import com.github.makewheels.universaluserservice.userservice.redis.RedisKey;
import com.github.makewheels.universaluserservice.userservice.redis.RedisService;
import com.github.makewheels.universaluserservice.userservice.redis.RedisTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserRedisService {
    @Resource
    private RedisService redisService;

    /**
     * 检查redis里是否有该loginToken
     *
     * @param loginToken
     * @return
     */
    public boolean isLoginTokenExist(String loginToken) {
        return redisService.hasKey(RedisKey.loginToken(loginToken));
    }

    public void setLoginToken(String loginToken, User user) {
        redisService.set(RedisKey.loginToken(loginToken), JSON.toJSONString(user),
                RedisTime.THIRTY_MINUTES);
    }

    public User getUserByLoginToken(String loginToken) {
        String json = (String) redisService.get(RedisKey.loginToken(loginToken));
        return JSON.parseObject(json, User.class);
    }

    public void deleteLoginToken(String loginToken) {
        if (loginToken == null) {
            return;
        }
        redisService.del(loginToken);
    }
}
