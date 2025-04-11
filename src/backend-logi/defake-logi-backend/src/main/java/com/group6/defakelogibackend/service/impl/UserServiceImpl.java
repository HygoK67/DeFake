package com.group6.defakelogibackend.service.impl;

import com.group6.defakelogibackend.exception.*;
import com.group6.defakelogibackend.mapper.UserMapper;
import com.group6.defakelogibackend.mapper.UserToGroupMapper;
import com.group6.defakelogibackend.model.Group;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements com.group6.defakelogibackend.service.UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    EmailService emailService;
    @Autowired
    JWTService jwtService;
    @Autowired
    OperationLogService operationLogService;
    @Autowired
    TencentCOSService cosService;
    @Autowired
    UserToGroupMapper userToGroupMapper;

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
        if (userMapper.findUserByPhone(user.getPhone()) != null) {
            throw new EntityDuplicateException("手机号已被注册，请换一个手机号!");
        }
        if (!emailService.verifyCode(user.getEmail(), verificationCode)) {
            throw new AuthenticationFailedException("邮箱验证码错误!");
        }
        user.setPasswordHash(PasswordService.encodePassword(user.getPassword()));
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
        if (!PasswordService.matches(user.getPassword(), userFound.getPasswordHash())) {
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
    public User getUserInfo(long userId) {
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
        } catch (Exception e) {
            throw new FileHandleException("上传文件失败, 请稍后重试!");
        }
        operationLogService.addUserUploadLog(userId, file.getOriginalFilename(), url);
        return url;
    }

    @Override
    @Transactional
    public void updateUserInfo(User user, String oldPassword, String verificationCode) {
        // user 实体中包装了实体的id，oldPassword 可能为 null
        boolean newPassExists = user.getPassword() != null && !user.getPassword().isEmpty();
        boolean oldPassExists = oldPassword != null && !oldPassword.isEmpty();
        // 找到目前操作的用户的老信息
        User userFound = userMapper.findUserById(user.getId());
        if (newPassExists != oldPassExists) {
            throw new FieldMissingException("请确保旧密码和新密码都存在!");
        }
        if (newPassExists) { // 如果用户需要更改密码
            if (oldPassword.equals(user.getPassword())) { // 检测新旧密码是否相同
                throw new EntityDuplicateException("旧密码不能和新密码相同!");
            }
            // 检查用户输入的旧密码是否正确
            String realOldPasswordHash = userFound.getPasswordHash();
            if (!PasswordService.matches(oldPassword, realOldPasswordHash)) {
                throw new AuthenticationFailedException("旧密码不正确!");
            }
            // 加密用户的新密码
            String hashedPassword = PasswordService.encodePassword(user.getPassword());
            user.setPasswordHash(hashedPassword);
        }
        // 检查手机号是否与自己，与其他人重复
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            if (user.getPhone().equals(userFound.getPhone())) {
                throw new EntityDuplicateException("手机号重复，与之前手机号相同!");
            }
            User userAnother = userMapper.findUserByPhone(user.getPhone());
            if (userAnother != null && userAnother.getId() != userFound.getId()) {
                throw new EntityDuplicateException("手机号已被占用，请更换后再尝试");
            }
        }
        // 如果要更改邮箱，对邮箱进行验证
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (user.getEmail().equals(userFound.getEmail())) {
                throw new EntityDuplicateException("不可与之前的邮箱相同!");
            }
            User userAnother = userMapper.findUserByEmail(user.getEmail());
            if (userAnother != null && userAnother.getId() != userFound.getId()) {
                throw new EntityDuplicateException("该邮箱已被占用，请换一个!");
            }
            if (verificationCode == null || verificationCode.isEmpty()) {
                throw new FieldMissingException("请输入邮箱验证码!");
            }
            if (!emailService.verifyCode(user.getEmail(), verificationCode)) {
                throw new AuthenticationFailedException("邮箱验证码不正确!");
            }
        }
        userMapper.updateUser(user);
    }

    @Override
    @Transactional
    public List<User> showAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    @Transactional
    public long getUserId(String email) {
        return userMapper.findUserByEmail(email).getId();
    }
}
