package com.ms.chat.application.services;

import com.ms.chat.application.Entity.User;
import com.ms.chat.application.Entity.UserAdditionalDetail;
import com.ms.chat.application.Response.FileUploadResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@Service
public class Profileservice {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private  Userservice userservice;

    @Autowired
    private  FileUploadService fileUploadService;
    public User saveUserAdditionDetailAndfile(UserAdditionalDetail userAdditionalDetail, ObjectId userId, MultipartFile file)throws  Exception{
        User dbUser = userservice.findById(userId);
        UserAdditionalDetail existingProfile = null;

        if (dbUser!=null) {
            if (file!=null) {
                FileUploadResponse fileUploadResponse = fileUploadService.uploadFile(file,"profile");
                if (fileUploadResponse.getSecureUrl().length() == 0) {
                    throw  new Exception("could not save the file in cloudinary");

                }
                dbUser.setProfilePic(fileUploadResponse.getSecureUrl());
            }

            if (dbUser.getProfileDetail()!=null) {

                System.out.println("profile exist");
               existingProfile = dbUser.getProfileDetail();
                // Use a partial update by setting only non-null or non-empty fields
                // Update 'bio' if it's not null or empty
                if (userAdditionalDetail.getBio() != null && !userAdditionalDetail.getBio().isEmpty()) {
                    existingProfile.setBio(userAdditionalDetail.getBio());
                }

                // Update 'pronoun' if it's not null or empty
                if (userAdditionalDetail.getPronoun() != null && !userAdditionalDetail.getPronoun().isEmpty()) {
                    existingProfile.setPronoun(userAdditionalDetail.getPronoun());
                }

                // Update 'gender' if it's not null or empty
                if (userAdditionalDetail.getGender() != null && !userAdditionalDetail.getGender().isEmpty()) {
                    existingProfile.setGender(userAdditionalDetail.getGender());
                }

                // Update 'profession' if it's not null or empty
                if (userAdditionalDetail.getProfession() != null && !userAdditionalDetail.getProfession().isEmpty()) {
                    existingProfile.setProfession(userAdditionalDetail.getProfession());
                }

            } else {
                System.out.println("profile does not exist");
                // If the profile doesn't exist, return null or throw an exception as needed
               existingProfile = new UserAdditionalDetail();

                existingProfile.setBio(userAdditionalDetail.getBio());
                existingProfile.setId(new ObjectId());
                existingProfile.setPronoun(userAdditionalDetail.getPronoun());
                existingProfile.setProfession(userAdditionalDetail.getProfession());
                existingProfile.setGender(userAdditionalDetail.getGender());

                dbUser.setProfileDetail(existingProfile);

            }
            // Save the updated profile back to the database
              mongoTemplate.save(existingProfile);
            mongoTemplate.save(dbUser);


            return dbUser; // Return the updated profile

        }
        return  null;

    }
 public UserAdditionalDetail saveUserAdditionDetail(UserAdditionalDetail userAdditionalDetail, ObjectId userId,MultipartFile file)throws  Exception{
        User dbUser = userservice.findById(userId);

        if (dbUser!=null) {
            UserAdditionalDetail dbUserDetail = mongoTemplate.save(userAdditionalDetail);
            if (userAdditionalDetail != null) {
                dbUser.setProfileDetail(dbUserDetail);
                if (file != null) {
                    FileUploadResponse fileUploadResponse = fileUploadService.uploadFile(file,"profile");
                    if (fileUploadResponse.getSecureUrl().length() == 0) {
                        throw new Exception("could not save the file in cloudinary");
                    }else {
                        dbUser.setProfilePic(fileUploadResponse.getSecureUrl());
                    }
                }
                userservice.saveUser(dbUser);
                return userAdditionalDetail;

            }
            return null;

        }
        return  null;

    }

    // Method to remove the profileDetail field from the User document
    public User removeProfileDetail(User dbUser) {

        if (dbUser != null) {
            // Create a Query object to find the User by its ID
            Query query = new Query(Criteria.where("id").is(dbUser.getId()));

            // Create an Update object to unset (remove) the profileDetail field
            Update update = new Update().unset("profileDetail");

            // Update the User document by removing the profileDetail field
            mongoTemplate.updateFirst(query, update, User.class);

            // Return the updated User document
            return mongoTemplate.findById(dbUser.getId(), User.class);
        }

        return null; // Return null if the user is not found
    }

    public UserAdditionalDetail deleteById(ObjectId profileId,ObjectId userId) {
        User dbUser = userservice.findById(userId);
        System.out.println("profile id is "+profileId);
        // Find the document before deletion (optional, to return the deleted object)
        UserAdditionalDetail detail = mongoTemplate.findById(profileId, UserAdditionalDetail.class);

        if (detail != null) {
            // Remove the document
                 removeProfileDetail(dbUser);
            mongoTemplate.remove(detail);
        }

        return detail; // Return the deleted object, or null if not found
    }

    public UserAdditionalDetail updateProfile(UserAdditionalDetail userProfile) {
        // Find the existing profile using the ID (assuming you have the ID from the client or session)
        UserAdditionalDetail existingProfile = mongoTemplate.findById(userProfile.getId(), UserAdditionalDetail.class);

        if (existingProfile != null) {
            // Use a partial update by setting only non-null or non-empty fields

            // Update 'bio' if it's not null or empty
            if (userProfile.getBio() != null && !userProfile.getBio().isEmpty()) {
                existingProfile.setBio(userProfile.getBio());
            }

            // Update 'pronoun' if it's not null or empty
            if (userProfile.getPronoun() != null && !userProfile.getPronoun().isEmpty()) {
                existingProfile.setPronoun(userProfile.getPronoun());
            }

            // Update 'gender' if it's not null or empty
            if (userProfile.getGender() != null && !userProfile.getGender().isEmpty()) {
                existingProfile.setGender(userProfile.getGender());
            }

            // Update 'profession' if it's not null or empty
            if (userProfile.getProfession() != null && !userProfile.getProfession().isEmpty()) {
                existingProfile.setProfession(userProfile.getProfession());
            }

            // Save the updated profile back to the database
            mongoTemplate.save(existingProfile);

            return existingProfile; // Return the updated profile
        } else {
            // If the profile doesn't exist, return null or throw an exception as needed
            return null;
        }

    }


}
