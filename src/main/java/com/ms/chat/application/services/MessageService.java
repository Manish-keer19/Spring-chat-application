package com.ms.chat.application.services;

import com.ms.chat.application.Entity.Message;
import com.ms.chat.application.Entity.MessageItem;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional
public class MessageService {

    @Autowired
    private MongoTemplate mongoTemplate;

     @Autowired
    private SimpMessagingTemplate messagingTemplate; // Used to send messages over WebSocket

    // Method to create and save a message
    public Message createMessage(ObjectId sender, ObjectId receiver, String messageContent) {
        // Create a new MessageItem with the current timestamp
        MessageItem newMessage = new MessageItem();
        newMessage.setMessage(messageContent);
        newMessage.setSender(sender);
        newMessage.setCreatedAt(LocalDateTime.now()); // Set the current timestamp

        // Query to find an existing conversation between sender and receiver
        Query query = new Query();
query.addCriteria(new Criteria().orOperator(
        Criteria.where("currentUser").is(sender).and("anotherUser").is(receiver),
        Criteria.where("currentUser").is(receiver).and("anotherUser").is(sender)
));

        // Check if conversation already exists
        Message existingMessage = mongoTemplate.findOne(query, Message.class);

        // If no conversation exists, create a new one
        if (existingMessage == null) {
            existingMessage = new Message();
            existingMessage.setCurrentUser(sender);
            existingMessage.setAnotherUser(receiver);
        }


        // Add the new message to the messages list
        existingMessage.addMessage(newMessage);


        // Save the message to MongoDB
        mongoTemplate.save(existingMessage);


        // Save the conversation to the database
        return existingMessage;
    }


    public Message getAlllMessages(ObjectId userId,ObjectId anotherUserId){
        
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("currentUser").is(userId).and("anotherUser").is(anotherUserId),
                Criteria.where("currentUser").is(anotherUserId).and("anotherUser").is(userId)
        ));

        return mongoTemplate.findOne(query, Message.class);
    }
}
