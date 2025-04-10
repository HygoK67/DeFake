package com.group6.defakelogibackend.mapper;

import com.group6.defakelogibackend.model.Author;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthorMapper {

    public List<Author> findAuthor(Long id, String nameSubString);

    public int addAuthor(Author author);

}
