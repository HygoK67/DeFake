package com.group6.defakelogibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paper {

    private Long id;
    private Long uploaderId;
    private String title;
    private String authors;
    private String abstracT;
    private String doi;
    private int pageNum;
    private String filePath;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
