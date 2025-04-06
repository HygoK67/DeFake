package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.model.Notification;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.service.NotificationService;
import com.group6.defakelogibackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping("/delete")
    public Result deleteNotification(@RequestBody Map<String, String> requestBody) {
        long notificationId = Long.parseLong(requestBody.get("notificationId"));
        if (notificationService.deleteNotification(notificationId)) {
            return Result.success();
        }
        return Result.error("删除通知失败");
    }

    @PostMapping("/clear")
    public Result deleteAllNotification(@RequestBody Map<String, String> requestBody) {
        long userId = Long.parseLong(requestBody.get("userId"));
        if (notificationService.deleteAllNotification(userId)) {
            return Result.success();
        }
        return Result.error("删除全部通知失败");
    }

    @PostMapping("/info")
    public Result notificationInfo(@RequestBody Map<String, String> requestBody) {
        long notificationId = Long.parseLong(requestBody.get("notificationId"));
        Notification notification = notificationService.notificationInfo(notificationId);
        if (notification != null) {
            return Result.success(notification);
        }
        return Result.error("查看通知内容失败");
    }

    @PostMapping("/filter")
    public Result notificationFilter(@RequestBody Map<String, String> requesetBody) {
        long userId = Long.parseLong(requesetBody.get("userId"));
        String condition = requesetBody.get("condition");
        List<Notification> list = notificationService.notificationFilter(userId, condition);
        if (list != null) {
            return Result.success(list);
        }
        return Result.error("筛选通知出现问题");
    }
}
