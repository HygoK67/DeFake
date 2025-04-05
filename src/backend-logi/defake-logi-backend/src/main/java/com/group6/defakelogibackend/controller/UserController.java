package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.service.UserService;
import com.group6.defakelogibackend.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;

    @GetMapping("/sendEmailCode")
    public Result sendVerifyCode(@RequestParam String email) {
        if (emailService.sendVerifyCode(email)) {
            return Result.success(null);
        } else {
            return Result.error("已发送过验证码，请稍后重试！");
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user, @RequestParam String verificationCode) {
        userService.registerUser(user, verificationCode);
        return Result.success(null);
    }

    @GetMapping("/login")
    public Result login(@RequestBody User user) {
        String jwtToken = userService.loginUser(user);
        return Result.success(jwtToken);
    }

    @PostMapping("/info")
    public Result info(@RequestBody Map<String, String> requestBody) {
        long userId = Long.parseLong(requestBody.get("userId"));
        User user = userService.userInfo(userId);
        if (user == null) {
            return Result.error("api/user/info出错，没有找到user");
        }
        return Result.success(user);
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String, String> requestBody) {
        long userId = Long.parseLong(requestBody.get("userId"));
        String oldPassword = requestBody.get("oldPassword");
        String newPassword = requestBody.get("newPassword");

        if (userService.updatePassword(userId, oldPassword, newPassword)) {
            return Result.success();
        }
        return Result.error("修改密码失败");
    }



}
