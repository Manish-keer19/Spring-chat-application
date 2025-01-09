package com.ms.chat.application.Response;

import com.ms.chat.application.Entity.UserAdditionalDetail;

public class FileUploadResponse {

    private String url;
    private String secureUrl;
    private String publicId;
    private String format;
    private String originalFilename;
    private long bytes;
    private String message;
    private boolean success;

    // Constructor
    public FileUploadResponse(String url, String secureUrl, String publicId, String format,
                              String originalFilename, long bytes, String message, boolean success) {
        this.url = url;
        
        this.secureUrl = secureUrl;
        this.publicId = publicId;
        this.format = format;
        this.originalFilename = originalFilename;
        this.bytes = bytes;
        this.message = message;
        this.success = success;
    }

    // Getters and setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecureUrl() {
        return secureUrl;
    }

    public void setSecureUrl(String secureUrl) {
        this.secureUrl = secureUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
