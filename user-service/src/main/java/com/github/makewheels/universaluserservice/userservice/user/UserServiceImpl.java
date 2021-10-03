package com.github.makewheels.universaluserservice.userservice.user;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.makewheels.universaluserservice.common.bean.Password;
import com.github.makewheels.universaluserservice.common.bean.User;
import com.github.makewheels.universaluserservice.userservice.redis.RedisKey;
import com.github.makewheels.universaluserservice.userservice.response.ErrorCode;
import com.github.makewheels.universaluserservice.userservice.response.Result;
import com.github.makewheels.universaluserservice.userservice.response.login.LoginResponse;
import com.github.makewheels.universaluserservice.userservice.util.PasswordUtil;
import com.github.makewheels.universaluserservice.userservice.util.SnowflakeUtil;
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
    @Resource
    private UserRepository userRepository;

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
        userRedisService.deleteLoginToken(RedisKey.loginToken(user.getLoginToken()));
        //生成新的loginToken
        String loginToken = RandomUtil.randomString(21);
        user.setLoginToken(loginToken);
        //更新数据库loginToken字段
        userRepository.updateByMongoId(user.getMongoId(), "loginToken", loginToken);
        //设置Redis的loginToken
        userRedisService.setLoginToken(loginToken, user);
        LoginResponse loginResponse = new LoginResponse();
        log.info("登陆成功: username = " + username + ", loginToken = " + loginToken);
        loginResponse.setLoginToken(loginToken);
        return Result.ok(loginResponse);
    }

    @Override
    public Boolean authLoginToken(String loginToken) {
        //如果Redis有token，直接通过
        boolean loginTokenExist = userRedisService.isLoginTokenExist(loginToken);
        if (loginTokenExist) {
            return true;
        }
        //如果没有，从数据库查询
        User user = userRepository.findOne("loginToken", loginToken);
        //如果没找到用户，那就说明没有这个loginToken，验证失败
        if (user == null) {
            return false;
        }
        //如果找到了这个用户，那说明只是Redis的缓存过期了，验证成功
        //缓存数据库中的loginToken到Redis中
        userRedisService.setLoginToken(loginToken, user);
        return true;
    }

    @Override
    public User getUserByLoginToken(String loginToken) {
        return userRedisService.getUserByLoginToken(loginToken);
    }
}
