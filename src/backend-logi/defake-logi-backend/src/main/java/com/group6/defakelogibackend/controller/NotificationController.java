package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping("/deleteNotification")
    public Result deleteNotification(@RequestBody Map<String, String> requestBody) {
        long notificationId = Long.parseLong(requestBody.get("notificationId"));
        if (notificationService.deleteNotification(notificationId)) {
            return Result.success();
        }
        return Result.error("删除消息失败");
    }

    @PostMapping("/deleteAllNotification")
    public Result deleteAllNotification(@RequestBody Map<String, String> requestBody) {
        long userId = Long.parseLong(requestBody.get("userId"));
        if (notificationService.deleteAllNotification(userId)) {
            return Result.success();
        }
        return Result.error("删除全部消息失败");
    }
}
