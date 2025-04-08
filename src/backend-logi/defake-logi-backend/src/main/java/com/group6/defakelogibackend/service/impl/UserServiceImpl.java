package com.group6.defakelogibackend.service.impl;

import com.group6.defakelogibackend.exception.*;
import com.group6.defakelogibackend.mapper.UserMapper;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements com.group6.defakelogibackend.service.UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    EmailService emailService;
    @Autowired
    PasswordService passwordService;
    @Autowired
    JWTService jwtService;
    @Autowired
    OperationLogService operationLogService;
    @Autowired
    TencentCOSService cosService;

    @Override
    @Transactional
    public void registerUser(User user, String verificationCode) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new FieldMissingException("用户名不可为空!");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new FieldMissingException("用户邮箱不可为空!");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new FieldMissingException("用户密码不可为空!");
        }
        if (userMapper.findUserByEmail(user.getEmail()) != null) {
            throw new EntityDuplicateException("邮箱已被注册，请换一个邮箱!");
        }
        if (userMapper.findUserByEmail(user.getEmail()) != null) {
            throw new EntityDuplicateException("手机号已被注册，请换一个手机号!");
        }
        if (!emailService.verifyCode(user.getEmail(), verificationCode)) {
            throw new AuthenticationFailedException("邮箱验证码错误!");
        }
        user.setPasswordHash(passwordService.encodePassword(user.getPassword()));
        userMapper.addUser(user);
        operationLogService.addUserRegisterLog(user.getId(), user.getEmail());
    }

    @Override
    @Transactional
    public String loginUser(User user) {
        // 保证传入的用户有 email 和 password 字段
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new FieldMissingException("用户邮箱不可为空!");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new FieldMissingException("用户密码不可为空!");
        }
        User userFound = userMapper.findUserByEmail(user.getEmail());
        if (userFound == null) { // 可能找不到给定的邮箱对应的用户
            throw new AuthenticationFailedException("用户不存在!");
        }
        if (!passwordService.matches(user.getPassword(), userFound.getPasswordHash())) {
            System.out.println(userFound.getId());
            throw new AuthenticationFailedException("用户邮箱和密码组合不正确!");
        }
        // 更新登录时间
        User tmpUser = new User();
        tmpUser.setId(userFound.getId());
        tmpUser.setLastLoginAt(LocalDateTime.now());
        userMapper.updateUser(tmpUser);
        // 生成登录令牌
        String jwtToken = jwtService.generateJWT(userFound.getId(), userFound.getEmail(), userFound.getRole());
        // 记录日志
        operationLogService.addUserLoginLog(userFound.getId(), userFound.getEmail(), jwtToken);
        return jwtToken;
    }

    @Override
    @Transactional
    public User userInfo(long userId) {
        // 如果findUserById找不到用户的话，会返回null
        User user = userMapper.findUserById(userId);
        if (user == null) {
            throw new EntityMissingException("id 错误, 找不到对应的用户!");
        }
        user.setPasswordHash(null);
        return user;
    }

    @Override
    @Transactional
    public String userUpload(long userId, MultipartFile file) {
        String url;
        try {
            url = cosService.upload(file);
        }
        catch (Exception e) {
            throw new FileHandleException("上传文件失败, 请稍后重试!");
        }
        operationLogService.addUserUploadLog(userId, file.getOriginalFilename(), url);
        return url;
    }

    @Override
    @Transactional
    public boolean updatePassword(long userId, String oldPassword, String newPassword) {
        User user = userMapper.findUserById(userId);
        if (user == null) {
            throw new EntityMissingException("id 错误, 找不到对应的用户!");
        }
        if (!passwordService.matches(oldPassword, user.getPasswordHash())) {
            throw new AuthenticationFailedException("密码错误");
        }

        user.setPassword(newPassword);
        user.setPasswordHash(passwordService.encodePassword(newPassword));

        userMapper.updateUserPassword(user);
        return true;
    }
}
