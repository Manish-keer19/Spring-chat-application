package com.ms.chat.application.Controller;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2LoginController {

    @GetMapping("/login/oauth2/code/google")
    public String googleLogin(@RequestParam String code) {
        // You can use the OAuth2 code to retrieve the access token and user details
        return "Google login successful";
    }

    @GetMapping("/login/oauth2/code/github")
    public String githubLogin(@RequestParam String code) {
        return "GitHub login successful";
    }

    @GetMapping("/login/oauth2/code/pinterest")
    public String pinterestLogin(@RequestParam String code) {
        return "Pinterest login successful";
    }

    @GetMapping("/login/oauth2/code/spotify")
    public String spotifyLogin(@RequestParam String code) {
        return "Spotify login successful";
    }

    // Optionally, display user information after login
    @GetMapping("/user")
    public String userDetails(OAuth2User principal) {
        return "Hello, " + principal.getAttributes().get("login");
    }
}

