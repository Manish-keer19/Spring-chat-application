package com.ms.chat.application.Repository;

import com.ms.chat.application.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,ObjectId>  {
    User findByuserName(String userName);
    User deleteByuserName(String userName);
    Optional<User> findByEmail(String email);

}


