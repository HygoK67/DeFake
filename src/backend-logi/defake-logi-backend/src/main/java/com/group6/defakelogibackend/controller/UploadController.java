package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.LoggedIn;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/upload")
@LoggedIn
public class UploadController {

}
