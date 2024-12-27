package com.ms.chat.application.Controller;


import com.ms.chat.application.Entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("")
@Slf4j
public class Oauth2Controller {

    @GetMapping("/done")
    public String oauth2Done(@AuthenticationPrincipal OAuth2User oAuth2User){
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


    @GetMapping("/success")
    public Map<?, ?> authLoginSuceessfully(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User is null");
            return response; // Return the map directly
        }

        log.info(oAuth2User.getAttributes().toString());

        // Fetching the attributes from the OAuth2User object
        String userName = oAuth2User.getName();
        String profilePic = (String) oAuth2User.getAttributes().get("profilepic"); // Example attribute name
        String email = (String) oAuth2User.getAttributes().get("email");

        // Create a User object and populate it with the details
        User user = new User();
        user.setUserName(userName);
        user.setProfilePic(profilePic);  // Ensure profilePic is cast correctly
        user.setEmail(email);

        // Optionally return the user details in a map or the User object itself
        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        return response;
    }

}
