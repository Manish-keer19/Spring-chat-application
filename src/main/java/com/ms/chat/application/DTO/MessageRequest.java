package com.ms.chat.application.DTO;

import org.bson.types.ObjectId;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MessageRequest {

    private ObjectId senderId; // Sender's ObjectId
        private ObjectId receiverId; // Receiver's ObjectId
    private String messageContent; // Message content

    
}
