package com.ms.chat.application.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ms.chat.application.Entity.userModel;

public interface UserRepository extends MongoRepository<userModel,ObjectId>  {
    userModel findByuserName(String userName);
    userModel deleteByuserName(String userName);
    
}
