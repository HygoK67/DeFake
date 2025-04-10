package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.Admin;
import com.group6.defakelogibackend.annotation.LoggedIn;
import com.group6.defakelogibackend.mapper.UserToGroupMapper;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.service.UserService;
import com.group6.defakelogibackend.utils.EmailService;
import com.group6.defakelogibackend.utils.JWTService;
import com.group6.defakelogibackend.utils.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;
    @Autowired
    JWTService jwtService;
    @Autowired
    private UserToGroupMapper userToGroupMapper;

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

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        String jwtToken = userService.loginUser(user);
        return Result.success(jwtToken);
    }

    @LoggedIn
    @GetMapping("/info")
    public Result info(@RequestHeader String jwtToken) {
        long id = Long.parseLong(jwtService.getUserId(jwtToken));
        User user = userService.getUserInfo(id);
        return Result.success(user);
    }

    @LoggedIn
    @PutMapping("/info")
    public Result updateInfo(
            @RequestBody User user,
            @RequestHeader String jwtToken,
            @RequestBody Map<String, String> map,
            @RequestParam(required = false) String verificationCode
    ) {
        long id = Long.parseLong(jwtService.getUserId(jwtToken));
        user.setId(id);
        userService.updateUserInfo(user, map.get("oldPassword"), verificationCode);
        return Result.success();
    }

    @Admin
    @GetMapping("/all")
    public Result userAll() {
        List<User> list = userService.showAllUsers();
        return Result.success(list);
    }
}
