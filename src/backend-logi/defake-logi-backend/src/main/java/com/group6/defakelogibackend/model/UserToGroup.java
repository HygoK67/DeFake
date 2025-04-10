package com.group6.defakelogibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserToGroup {

    public enum Status {
        in, pending_apply, pending_invite
    }

    public enum Role {
        leader, member
    }

    private long id;
    private long userId;
    private long groupId;
    private LocalDateTime createdAt;
    private Status status;
    private Role role;
}
