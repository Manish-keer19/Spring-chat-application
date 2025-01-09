package com.ms.chat.application.Entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    private ObjectId id;  // MongoDB _id

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId currentUser;  // Reference to the current user (ObjectId)


    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId anotherUser;  // Reference to the other user (ObjectId)

    private List<MessageItem> messages = new ArrayList<>();  // List of messages

    // Getters and setters for senderDetails and receiverDetails
   
    public void addMessage(MessageItem messageItem) {
        this.messages.add(messageItem);
    }
}
