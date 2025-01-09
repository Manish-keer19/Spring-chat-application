package com.ms.chat.application.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ms.chat.application.Response.FileUploadResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileUploadService {
    @Autowired
    private Cloudinary cloudinary;

    public FileUploadResponse uploadFile(MultipartFile file) {
        try {
            // Upload file to Cloudinary and capture the response
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder","spring"));

            // Extract response data
            String url = (String) uploadResult.get("url");
            String secureUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            String format = (String) uploadResult.get("format");
            String originalFilename = (String) uploadResult.get("original_filename");
            Object bytesObj = uploadResult.get("bytes");
            long bytes = (bytesObj instanceof Integer) ? ((Integer) bytesObj).longValue() : (Long) bytesObj;

            // Return the full response encapsulated in the FileUploadResponse object
            return new FileUploadResponse(url, secureUrl, publicId, format, originalFilename, bytes,
                    "File uploaded successfully!", true);
        } catch (IOException e) {
            // Handle exception and return failure response
            return new FileUploadResponse(null, null, null, null, null, 0,
                    "Error uploading file: " + e.getMessage(), false);
        }
    }
}
