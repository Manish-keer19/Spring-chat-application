package com.ms.chat.application.config;


import com.ms.chat.application.Entity.User;
import com.ms.chat.application.Utils.JwtUtil;
import com.ms.chat.application.services.Userservice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private Userservice userService;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        log.info("🎉 OAuth2 Login Success Triggered");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        if (oAuth2User == null) {
            log.error("❌ OAuth2User is null");
            response.sendRedirect("https://manishchatapp.vercel.app/#/oauth-failed");
            return;
        }

        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("OAuth2 Attributes: {}", attributes);

        // Determine provider and user info
        String provider;
        String userName;
        String profilePic;
        String email;

        if (attributes.containsKey("login")) {
            // GitHub
            provider = "GitHub";
            userName = (String) attributes.get("login");
            profilePic = (String) attributes.get("avatar_url");
            email = (String) attributes.get("email");
        } else {
            // Google
            provider = "Google";
            userName = (String) attributes.get("name");
            profilePic = (String) attributes.get("picture");
            email = (String) attributes.get("email");
        }

        log.info("Provider: {}", provider);
        log.info("User: {}, Email: {}", userName, email);

        // Create User object
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setProfilePic(profilePic);
        user.setRole(Collections.singletonList("USER"));

        // Save or update user
        User dbUser = userService.findByUsername(userName);
        if (dbUser == null) {
            user.setId(new ObjectId());
            dbUser = userService.saveUser(user);
            log.info("👤 New user saved: {}", dbUser);
        } else {
            log.info("✅ Existing user found: {}", dbUser);
        }

        // Generate JWT
        String token = jwtUtil.generateToken(userName, "");

        // ✅ Also get the MongoDB ID
        String userId = dbUser.getId().toString();


        String platform = (String) request.getSession().getAttribute("platform");

        String redirectUrl;
        if ("mobile".equals(platform)) {
            redirectUrl = String.format("manishapp://oauth-success?token=%s&userId=%s", token, userId);
        } else {
            redirectUrl = String.format("https://manishchatapp.vercel.app/#/oauth-success?token=%s&userId=%s", token, userId);
        }


        // ✅ Redirect with both token and userId
//        String redirectUrl = String.format("https://manishchatapp.vercel.app/#/oauth-success?token=%s&userId=%s", token, userId);
//        String redirectUrl = String.format("http://localhost:51 -73/#/oauth-success?token=%s&userId=%s", token, userId);
        response.sendRedirect(redirectUrl);
    }
}
