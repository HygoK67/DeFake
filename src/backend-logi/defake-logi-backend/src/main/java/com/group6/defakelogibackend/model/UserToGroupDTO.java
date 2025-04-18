package com.group6.defakelogibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserToGroupDTO {
    private long id;
    private long userId;
    private String username;
    private String email;
    private long groupId;
    private LocalDateTime createdAt;
    private UserToGroup.Status status;
    private UserToGroup.Role role;
}
