package com.github.makewheels.universaluserservice;

import com.github.makewheels.universaluserservice.bean.Password;
import com.github.makewheels.universaluserservice.bean.User;
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
        user.setUserName(username);

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
    public User getUserBySnowflakeId(String snowflakeId) {
        Query query = Query.query(Criteria.where("snowflakeId").is(snowflakeId));
        return mongoTemplate.findOne(query, User.class);
    }
}
