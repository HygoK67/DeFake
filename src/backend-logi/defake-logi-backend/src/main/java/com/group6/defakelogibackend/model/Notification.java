package com.group6.defakelogibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private long id;
    private int template_id;
    private long user_id;
    private String title;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;

}
