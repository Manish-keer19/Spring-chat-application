package com.ms.chat.application.Entity;


import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Profile")
public class UserAdditionalDetail {



    @Id
    private ObjectId id;
    private String bio;
    private  String pronoun;
    private  String gender;
    private  String profession;
}
