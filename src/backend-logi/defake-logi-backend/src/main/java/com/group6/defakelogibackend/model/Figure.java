package com.group6.defakelogibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Figure {

    private Long id;
    private Long paperId;
    private Long uploaderId;
    private String filePath;
    private String thumbnail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
