package com.ms.chat.application.Controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.chat.application.DTO.MessageRequest;
import com.ms.chat.application.Entity.Message;

import com.ms.chat.application.Response.Response;
import com.ms.chat.application.services.CustomUserDetails;
import com.ms.chat.application.services.MessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/create-message")
    public ResponseEntity<Response<Message>> createMessage(@RequestBody MessageRequest messageRequest) {

        // Log the incoming JSON object
        log.info("Received messageRequest: senderId={}, receiverId={}, messageContent={}",
                messageRequest.getSenderId(), messageRequest.getReceiverId(), messageRequest.getMessageContent());

        // Extract senderId, receiverId, and messageContent from the request DTO
        ObjectId senderId = messageRequest.getSenderId();
        ObjectId receiverId = messageRequest.getReceiverId();
        String messageContent = messageRequest.getMessageContent();

        log.info("messageContent: {}", messageContent);
        log.info("senderId: {}", senderId);
        log.info("receiverId: {}", receiverId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Cast the Principal to CustomUserDetails to access the userId
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        ObjectId userId = userDetails.getUserId();
        try {

            Message messages = messageService.createMessage(userId, receiverId, messageContent);
            // Message messages = messageService.createMessage(senderId, receiverId,
            // messageContent);

            if (messages != null) {
                Response<Message> response = Response.success(200, "message has been created succefully", messages);

                return ResponseEntity.ok(response);
            } else {
                Response<Message> response = Response.error(400, "could not create message ",
                        "error while create message");

                return ResponseEntity.internalServerError().body(response);

            }
        } catch (Exception e) {

            Response<Message> response = Response.error(400, "could not create message ", e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }

    }

    @GetMapping("/get-messages/{anotherUserId}")
    public ResponseEntity<Response<?>> getMessages(@PathVariable ObjectId anotherUserId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            ObjectId userId = userDetails.getUserId();

            Message messages = messageService.getAlllMessages(userId, anotherUserId);
            Response<Message> response = Response.success(200, "message has been created succefully", messages);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // TODO: handle exception
            Response<?> res = Response.error(400, "could not get the message,", e.getMessage());
            return ResponseEntity.internalServerError().body(res);

        }

    }
}
