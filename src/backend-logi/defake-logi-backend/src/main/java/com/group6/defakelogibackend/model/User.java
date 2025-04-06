package com.group6.defakelogibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {       // 用户类

    public enum UserRole {
        user, admin
    };

    private long id;
    private String username;
    private String email;
    private String phone;
    private String password;
    private String passwordHash;
    private String avatarPath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    private UserRole role;
}
