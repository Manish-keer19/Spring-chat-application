package com.ms.chat.application.services;

import com.ms.chat.application.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AuthService {
    @Autowired
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private MongoTemplate mongoTemplate;

    public void SignIn(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Arrays.asList("USER")); // Wrapping "USER" in a List
        // Dynamically set profile picture based on the user's name
        String profilePicUrl = "https://ui-avatars.com/api/?name=" +
                user.getUserName().replaceAll(" ", "+") +
                "&background=random";
        user.setProfilePic(profilePicUrl);
        User dbuser = mongoTemplate.save(user);
//        return dbuser;

}

}


