package com.ms.chat.application.services;

import com.ms.chat.application.Entity.User;
import com.ms.chat.application.Response.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
public class AuthService {
    @Autowired
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private  FileUploadService fileUploadService;

    public Boolean signUp(User user, MultipartFile file) throws Exception {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Arrays.asList("USER")); // Wrapping "USER" in a List

        if (file != null && !file.isEmpty()) {
            FileUploadResponse fileUploadResponse = fileUploadService.uploadFile(file,"profilePic");
            if (fileUploadResponse == null) {
                throw new Exception("could not save the file in cloudinary");
            }
            user.setProfilePic(fileUploadResponse.getSecureUrl());
        }
        else {
            // Dynamically set profile picture based on the user's name
            String profilePicUrl = "https://ui-avatars.com/api/?name=" +
                    user.getUserName().replaceAll(" ", "+") +
                    "&background=random";
            user.setProfilePic(profilePicUrl);

        }
        User dbuser = mongoTemplate.save(user);
//        return dbuser;
        if (dbuser!=null){
            return true;
        }
        return false;

}

}


