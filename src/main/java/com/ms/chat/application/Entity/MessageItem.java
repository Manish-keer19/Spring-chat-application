package com.ms.chat.application.Entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageItem {

    private String message; // The message content

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId sender; // Reference to the sender
    private LocalDateTime createdAt; // Timestamp when the message was created

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
