package com.group6.defakelogibackend.service.impl;

import com.group6.defakelogibackend.exception.AuthenticationFailedException;
import com.group6.defakelogibackend.exception.EntityDuplicateException;
import com.group6.defakelogibackend.exception.FieldMissingException;
import com.group6.defakelogibackend.mapper.UserMapper;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.utils.EmailService;
import com.group6.defakelogibackend.utils.JWTService;
import com.group6.defakelogibackend.utils.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return jwtService.generateJWT(userFound.getId());
    }

    @Override
    @Transactional
    public User userInfo(long userId) {
        // 如果findUserById找不到用户的话，会返回null
        User user = userMapper.findUserById(userId);
        return user;
    }

    @Override
    @Transactional
    public boolean updatePassword(long userId, String oldPassword, String newPassword) {
        User user = userMapper.findUserById(userId);
        if (user == null) {
            return false;
        }
        System.out.println(user.getPasswordHash());
        if (!passwordService.matches(oldPassword, user.getPasswordHash())) {
            return false;
        }

        user.setPassword(newPassword);
        user.setPasswordHash(passwordService.encodePassword(newPassword));

        userMapper.updateUserPassword(user);
        return true;
    }
}
