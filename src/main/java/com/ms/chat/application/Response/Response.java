package com.ms.chat.application.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Response<T> {

    private int statusCode;
    private boolean success;  // Success flag
    private String message;
    private T data;
    private String error;

    // Constructor for success response with data
    public Response(int statusCode, boolean success, String message, T data) {
        this.statusCode = statusCode;
        this.success = success; // Set the success flag
        this.message = message;
        this.data = data;
        this.error = null; // No error if there's data
    }

    // Constructor for error response
    public Response(int statusCode, boolean success, String message, String error) {
        this.statusCode = statusCode;
        this.success = success; // Set the success flag
        this.message = message;
        this.data = null; // No data if there's an error
        this.error = error;
    }



    // Convenience methods to create success and error responses
    public static <T> Response<T> success(int statusCode, String message, T data) {
        return new Response<>(statusCode, true, message, data); // success flag is true
    }

    public static <T> Response<T> error(int statusCode, String message, String error) {
        return new Response<>(statusCode, false, message, error); // success flag is false
    }
}
