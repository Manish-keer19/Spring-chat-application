package com.ms.chat.application.Controller;

import com.ms.chat.application.Response.FileUploadResponse;
import com.ms.chat.application.services.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/upload")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping
    public FileUploadResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileUploadService.uploadFile(file);  // Call the service to upload the file
    }
}
