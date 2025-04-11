package com.group6.defakelogibackend.service;

import com.group6.defakelogibackend.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    // 用户注册
    public void registerUser(User user, String verificationCode);

    // 用户登录，成功时返回 jwt 令牌
    public String loginUser(User user);

    public User getUserInfo(long userId);

    public String userUpload(long userId, MultipartFile file);

    public void updateUserInfo(User user, String oldPassword, String verificationCode);

    public List<User> showAllUsers();

    public long getUserId(String email);

}
