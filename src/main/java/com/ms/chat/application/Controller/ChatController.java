package com.ms.chat.application.Controller;

import com.ms.chat.application.Entity.Message;
import com.ms.chat.application.services.MessageService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class ChatController {

    @Autowired
    private MessageService messageService;

    // Endpoint to create and send a message (real-time WebSocket)
    @PostMapping("/create")
    public Message createMessage(
            @RequestParam ObjectId currentUser,
            @RequestParam ObjectId anotherUser,
            @RequestParam String messageContent) {
        return messageService.createMessage(currentUser, anotherUser, messageContent);
    }
}
