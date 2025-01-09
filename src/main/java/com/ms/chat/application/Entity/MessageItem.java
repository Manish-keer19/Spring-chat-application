package com.ms.chat.application.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageItem {


    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId id;
    private String message; // The message content
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId sender; // Reference to the sender
    private LocalDateTime createdAt; // Timestamp when the message was created

    private String media;
    private  String mediaPublicId;

    // Constructor with parameters
    public MessageItem(String message, ObjectId sender) {
        this.message = message;
        this.sender = sender;
        this.createdAt = LocalDateTime.now(); // Set the current date and time
    }
    // Default constructor
    public MessageItem() {
        this.createdAt = LocalDateTime.now(); // Set the current date and time for default objects
    }
}
