package com.ms.chat.application.Controller;

import com.ms.chat.application.Entity.User;
import com.ms.chat.application.Entity.UserAdditionalDetail;
import com.ms.chat.application.Response.Response;
import com.ms.chat.application.services.CustomUserDetails;
import com.ms.chat.application.services.Userservice;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
    @Autowired
    private  Userservice userservice;



    @GetMapping("/get-all-usersdata")
public ResponseEntity<Response<List<User>>> getAlluserData() {
        try {

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();


            List<User> usersData = userservice.getAllUserData();

            usersData.removeIf(user -> user.getId().equals(customUserDetails.getUserId()));

            if (usersData == null) {
                return new ResponseEntity<>(Response.error(404, "users not found", "ni mile bhai user"), HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(Response.success(200, "usersData found succefully",usersData ), HttpStatus.OK);



        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(404, "could not find the allUserData ", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
