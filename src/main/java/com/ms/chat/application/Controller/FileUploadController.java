package com.ms.chat.application.Controller;

import com.ms.chat.application.Entity.UserAdditionalDetail;
import com.ms.chat.application.Response.FileUploadResponse;
import com.ms.chat.application.Response.Response;
import com.ms.chat.application.services.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/upload")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping
    public FileUploadResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileUploadService.uploadFile(file,"Testing");  // Call the service to upload the file
    }



    @PostMapping("/file-and-data")
    public ResponseEntity<Response<?>> sendFileAndData(
            @RequestPart("userAdditionalDetail") UserAdditionalDetail userAdditionalDetail
//            @RequestParam("file") MultipartFile file) {
    ){

        System.out.println("hello bhai we are in file and data");
//        System.out.println("file is " + file);
        System.out.println("userAdditionalDetail is " + userAdditionalDetail);

        // Map to hold the response data
        Map<String, Object> data = new HashMap<>();
        data.put("userAdditionalDetail", userAdditionalDetail);
//        data.put("file", file.getOriginalFilename());

        return new ResponseEntity<>(Response.success(200, "Profile sent successfully", data), HttpStatus.OK);
    }





}
