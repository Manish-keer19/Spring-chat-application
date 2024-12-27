package com.ms.chat.application.Entity;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;





@Data
@Document(collection = "users")

public class User {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Indexed(unique = true)
    private String userName;

    private String password;

    private  String ProfilePic;
    private String email;

    @DBRef
    private UserAdditionalDetail profileDetail;
    private List<String> Role;

}
