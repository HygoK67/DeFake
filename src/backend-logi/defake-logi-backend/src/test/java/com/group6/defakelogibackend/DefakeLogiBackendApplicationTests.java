package com.group6.defakelogibackend;

import com.group6.defakelogibackend.mapper.AuthorMapper;
import com.group6.defakelogibackend.mapper.UserMapper;
import com.group6.defakelogibackend.model.Author;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.utils.EmailService;
import com.group6.defakelogibackend.utils.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class DefakeLogiBackendApplicationTests {

    @Autowired
    AuthorMapper authorMapper;

    @Test
    void contextLoads() {
//        List<Author> authors = authorMapper.findAuthor(null, "Shi");
//        for (Author author : authors) {
//            System.out.println(author.getName());
//        }
    }

}