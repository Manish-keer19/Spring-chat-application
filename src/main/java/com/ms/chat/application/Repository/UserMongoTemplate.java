package com.ms.chat.application.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.ms.chat.application.Entity.userModel;


@Component
public class UserMongoTemplate {
  
    @Autowired
    private MongoTemplate mongoTemplate;


    public List<userModel> getUserThatHasEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<userModel> users = mongoTemplate.find(query, userModel.class);

        return users;
    }


    
}