package com.ms.chat.application.Entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Profile")
public class UserAdditionalDetail {



    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String bio;
    private  String pronoun;
    private  String gender;
    private  String profession;
}
