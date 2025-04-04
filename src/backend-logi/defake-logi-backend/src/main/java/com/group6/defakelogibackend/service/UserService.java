package com.group6.defakelogibackend.service;

import com.group6.defakelogibackend.model.User;
import org.springframework.stereotype.Service;

public interface UserService {

    // 用户注册
    public void registerUser(User user, String verificationCode);

    // 用户登录，成功时返回 jwt 令牌
    public String loginUser(User user);

    public User userInfo(long userId);

    public boolean updatePassword(long userId, String oldPassword, String newPassword);

}
