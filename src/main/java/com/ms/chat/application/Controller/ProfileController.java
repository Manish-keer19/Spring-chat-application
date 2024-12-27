package com.ms.chat.application.Controller;


import com.ms.chat.application.Entity.User;
import com.ms.chat.application.Entity.UserAdditionalDetail;
import com.ms.chat.application.Response.FileUploadResponse;
import com.ms.chat.application.Response.Response;
import com.ms.chat.application.services.CustomUserDetails;
import com.ms.chat.application.services.FileUploadService;
import com.ms.chat.application.services.Profileservice;
import com.ms.chat.application.services.Userservice;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profile")
@Slf4j
public class ProfileController {
    @Autowired
    private Profileservice profileservice;


    @Autowired
    private  Userservice userservice;

    @Autowired
    private FileUploadService fileUploadService;


    @PostMapping("/add-profile")
    public ResponseEntity<Response<UserAdditionalDetail>> addProfileDetail( @RequestBody UserAdditionalDetail userAdditionalDetail ) {

        try {


            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Cast the Principal to CustomUserDetails to access the userId
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            ObjectId userId =(ObjectId) userDetails.getUserId();
            // Save the profile detail
            UserAdditionalDetail dbUserAdditionalDetail = profileservice.saveUserAdditionDetail(userAdditionalDetail,userId);

            // Return a successful response with status 200
            return new ResponseEntity<>(Response.success(200, "User profile detail created successfully", dbUserAdditionalDetail), HttpStatus.CREATED);

        } catch (Exception e) {
            // Return an error response if something goes wrong
            return new ResponseEntity<>(Response.error(401, "Could not create the user profile detail", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/delete-profile/{profileId}")
    public ResponseEntity<Response<UserAdditionalDetail>> deleteUserDetail(@PathVariable ObjectId profileId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            ObjectId userId = (ObjectId) userDetails.getUserId();

            UserAdditionalDetail deletedProfile = profileservice.deleteById(profileId,userId);

            if (deletedProfile == null) {
                return new ResponseEntity<>(Response.error(404, "Profile not found", null), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(Response.success(200, "Profile deleted successfully", deletedProfile), HttpStatus.OK);

        } catch (Exception e) {
            log.error(String.valueOf(e));
            return new ResponseEntity<>(Response.error(500, "Could not delete the profile", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/upadte-profile")
    public  ResponseEntity<Response<UserAdditionalDetail>> updateProfile(@RequestBody UserAdditionalDetail userProfile ) {


        try {

               UserAdditionalDetail updatedProfile = profileservice.updateProfile(userProfile);

            if (updatedProfile == null) {
                return new ResponseEntity<>(Response.error(404, "Profile not found", null), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(Response.success(200, "Profile updated successfully", updatedProfile), HttpStatus.OK);

        }
         catch (Exception e) {
            return new ResponseEntity<>(Response.error(401, "could not update the profile",e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
