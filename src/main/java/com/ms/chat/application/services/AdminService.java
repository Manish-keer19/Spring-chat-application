//package com.ms.chat.application.services;
//
//
//
//import com.ms.chat.application.Entity.Message;
//import com.ms.chat.application.Entity.User;
//import com.ms.chat.application.Entity.UserAdditionalDetail;
//import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class AdminService {
//
//    private final MongoTemplate mongoTemplate;
//
//    public AdminService(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }
//
//    // Get all users with their details
//    public List<User> getAllUsersWithDetails() {
//        return mongoTemplate.findAll(User.class);
//    }
//
//    // Get user by ID with all details
//    public User getUserWithDetails(ObjectId userId) {
//        return mongoTemplate.findById(userId, User.class);
//    }
//
//    // Get all messages for a specific user
//    public List<Message> getAllMessagesForUser(ObjectId userId) {
//        Query query = new Query();
//        query.addCriteria(new Criteria().orOperator(
//                Criteria.where("currentUser").is(userId),
//                Criteria.where("anotherUser").is(userId)
//        ));
//        return mongoTemplate.find(query, Message.class);
//    }
//
//    // Delete user and all associated data
//    public void deleteUserAndData(ObjectId userId) {
//        // First find the user to check for profile details
//        User user = mongoTemplate.findById(userId, User.class);
//
//        if (user != null) {
//            // Delete user additional details if exists
//            if (user.getProfileDetail() != null) {
//                mongoTemplate.remove(user.getProfileDetail());
//            }
//
//            // Delete all messages associated with this user
//            Query messageQuery = new Query();
//            messageQuery.addCriteria(new Criteria().orOperator(
//                    Criteria.where("currentUser").is(userId),
//                    Criteria.where("anotherUser").is(userId)
//            ));
//            mongoTemplate.remove(messageQuery, Message.class);
//
//            // Finally delete the user
//            mongoTemplate.remove(user);
//        }
//    }
//
//    // Check if user is admin
//    public boolean isAdmin(ObjectId userId) {
//        User user = mongoTemplate.findById(userId, User.class);
//        return user != null && user.getRole() != null && user.getRole().contains("ADMIN");
//    }
//}
//
//
//
//
//



package com.ms.chat.application.services;

import com.ms.chat.application.Entity.Message;
import com.ms.chat.application.Entity.User;
import com.ms.chat.application.Entity.UserAdditionalDetail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final MongoTemplate mongoTemplate;

    public AdminService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Total users count
        stats.put("totalUsers", mongoTemplate.count(new Query(), User.class));

        // Active users (last 30 days)
        Date thirtyDaysAgo = Date.from(LocalDateTime.now().minusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        stats.put("activeUsers", mongoTemplate.count(
                Query.query(Criteria.where("lastActive").gte(thirtyDaysAgo)),
                User.class
        ));

        // Admin count
        stats.put("adminCount", mongoTemplate.count(
                Query.query(Criteria.where("role").in("ADMIN")),
                User.class
        ));

        // New users (last 7 days)
        Date sevenDaysAgo = Date.from(LocalDateTime.now().minusDays(7).atZone(ZoneId.systemDefault()).toInstant());
        stats.put("newUsers", mongoTemplate.count(
                Query.query(Criteria.where("createdAt").gte(sevenDaysAgo)),
                User.class
        ));

        return stats;
    }

    public List<User> getAllUsersWithDetails() {
        return mongoTemplate.findAll(User.class);
    }

    public User getUserWithDetails(ObjectId userId) {
        return mongoTemplate.findById(userId, User.class);
    }

    public List<Message> getAllMessagesForUser(ObjectId userId) {
        Query query = Query.query(new Criteria().orOperator(
                Criteria.where("currentUser").is(userId),
                Criteria.where("anotherUser").is(userId)
        ));
        return mongoTemplate.find(query, Message.class);
    }

    public void deleteUserAndData(ObjectId userId) {
        User user = mongoTemplate.findById(userId, User.class);
        if (user != null) {
            if (user.getProfileDetail() != null) {
                mongoTemplate.remove(user.getProfileDetail());
            }

            Query messageQuery = Query.query(new Criteria().orOperator(
                    Criteria.where("currentUser").is(userId),
                    Criteria.where("anotherUser").is(userId)
            ));
            mongoTemplate.remove(messageQuery, Message.class);
            mongoTemplate.remove(user);
        }
    }

    public boolean isAdmin(ObjectId userId) {
        User user = mongoTemplate.findById(userId, User.class);
        return user != null && user.getRole() != null && user.getRole().contains("ADMIN");
    }
}