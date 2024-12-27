package com.ms.chat.application.Controller;

import com.ms.chat.application.DTO.MessageRequest;
import com.ms.chat.application.DTO.SampleMessageRequest;
import com.ms.chat.application.Entity.Message;
import com.ms.chat.application.Response.Response;
import com.ms.chat.application.services.MessageService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/messages")
public class ChatController {

    @Autowired
    private MessageService messageService;

    // Endpoint to create and send a message (real-time WebSocket)
//    @MessageMapping("/sendMessage")
//    @SendTo("/public/messages")
//    public SampleMessageRequest createMessage(@Payload SampleMessageRequest message) {
//        log.info("we are in createMessage controller");
//   log.info("message is message {}",message);
//        return  message;
//
//
//    }


    @MessageMapping("/sendMessage")
    @SendTo("/public/messages")
    public Message createMessage(@Payload MessageRequest message) {

        Message messaages = messageService.createMessage(message.getSenderId(),
                message.getReceiverId(),
                message.getMessageContent());

        return messaages;
    }
@MessageMapping("/sendTypingMessage")
    @SendTo("/public/typing")
    public String SendtypingMessage() {
       return "typing....";
        }


        
        


    }


