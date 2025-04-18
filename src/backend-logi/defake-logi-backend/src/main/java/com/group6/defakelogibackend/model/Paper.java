package com.group6.defakelogibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paper {

    private Long id;
    private Long uploaderId;
    private String title;
    private String[] authorList;
    private String authors;
    private String abstracT;
    private String doi;
    private int pageNum;
    private String filePath;
    private LocalDate publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
