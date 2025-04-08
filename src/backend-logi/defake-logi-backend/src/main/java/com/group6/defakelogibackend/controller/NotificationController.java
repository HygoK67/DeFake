package com.group6.defakelogibackend.controller;

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
    @DeleteMapping("/delete")
    public Result deleteNotification(@RequestBody Map<String, String> requestBody) {
        long notificationId = Long.parseLong(requestBody.get("notificationId"));
        if (notificationService.deleteNotification(notificationId)) {
            return Result.success();
        }
        return Result.error("删除通知失败");
    }

    @LoggedIn
    @DeleteMapping("/clear")
    public Result deleteAllNotification(@RequestHeader String jwtToken) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        if (notificationService.deleteAllNotification(userId)) {
            return Result.success();
        }
        return Result.error("删除全部通知失败");
    }

    @LoggedIn
    @GetMapping("/filter")
    public Result notificationFilter(@RequestHeader String jwtToken, @RequestParam("condition") String condition) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        List<Notification> list = notificationService.notificationFilter(userId, condition);
        return Result.success(list);
    }
}
