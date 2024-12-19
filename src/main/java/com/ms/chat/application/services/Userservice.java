package com.ms.chat.application.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ms.chat.application.Entity.userModel;

@Service
public class Userservice {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public userModel SaveUser(userModel user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Arrays.asList("USER")); // Wrapping "USER" in a List
        // userModel dbuser = userRepository.save(user);
        userModel dbuser = mongoTemplate.save(user);
        return dbuser;
    }

}
