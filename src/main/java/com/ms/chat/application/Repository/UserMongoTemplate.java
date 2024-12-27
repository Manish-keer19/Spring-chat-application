package com.ms.chat.application.Repository;

import java.util.List;

import com.ms.chat.application.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


@Component
public class UserMongoTemplate {
  
    @Autowired
    private MongoTemplate mongoTemplate;


    public List<User> getUserThatHasEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<User> users = mongoTemplate.find(query, User.class);

        return users;
    }


    
}