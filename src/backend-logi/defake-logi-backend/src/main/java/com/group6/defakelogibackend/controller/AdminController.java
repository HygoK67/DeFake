package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.Admin;
import com.group6.defakelogibackend.model.OperationLog;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.utils.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Admin
public class AdminController {

    @Autowired
    OperationLogService operationLogService;

    @GetMapping("userInfo")
    public Result getUserInfo() {
        return Result.success();
    }

    @GetMapping("/operationLog")
    public Result getOperationLog() {
        List<OperationLog> operationLogs = operationLogService.getOperationLog();
        return Result.success(operationLogs);
    }

}
