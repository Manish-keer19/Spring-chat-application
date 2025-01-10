package com.ms.chat.application.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.chat.application.Entity.User;
import com.ms.chat.application.Response.LoginResponse;
import com.ms.chat.application.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

import com.ms.chat.application.Response.Response;
import com.ms.chat.application.Utils.JwtUtil;

import com.ms.chat.application.services.Userservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private Userservice userService;

    @Autowired
    private AuthService authService;
    
 
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;  // Add AuthenticationManager

    // Sign-up endpoint
    @PostMapping("/signup")
    public Response<String> signUp(@RequestParam("User") String user , @RequestParam("file") MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user1 = objectMapper.readValue(user, User.class);
            log.info(user1.toString());
            log.info(file.toString());
           Boolean IsUserExists =   authService.signUp(user1,file);
           if (IsUserExists) {
               return Response.success(200, "User registered successfully","");
           }
           else {
               return Response.error(400, "could not register user","");
           }
        } catch (Exception e) {
            return Response.error(400, "Error registering user", e.getMessage());
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody User user) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));


                String jwt = jwtUtil.generateToken(user.getUserName(),"");

                User userData = userService.findByUsername(user.getUserName());

                userData.setPassword(null);
            LoginResponse loginResponse = new LoginResponse(jwt,userData);
            // Return successful response with JWT token
            return Response.success(200, "User login successful",loginResponse );
            
        } catch (Exception e) {

            // Return error response
            return Response.error(401, "Invalid username or password", e.getMessage());
        }
    }
    
}
