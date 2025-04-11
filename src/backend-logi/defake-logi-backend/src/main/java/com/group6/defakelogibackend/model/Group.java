package com.group6.defakelogibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private long id;
    private String groupname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String introduction;
    private String ddl;
}
