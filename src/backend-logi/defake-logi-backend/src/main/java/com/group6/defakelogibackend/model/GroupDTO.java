package com.group6.defakelogibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {

    private Long id;
    private String groupname;
    private String introduction;
    private String ddl;
    private UserToGroup.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
