package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.LoggedIn;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.utils.TencentCOSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@LoggedIn
public class FileController {

    @Autowired
    TencentCOSService cosService;

    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String url = cosService.upload(file);
            return Result.success(url);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传文件失败，请重试!");
        }
    }
}
