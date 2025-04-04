package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.service.UserService;
import com.group6.defakelogibackend.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;

    @GetMapping("/sendEmailCode")
    public Result sendVerifyCode(@RequestParam String email) {
        if(emailService.sendVerifyCode(email)) {
            return Result.success(null);
        }
        else {
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

}
