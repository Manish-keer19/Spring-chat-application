//
//
//
//
//
//
//package com.ms.chat.application.Controller;
//
//import com.ms.chat.application.Entity.Message;
//import com.ms.chat.application.Entity.User;
//import com.ms.chat.application.Response.Response;
//import com.ms.chat.application.services.AdminService;
//import org.bson.types.ObjectId;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/admin")
//public class AdminController {
//
//    private final AdminService adminService;
//
//    public AdminController(AdminService adminService) {
//        this.adminService = adminService;
//    }
//
//    @GetMapping("/users")
//    public ResponseEntity<Response<List<User>>> getAllUsers(@RequestHeader("X-User-Id") String adminId) {
//        try {
//            if (!adminService.isAdmin(new ObjectId(adminId))) {
//                return ResponseEntity.status(403).body(Response.error(403, "Forbidden", "User is not authorized"));
//            }
//            List<User> users = adminService.getAllUsersWithDetails();
//            return ResponseEntity.ok(Response.success(200, "Users retrieved successfully", users));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Response.error(500, "Internal Server Error", e.getMessage()));
//        }
//    }
//
//    @GetMapping("/users/{id}")
//    public ResponseEntity<Response<User>> getUserDetails(@PathVariable String id,
//                                                         @RequestHeader("X-User-Id") String adminId) {
//        try {
//            if (!adminService.isAdmin(new ObjectId(adminId))) {
//                return ResponseEntity.status(403).body(Response.error(403, "Forbidden", "User is not authorized"));
//            }
//            User user = adminService.getUserWithDetails(new ObjectId(id));
//            if (user == null) {
//                return ResponseEntity.status(404).body(Response.error(404, "Not Found", "User not found"));
//            }
//            return ResponseEntity.ok(Response.success(200, "User details retrieved successfully", user));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Response.error(500, "Internal Server Error", e.getMessage()));
//        }
//    }
//
//    @GetMapping("/users/{id}/messages")
//    public ResponseEntity<Response<List<Message>>> getUserMessages(@PathVariable String id,
//                                                                   @RequestHeader("X-User-Id") String adminId) {
//        try {
//            if (!adminService.isAdmin(new ObjectId(adminId))) {
//                return ResponseEntity.status(403).body(Response.error(403, "Forbidden", "User is not authorized"));
//            }
//            List<Message> messages = adminService.getAllMessagesForUser(new ObjectId(id));
//            return ResponseEntity.ok(Response.success(200, "User messages retrieved successfully", messages));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Response.error(500, "Internal Server Error", e.getMessage()));
//        }
//    }
//
//    @DeleteMapping("/users/{id}")
//    public ResponseEntity<Response<Void>> deleteUser(@PathVariable String id,
//                                                     @RequestHeader("X-User-Id") String adminId) {
//        try {
//            if (!adminService.isAdmin(new ObjectId(adminId))) {
//                return ResponseEntity.status(403).body(Response.error(403, "Forbidden", "User is not authorized"));
//            }
//            adminService.deleteUserAndData(new ObjectId(id));
//            return ResponseEntity.ok(Response.success(204, "User deleted successfully", null));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Response.error(500, "Internal Server Error", e.getMessage()));
//        }
//    }
//}







package com.ms.chat.application.Controller;

import com.ms.chat.application.Entity.Message;
import com.ms.chat.application.Entity.User;
import com.ms.chat.application.Response.Response;
import com.ms.chat.application.services.AdminService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final MongoTemplate mongoTemplate;

    public AdminController(AdminService adminService, MongoTemplate mongoTemplate) {
        this.adminService = adminService;
        this.mongoTemplate = mongoTemplate;
    }



    @GetMapping("/users")
    public ResponseEntity<Response<List<User>>> getAllUsers(@RequestHeader("X-User-Id") String adminId) {
        try {
            if (!adminService.isAdmin(new ObjectId(adminId))) {
                return ResponseEntity.status(403).body(Response.error(403, "Forbidden", "User is not authorized"));
            }
            List<User> users = adminService.getAllUsersWithDetails();
            return ResponseEntity.ok(Response.success(200, "Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Response.error(500, "Internal Server Error", e.getMessage()));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Response<User>> getUserDetails(@PathVariable String id,
                                                         @RequestHeader("X-User-Id") String adminId) {
        try {
            if (!adminService.isAdmin(new ObjectId(adminId))) {
                return ResponseEntity.status(403).body(Response.error(403, "Forbidden", "User is not authorized"));
            }
            User user = adminService.getUserWithDetails(new ObjectId(id));
            if (user == null) {
                return ResponseEntity.status(404).body(Response.error(404, "Not Found", "User not found"));
            }
            return ResponseEntity.ok(Response.success(200, "User details retrieved successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Response.error(500, "Internal Server Error", e.getMessage()));
        }
    }

    @GetMapping("/users/{id}/messages")
    public ResponseEntity<Response<List<Message>>> getUserMessages(@PathVariable String id,
                                                                   @RequestHeader("X-User-Id") String adminId) {
        try {
            if (!adminService.isAdmin(new ObjectId(adminId))) {
                return ResponseEntity.status(403).body(Response.error(403, "Forbidden", "User is not authorized"));
            }
            List<Message> messages = adminService.getAllMessagesForUser(new ObjectId(id));
            return ResponseEntity.ok(Response.success(200, "User messages retrieved successfully", messages));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Response.error(500, "Internal Server Error", e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Response<Void>> deleteUser(@PathVariable String id,
                                                     @RequestHeader("X-User-Id") String adminId) {
        try {
            if (!adminService.isAdmin(new ObjectId(adminId))) {
                return ResponseEntity.status(403).body(Response.error(403, "Forbidden", "User is not authorized"));
            }
            adminService.deleteUserAndData(new ObjectId(id));
            return ResponseEntity.ok(Response.success(204, "User deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Response.error(500, "Internal Server Error", e.getMessage()));
        }
    }
}