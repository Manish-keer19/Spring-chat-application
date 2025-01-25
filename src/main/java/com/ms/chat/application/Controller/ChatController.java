package com.ms.chat.application.Controller;

import com.ms.chat.application.DTO.MessageRequest;
import com.ms.chat.application.DTO.SampleMessageRequest;
import com.ms.chat.application.Entity.Message;
import com.ms.chat.application.Entity.MessageItem;
import com.ms.chat.application.Response.Response;
import com.ms.chat.application.services.MessageService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/messages")
public class ChatController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/sendMessage")
    @SendTo("/public/messages")
    public MessageItem createMessage(@Payload MessageRequest message) throws Exception {
        log.info("sender is is {} rececverId is {} message is {} ",message.getSenderId(),message.getReceiverId(),message.getMessageContent());
        MessageItem messaages = messageService.createMessage(message.getSenderId(),
                message.getReceiverId(),
                message.getMessageContent());


        return messaages;
    }

@MessageMapping("/sendTypingMessage")
    @SendTo("/public/typing")
    public String SendtypingMessage() {
       return "typing....";
        }



    @MessageMapping("/sendMessageString")
    @SendTo("/public/messages")
    public String sendMessageString(String msg){
        return msg;
    }




    @MessageMapping("/sendMessageSenderAndReceiver")
    public void sendMessageSederAndReceiver(@Payload MessageRequest message) throws Exception {
        log.info("Sender: {}, Receiver: {}, Message: {}",
                message.getSenderId(), message.getReceiverId(), message.getMessageContent());

        // Create the message item
        MessageItem messageItem = messageService.createMessage(
                message.getSenderId(),
                message.getReceiverId(),
                message.getMessageContent()
        );

        // Send message to the sender's queue
//        System.out.println("Sending message to: /user/queue/" + message.getReceiverId());
        messagingTemplate.convertAndSend("/user/queue/" + message.getSenderId(), messageItem);

        // Send message to the receiver's queue
        messagingTemplate.convertAndSend("/user/queue/" + message.getReceiverId(), messageItem);
    }






}


