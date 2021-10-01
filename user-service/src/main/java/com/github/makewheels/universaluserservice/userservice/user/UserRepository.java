package com.github.makewheels.universaluserservice.userservice.user;

import com.github.makewheels.universaluserservice.common.bean.User;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserRepository {
    @Resource
    private MongoTemplate mongoTemplate;

    public UpdateResult updateByMongoId(String mongoId, String key, Object value) {
        return mongoTemplate.updateFirst(
                Query.query(Criteria.where("mongoId").is(mongoId)),
                Update.update(key, value), User.class);
    }

    public User findOne(String key, Object value) {
        Query query = Query.query(Criteria.where(key).is(value));
        return mongoTemplate.findOne(query, User.class);
    }

}
