package com.ms.chat.application.services;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ms.chat.application.DTO.MessageRequest;
import com.ms.chat.application.Entity.Message;
import com.ms.chat.application.Entity.MessageItem;


import com.ms.chat.application.Response.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Service
@Transactional
@Slf4j
public class MessageService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
   private SimpMessagingTemplate messagingTemplate ;


    // Method to create and save a message
    public MessageItem createMessage(ObjectId sender, ObjectId receiver, String messageContent) throws Exception {
        // Create a new MessageItem with the current timestamp
        MessageItem newMessage = new MessageItem();
        newMessage.setMessage(messageContent);
        newMessage.setSender(sender);
        newMessage.setCreatedAt(LocalDateTime.now()); // Set the current timestamp
        newMessage.setId(new ObjectId());


        // Query to find an existing conversation between sender and receiver
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(Criteria.where("currentUser").is(sender).and("anotherUser").is(receiver), Criteria.where("currentUser").is(receiver).and("anotherUser").is(sender)));

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
        return newMessage;
    }

    public MessageItem createMessageWithMedia(MessageRequest messageRequest, MultipartFile file) throws Exception {
        // Create a new MessageItem with the current timestamp
        MessageItem newMessage = new MessageItem();
        newMessage.setSender(messageRequest.getSenderId());
        newMessage.setCreatedAt(LocalDateTime.now()); // Set the current timestamp
        newMessage.setId(new ObjectId());
        if (messageRequest.getMessageContent() != null) {
            newMessage.setMessage(messageRequest.getMessageContent());
        }
        if (file != null) {
            FileUploadResponse fileUploadResponse = fileUploadService.uploadFile(file,"message");
            if (fileUploadResponse == null) {
                throw new Exception("could not save the file in cloudinary");

            }

            System.out.println("fileupload res is not null");
            log.info("fileupload res is ", fileUploadResponse);

            newMessage.setMedia(fileUploadResponse.getSecureUrl());
            newMessage.setMediaPublicId(fileUploadResponse.getPublicId());
        }

        // Query to find an existing conversation between sender and receiver
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(Criteria.where("currentUser").is(messageRequest.getSenderId()).and("anotherUser").is(messageRequest.getReceiverId()), Criteria.where("currentUser").is(messageRequest.getReceiverId()).and("anotherUser").is(messageRequest.getSenderId())));

        // Check if conversation already exists
        Message existingMessage = mongoTemplate.findOne(query, Message.class);

        System.out.println("exiting message is "+existingMessage);
        // If no conversation exists, create a new one
        if (existingMessage == null) {
            existingMessage = new Message();
            existingMessage.setCurrentUser(messageRequest.getSenderId());
            existingMessage.setAnotherUser(messageRequest.getReceiverId());
        }


        // Add the new message to the messages list
        existingMessage.addMessage(newMessage);


        // Save the message to MongoDB
        mongoTemplate.save(existingMessage);


        messagingTemplate.convertAndSend("/user/queue/" + messageRequest.getSenderId(), newMessage);
        messagingTemplate.convertAndSend("/user/queue/" + messageRequest.getReceiverId(), newMessage);

        return newMessage;
    }


    public Message getAlllMessages(ObjectId userId, ObjectId anotherUserId) {

        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(Criteria.where("currentUser").is(userId).and("anotherUser").is(anotherUserId), Criteria.where("currentUser").is(anotherUserId).and("anotherUser").is(userId)));

        return mongoTemplate.findOne(query, Message.class);
    }

    public Boolean deleteMessageItem(ObjectId messageId, ObjectId messageItemId) {
        // Query to find the Message document containing the MessageItem
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(messageId).and("messages._id").is(messageItemId));

        // Update to pull the MessageItem from the messages array
        Update update = new Update();
        update.pull("messages", new Query(Criteria.where("_id").is(messageItemId)));

        // Execute the update and get the result
        UpdateResult result = mongoTemplate.updateFirst(query, update, Message.class);

        // Return true if at least one document was modified
        return result.getModifiedCount() > 0;
    }
}
