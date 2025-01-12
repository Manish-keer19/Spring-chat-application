package com.ms.chat.application.Controller;


import com.ms.chat.application.Entity.User;
import com.ms.chat.application.Response.Response;
import com.ms.chat.application.Utils.JwtUtil;
import com.ms.chat.application.services.Userservice;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/oauth2")
@Slf4j
public class Oauth2Controller {

    @Autowired
    private Userservice userservice;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/done")
    public String oauth2Done(@AuthenticationPrincipal OAuth2User oAuth2User) {
        log.info(oAuth2User.getAttributes().toString());

        return "auth success full";

    }
//    @GetMapping("/success")
//    public Map<?, ?> authLoginSuceessfully(@AuthenticationPrincipal OAuth2User oAuth2User){
//
//        if (oAuth2User == null) {
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "User is null");
//            return response; // Return the map directly
//        }
//
//        log.info(oAuth2User.getAttributes().toString());
//
//        return  oAuth2User.getAttributes();
//
//
//
//
//    }


//    @GetMapping("/success")
//    public ResponseEntity<Response<?>> authLoginSuceessfully(@AuthenticationPrincipal OAuth2User oAuth2User) {
//        if (oAuth2User == null) {
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "User is null");
//            return ResponseEntity.ok(Response.error(404, "User is null", null));
//        }
//
//        log.info(oAuth2User.getAttributes().toString());
//
//        // Fetching the attributes from the OAuth2User object
//        String userName = oAuth2User.getAttributes().get("login").toString();
//        System.out.println("usernam is "+userName);
//        String profilePic = (String) oAuth2User.getAttributes().get("avatar_url"); // Example attribute name
//        System.out.println("profile pic is "+profilePic);
//        String email = (String) oAuth2User.getAttributes().get("email");
//        System.out.println("email is "+email);
//
//        // Create a User object and populate it with the details
//        User user = new User();
//        user.setUserName(userName);
//        user.setProfilePic(profilePic);  //Ensure profilePic is cast correctly
//        user.setEmail(email);
//        log.info("user is "+user);
//
//        // Optionally return the user details in a map or the User object itself
//        Map<String, Object> response = new HashMap<>();
//
//        String token = jwtUtil.generateToken(userName,"");
//        response.put("user", user);
//        response.put("token", token);
//        return ResponseEntity.ok(Response.success(200, "Userdata fetched successfully", response));
//
//    }
//}


    @GetMapping("/success")
    public ResponseEntity<Response<?>> authLoginSuceessfully(@AuthenticationPrincipal OAuth2User oAuth2User) {
        System.out.println("hello bhai we are in success");
//        System.out.println("oAuth2User is " + oAuth2User.getAttributes().toString());
        if (oAuth2User == null) {
            return ResponseEntity.ok(Response.error(404, "User is null", null));
        }

        log.info(oAuth2User.getAttributes().toString());

        // Identify the provider by checking specific keys
        String provider;
        String userName;
        String profilePic;
        String email;

        if (oAuth2User.getAttributes().containsKey("login")) {
            // GitHub login
            provider = "GitHub";
            userName = oAuth2User.getAttributes().get("login").toString();
            profilePic = (String) oAuth2User.getAttributes().get("avatar_url");
            email = (String) oAuth2User.getAttributes().get("email");
        } else if (oAuth2User.getAttributes().containsKey("email")) {
            // Google login
            provider = "Google";
            userName = oAuth2User.getAttributes().get("name").toString();
            profilePic = (String) oAuth2User.getAttributes().get("picture");
            email = (String) oAuth2User.getAttributes().get("email");
        } else {
            provider = "Unknown";
            userName = "Unknown";
            profilePic = "Unknown";
            email = "Unknown";
        }

        log.info("Provider: " + provider);
        log.info("Username: " + userName);
        log.info("Profile Picture: " + profilePic);
        log.info("Email: " + email);

        // Create a User object and populate it with the details
        User user = new User();
        user.setUserName(userName);
        user.setProfilePic(profilePic);
        user.setEmail(email);
       user.setRole(Arrays.asList("USER"));



        // Generate a JWT token
        String token = jwtUtil.generateToken(userName, "");



        User dbuser = userservice.findByUsername(user.getUserName());
        if (dbuser == null) {
            user.setId(new ObjectId());
         dbuser = userservice.saveUser(user);

        }
        // Create a response map
        Map<String, Object> response = new HashMap<>();
        response.put("provider", provider);
        response.put("user", dbuser);
        response.put("token", token);
        System.out.println("user pahle se he db me "+dbuser);
        return ResponseEntity.ok(Response.success(200, "Userdata fetched successfully", response));


    }
}
