package com.ms.chat.application.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

import com.ms.chat.application.Entity.userModel;
import com.ms.chat.application.Response.Response;
import com.ms.chat.application.Utils.JwtUtil;

import com.ms.chat.application.services.Userservice;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private Userservice userService;
    
 
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;  // Add AuthenticationManager

    // Sign-up endpoint
    @PostMapping("/signup")
    public Response<userModel> signUp(@RequestBody userModel user) {
        try {
            userModel dbuser =  userService.SaveUser(user);
            return Response.success(200, "User registered successfully", dbuser);
        } catch (Exception e) {
            return Response.error(400, "Error registering user", e.getMessage());
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public Response<String> login(@RequestBody userModel user) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            
            
            // Generate JWT token
           String userId = user.getId().toString();
            String jwt = jwtUtil.generateToken(user.getUserName(),userId,"");
            
            // Return successful response with JWT token
            return Response.success(200, "User login successful", jwt);
            
        } catch (Exception e) {
            log.error("Exception occurred while creating authentication token", e);
            
            // Return error response
            return Response.error(401, "Invalid username or password", e.getMessage());
        }
    }
    
}
