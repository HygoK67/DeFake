package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.LoggedIn;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.service.UserService;
import com.group6.defakelogibackend.utils.JWTService;
import com.group6.defakelogibackend.utils.OperationLogService;
import com.group6.defakelogibackend.utils.TencentCOSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@LoggedIn
public class FileController {

    @Autowired
    UserService userService;
    @Autowired
    JWTService jwtService;

    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("files") MultipartFile file, @RequestHeader String jwtToken) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        String url = userService.userUpload(userId, file);
        return Result.success(url);
    }
}
