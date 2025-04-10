package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.Admin;
import com.group6.defakelogibackend.annotation.LoggedIn;
import com.group6.defakelogibackend.model.Notification;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.service.NotificationService;
import com.group6.defakelogibackend.service.UserService;
import com.group6.defakelogibackend.utils.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    @Autowired
    JWTService jwtService;

    @LoggedIn
    @GetMapping("/filter")
    public Result notificationFilter(@RequestHeader String jwtToken, @RequestParam("condition") String condition) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        List<Notification> list = notificationService.notificationFilter(userId, condition);
        return Result.success(list);
    }

    @LoggedIn
    @PutMapping("/read")
    public Result readNotification(@RequestHeader String jwtToken, @RequestBody Map<String, String> requestBody){
        long userIdRec = Long.parseLong(jwtService.getUserId(jwtToken));
        long notificationId = Long.parseLong(requestBody.get("notificationId"));
        notificationService.readNotification(userIdRec, notificationId);
        return Result.success();
    }
}
