package com.group6.defakelogibackend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

@Component
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    private static final HashMap<String, String> codeMap = new HashMap<>();        // 记录验证码
    private static final HashMap<String, LocalDateTime> timeMap = new HashMap<>(); // 记录验证码过期时间

    private static final int CODE_EXPIRE_MINUTES = 2; // 建议延长过期时间

    public boolean sendVerifyCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));

        // 存入缓存中
        // 检查缓存中是否存在已经发送的 email
        if (timeMap.containsKey(email)) {
            if (timeMap.get(email).isAfter(LocalDateTime.now())) {
                return false;
            }
        }
        codeMap.put(email, code);
        timeMap.put(email, LocalDateTime.now().plusMinutes(CODE_EXPIRE_MINUTES));

        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ruangonggroup6@163.com");
        message.setTo(email);
        message.setSubject("DeFake 图像造假检测平台注册验证码");
        message.setText("您的验证码是：" + code + "，有效期" + CODE_EXPIRE_MINUTES + "分钟");
        mailSender.send(message);
        return true;
    }

    public boolean verifyCode(String email, String code) {
        if (codeMap.containsKey(email)) {
            return codeMap.get(email).equals(code);
        }
        return false;
    }
}