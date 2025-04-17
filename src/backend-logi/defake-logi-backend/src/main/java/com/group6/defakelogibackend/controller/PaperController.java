package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.LoggedIn;
import com.group6.defakelogibackend.model.Figure;
import com.group6.defakelogibackend.model.Paper;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.service.DetectionService;
import com.group6.defakelogibackend.utils.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paper")
@LoggedIn
public class PaperController {

    @Autowired
    DetectionService detectionService;
    @Autowired
    JWTService jwtService;

    @PostMapping
    public Result addPaper(@RequestBody Paper paper, @RequestHeader String jwtToken) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        detectionService.userUploadPaper(userId, paper);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("id", paper.getId());
        return Result.success(resultMap);
    }

    @GetMapping()
    public Result getPaper(@RequestParam long paperId, @RequestHeader String jwtToken) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        Paper paper = detectionService.getPaperInfoById(userId, paperId);
        return Result.success(paper);
    }

    @GetMapping("/figures")
    public Result getImagesOfPaper(@RequestParam long paperId, @RequestHeader String jwtToken) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        List<Figure> figureList = detectionService.getImagesByPaperId(userId, paperId);
        return Result.success(figureList);
    }

}
