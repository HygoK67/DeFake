package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.Admin;
import com.group6.defakelogibackend.model.*;
import com.group6.defakelogibackend.service.GroupService;
import com.group6.defakelogibackend.service.NotificationService;
import com.group6.defakelogibackend.service.UserService;
import com.group6.defakelogibackend.utils.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Admin
public class AdminController {

    @Autowired
    OperationLogService operationLogService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;

    @GetMapping("/user/all")
    public Result getAllUsers() {
        List<User> allUsers = userService.showAllUsers();
        return Result.success(allUsers);
    }

    @GetMapping("/group/all")
    public Result getAllGroups() {
        List<Group> allGroups = groupService.showAllGroups();
        return Result.success(allGroups);
    }

    @GetMapping("/operationLogs/all")
    public Result getOperationLog() {
        List<OperationLog> operationLogs = operationLogService.getOperationLog();
        return Result.success(operationLogs);
    }
}
