package com.github.makewheels.universaluserservice;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.github.makewheels.universaluserservice.bean.Password;
import com.github.makewheels.universaluserservice.bean.User;
import com.github.makewheels.universaluserservice.redis.RedisKey;
import com.github.makewheels.universaluserservice.redis.RedisService;
import com.github.makewheels.universaluserservice.redis.RedisTime;
import com.github.makewheels.universaluserservice.util.PasswordUtil;
import com.github.makewheels.universaluserservice.util.SnowflakeUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private RedisService redisService;

    /**
     * 内部调用方法，生成基本用户
     *
     * @return
     */
    private User createBasicUser(String appId) {
        User user = new User();
        user.setAppId(appId);
        user.setSnowflakeId(SnowflakeUtil.getId());
        user.setCreateTime(new Date());
        return user;
    }

    @Override
    public User createUser(String appId) {
        User user = createBasicUser(appId);
        mongoTemplate.save(user);
        return user;
    }

    @Override
    public User createUser(String appId, String username, String passwordText) {
        User user = createBasicUser(appId);
        user.setUsername(username);

        Password password = new Password();

        password.setIsEncrypted(true);
        password.setDigest(PasswordUtil.encrypt(passwordText));

        Date createTime = new Date();
        password.setCreateTime(createTime);
        password.setUpdateTime(createTime);
        password.setMethod("md5");
        user.setPassword(password);

        mongoTemplate.save(user);
        return user;
    }

    @Override
    public User getUserByMongoId(String mongoId) {
        System.out.println("UserServiceImpl.getUserByMongoId");
        return mongoTemplate.findById(mongoId, User.class);
    }

    @Override
    public User getUserBySnowflakeId(long snowflakeId) {
        Query query = Query.query(Criteria.where("snowflakeId").is(snowflakeId));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public String login(String username, String password) {
        //数据库校验
        String passwordHash = PasswordUtil.encrypt(password);
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        query.addCriteria(Criteria.where("password.digest").is(passwordHash));
        User user = mongoTemplate.findOne(query, User.class);
        if (user == null) {
            return null;
        }
        String loginToken = IdUtil.simpleUUID();
        System.out.println(user);
        redisService.set(RedisKey.loginToken(loginToken), JSON.toJSONString(user), RedisTime.ONE_HOUR);
        return loginToken;
    }

    @Override
    public Boolean authLoginToken(String authLoginToken) {
        //判断loginToken

        return null;
    }
}
