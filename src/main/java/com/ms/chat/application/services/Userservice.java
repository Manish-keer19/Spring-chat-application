package com.ms.chat.application.services;

import java.util.Arrays;
import java.util.List;


import com.ms.chat.application.Entity.User;
import com.ms.chat.application.Entity.UserAdditionalDetail;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Userservice {

    @Autowired
    private MongoTemplate mongoTemplate;


    public  void  saveUser(User user){
        mongoTemplate.save(user);
    }

    public User findByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        User user = mongoTemplate.findOne(query, User.class);

        return  user;
    }



    public User findByUsername(String userName){
        Query query = new Query();

        query.addCriteria(Criteria.where("userName").is(userName));

      return  mongoTemplate.findOne(query,User.class);

    }

    public  User findById(ObjectId id){
        Query query = new Query();

        query.addCriteria(Criteria.where("_id").is(id));
        return  mongoTemplate.findOne(query,User.class);
    }

     public List<User> getAllUserData(){
        return  mongoTemplate.findAll(User.class);
     }


}
