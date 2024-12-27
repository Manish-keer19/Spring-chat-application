package com.ms.chat.application.Entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Document(collection = "messages")
public class Message {

    @Id
    private String id;  // MongoDB _id
    private ObjectId currentUser;  // Reference to the current user (ObjectId)

    private ObjectId anotherUser;  // Reference to the other user (ObjectId)

    private List<MessageItem> messages = new ArrayList<>();  // List of messages
    
    // Additional fields for the full user details to be returned in the response
    private User senderDetails;  // Full details of the sender
    private User receiverDetails;  // Full details of the receiver

    // Getters and setters for senderDetails and receiverDetails
   
    public void addMessage(MessageItem messageItem) {
        this.messages.add(messageItem);
    }
}
