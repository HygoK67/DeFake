package com.group6.defakelogibackend;

import com.group6.defakelogibackend.mapper.UserMapper;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.utils.EmailService;
import com.group6.defakelogibackend.utils.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class DefakeLogiBackendApplicationTests {

    @Autowired
    EmailService emailService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordService passwordService;

    @Test
    void contextLoads() {
    }

}