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
@LoggedIn
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    @Autowired
    JWTService jwtService;

    @PostMapping("/deleteNotification")
    public Result deleteNotification(@RequestBody Map<String, String> requestBody) {
        long notificationId = Long.parseLong(requestBody.get("notificationId"));
        if (notificationService.deleteNotification(notificationId)) {
            return Result.success();
        }
        return Result.error("删除通知失败");
    }

    @PostMapping("/deleteAllNotification")
    public Result deleteAllNotification(@RequestBody Map<String, String> requestBody) {
        long userId = Long.parseLong(requestBody.get("userId"));
        if (notificationService.deleteAllNotification(userId)) {
            return Result.success();
        }
        return Result.error("删除全部通知失败");
    }

    @PostMapping("/notificationInfo")
    @Admin
    public Result notificationInfo(@RequestBody Map<String, String> requestBody) {
        long notificationId = Long.parseLong(requestBody.get("notificationId"));
        Notification notification = notificationService.notificationInfo(notificationId);
        if (notification != null) {
            return Result.success(notification);
        }
        return Result.error("查看通知内容失败");
    }

    @PostMapping("/notificationFilter")
    public Result notificationFilter(@RequestHeader String jwtToken, @RequestBody Map<String, String> requestBody) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        String condition = requestBody.get("condition");
        List<Notification> list = notificationService.notificationFilter(userId, condition);
        if (list != null) {
            return Result.success(list);
        }
        return Result.error("筛选通知出现问题");
    }
}
