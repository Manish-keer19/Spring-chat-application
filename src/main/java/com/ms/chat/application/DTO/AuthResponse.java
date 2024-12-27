package com.ms.chat.application.DTO;


import com.ms.chat.application.Entity.User;
import lombok.Data;


@Data
public class AuthResponse {

    private String token;
    private User user;  // Assuming User is another class containing user-related information

    // Constructor
    public AuthResponse(String token, User user) {
        this.token = token;
         this.user= user;
    }


}
