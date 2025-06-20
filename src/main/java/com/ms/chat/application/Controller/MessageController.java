package com.ms.chat.application.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.chat.application.Entity.MessageItem;
import com.ms.chat.application.Response.FileUploadResponse;
import com.ms.chat.application.services.FileUploadService;
import io.jsonwebtoken.io.IOException;
import jakarta.websocket.server.PathParam;
import org.apache.hc.core5.http.HttpMessage;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ms.chat.application.DTO.MessageRequest;
import com.ms.chat.application.Entity.Message;

import com.ms.chat.application.Response.Response;
import com.ms.chat.application.services.CustomUserDetails;
import com.ms.chat.application.services.MessageService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/create-message")
    public ResponseEntity<Response<MessageItem>> createMessage(@RequestBody MessageRequest messageRequest) {

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

            MessageItem messages = messageService.createMessage(senderId, receiverId, messageContent);
            // Message messages = messageService.createMessage(senderId, receiverId,
            // messageContent);

            if (messages != null) {
                Response<MessageItem> response = Response.success(200, "message has been created succefully", messages);

                return ResponseEntity.ok(response);
            } else {
                Response<MessageItem> response = Response.error(400, "could not create message ",
                        "error while create message");
                return ResponseEntity.internalServerError().body(response);

            }
        } catch (Exception e) {

            Response<MessageItem> response = Response.error(400, "could not create message ", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

    }

    @PostMapping("/create-message-with-media-without-message")
    public ResponseEntity<Response<MessageItem>> createMessageWithMediaWithouMessage(
    @RequestParam("messageRequest") String messageRequestJson,
    @RequestParam("file") MultipartFile file) throws JsonProcessingException {
        // Parse the JSON into the DTO class
        ObjectMapper objectMapper = new ObjectMapper();
        MessageRequest messageRequest = objectMapper.readValue(messageRequestJson, MessageRequest.class);

        // Print the parsed data
        System.out.println("Text (MessageRequest): " + messageRequest.getMessageContent());
        System.out.println("SenderId: " + messageRequest.getSenderId());
        System.out.println("ReceiverId: " + messageRequest.getReceiverId());
        System.out.println("File Name: " + file.getOriginalFilename());
        // Log the incoming JSON object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Cast the Principal to CustomUserDetails to access the userId
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        ObjectId userId = userDetails.getUserId();
        try {
            MessageItem messages = messageService.createMessageWithMedia(messageRequest,file);

            if (messages != null) {
                Response<MessageItem> response = Response.success(200, "media created succesfully", messages);

                return ResponseEntity.ok(response);
            } else {
                Response<MessageItem> response = Response.error(400, "could not create message ",
                        "error while create message");
                return ResponseEntity.internalServerError().body(response);

            }
        } catch (Exception e) {

            Response<MessageItem> response = Response.error(400, "could not create message ", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

    }


    @PostMapping("/create-message-with-media")
    public ResponseEntity<Response<?>> createMessageWithMedia(@RequestParam("messageRequest") String messageRequestJson,
                                                              @RequestParam("file") MultipartFile file) throws JsonProcessingException {
        // Parse the JSON into the DTO class
        ObjectMapper objectMapper = new ObjectMapper();
        MessageRequest messageRequest = objectMapper.readValue(messageRequestJson, MessageRequest.class);

        // Print the parsed data
        System.out.println("Text (MessageRequest): " + messageRequest.getMessageContent());
        System.out.println("SenderId: " + messageRequest.getSenderId());
        System.out.println("ReceiverId: " + messageRequest.getReceiverId());
        System.out.println("File Name: " + file.getOriginalFilename());
        // Log the incoming JSON object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        // Cast the Principal to CustomUserDetails to access the userId
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        ObjectId userId = userDetails.getUserId();
        try {

            MessageItem messages = messageService.createMessageWithMedia(messageRequest, file);
            if (messages != null) {
                Response<MessageItem> response = Response.success(200, "media created succesfully", messages);

                return ResponseEntity.ok(response);
            } else {
                Response<MessageItem> response = Response.error(400, "could not create message ",
                        "error while create message");
                return ResponseEntity.internalServerError().body(response);

            }
        } catch (Exception e) {

            Response<MessageItem> response = Response.error(400, "could not create message ", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

    }


    @PostMapping("/create-message-with-media-testing")
    public ResponseEntity<Response<?>> createMessageWithMediaTest(
            @RequestParam("messageRequest") String messageRequestJson,
            @RequestParam("file") MultipartFile file) throws JsonProcessingException {

        // Parse the JSON into the DTO class
        ObjectMapper objectMapper = new ObjectMapper();
        MessageRequest messageRequest = objectMapper.readValue(messageRequestJson, MessageRequest.class);

        // Print the parsed data
        System.out.println("Text (MessageRequest): " + messageRequest.getMessageContent());
        System.out.println("SenderId: " + messageRequest.getSenderId());
        System.out.println("ReceiverId: " + messageRequest.getReceiverId());
        System.out.println("File Name: " + file.getOriginalFilename());

        // Implement your logic for file processing and storing the message here

        return ResponseEntity.ok(new Response<>("Message created successfully"));
    }


    @GetMapping("/get-messages/{anotherUserId}")
    public ResponseEntity<Response<?>> getMessages(@PathVariable ObjectId anotherUserId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            ObjectId userId = userDetails.getUserId();

            Message messages = messageService.getAlllMessages(userId, anotherUserId);
            Response<Message> response = Response.success(200, "message has been fetched succefully", messages);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // TODO: handle exception
            Response<?> res = Response.error(400, "could not get the message,", e.getMessage());
            return ResponseEntity.internalServerError().body(res);

        }
    }

    @DeleteMapping("/delete-message/{messageId}/items/{messageItemId}")
    public ResponseEntity<Response<?>> deleteMessage(@PathVariable ObjectId messageId, @PathVariable ObjectId messageItemId) {

        try {
            Boolean IsMessageDeleted = messageService.deleteMessageItem(messageId,messageItemId);

            if(!IsMessageDeleted){
                return new ResponseEntity<>(Response.error(404, "Message not found", null), HttpStatus.NOT_FOUND);
            }
            Response<MessageItem> response = Response.success(200, "message deleted successfully",null);
            return ResponseEntity.ok(response);

        }
        catch (Exception e) {
            Response<MessageItem> response = Response.error(400, "could not delete message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

//    @PutMapping("/update-message/{messageId}")
//    public ResponseEntity<Response<?>> updateMessage(@PathVariable ObjectId messageId, @RequestBody MessageRequest messageRequest) {
//        try {
//            MessageItem message = messageService.updateMessage(messageId, messageRequest);
//            Response<MessageItem> response = Response.success(200, "message updated successfully", message);
//            return ResponseEntity.ok(response);
//        }
//        catch (Exception e) {
//            Response<MessageItem> response = Response.error(400, "could not update message", e.getMessage());
//            return ResponseEntity.internalServerError().body(response);
//        }
//    }




}
