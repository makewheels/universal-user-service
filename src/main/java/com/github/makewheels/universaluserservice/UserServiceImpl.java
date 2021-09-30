package com.github.makewheels.universaluserservice;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.github.makewheels.universaluserservice.bean.Password;
import com.github.makewheels.universaluserservice.bean.User;
import com.github.makewheels.universaluserservice.redis.RedisKey;
import com.github.makewheels.universaluserservice.redis.RedisService;
import com.github.makewheels.universaluserservice.redis.RedisTime;
import com.github.makewheels.universaluserservice.response.ErrorCode;
import com.github.makewheels.universaluserservice.response.Result;
import com.github.makewheels.universaluserservice.response.login.LoginResponse;
import com.github.makewheels.universaluserservice.util.PasswordUtil;
import com.github.makewheels.universaluserservice.util.SnowflakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private UserRedisService userRedisService;

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

    /**
     * 登录
     */
    @Override
    public Result<LoginResponse> login(String username, String password) {
        //根据用户名密码查数据库
        String passwordHash = PasswordUtil.encrypt(password);
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        query.addCriteria(Criteria.where("password.digest").is(passwordHash));
        User user = mongoTemplate.findOne(query, User.class);
        if (user == null) {
            log.info("登录失败, username = " + username);
            return Result.error(ErrorCode.LOGIN_LOGIN_NAME_PASSWORD_WRONG);
        }
        //把Redis里之前的loginToken干掉
        userRedisService.deleteLoginToken(user.getLoginToken());
        //生成新的loginToken
        String loginToken = IdUtil.simpleUUID();
        user.setLoginToken(loginToken);
        //更新数据库loginToken字段
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("mongoId").is(user.getMongoId())),
                Update.update("loginToken", loginToken), User.class);
        //设置Redis的loginToken
        userRedisService.setLoginToken(loginToken, user);
        LoginResponse loginResponse = new LoginResponse();
        return Result.ok(loginResponse);
    }

    @Override
    public Boolean authLoginToken(String authLoginToken) {
        //如果Redis有token，直接通过
        boolean loginTokenExist = userRedisService.isLoginTokenExist(authLoginToken);
        if (loginTokenExist) {
            return true;
        }
        return null;
    }

    @Override
    public Result<User> getUserByLoginToken(String loginToken) {
        User user = userRedisService.getUserByLoginToken(loginToken);
        if (user != null) {
            return Result.ok(user);
        } else {
            return Result.error(ErrorCode.CHECK_LOGIN_TOKEN_ERROR);
        }
    }
}
