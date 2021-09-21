package com.github.makewheels.universaluserservice;

import cn.hutool.core.util.RandomUtil;
import com.github.makewheels.universaluserservice.bean.Password;
import com.github.makewheels.universaluserservice.bean.User;
import com.github.makewheels.universaluserservice.util.PasswordUtil;
import com.github.makewheels.universaluserservice.util.SnowflakeUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
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
        user.setCreateDate(new Date());
        return user;
    }

    @Override
    public User createUser(String appId) {
        User user = createBasicUser(appId);
        mongoTemplate.save(user);
        return user;
    }

    @Override
    public User createUser(String appId, String username, String password) {
        User user = createBasicUser(appId);
        user.setUserName(username);

        Password passwordObject = new Password();
        Date createTime = new Date();
        String salt = RandomUtil.randomString(RandomUtil.randomInt(13, 29));

        passwordObject.setIsEncrypted(true);
        passwordObject.setSalt(salt);
        passwordObject.setDigest(PasswordUtil.encrypt(password, salt));

        passwordObject.setCreateTime(createTime);
        passwordObject.setUpdateTime(createTime);
        passwordObject.setMethod("md5-salt");
        user.setPassword(passwordObject);

        mongoTemplate.save(user);
        return user;
    }
}
