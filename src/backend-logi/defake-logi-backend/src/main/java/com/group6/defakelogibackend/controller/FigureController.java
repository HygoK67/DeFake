package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.LoggedIn;
import com.group6.defakelogibackend.model.Figure;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.service.DetectionService;
import com.group6.defakelogibackend.utils.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/figure")
@LoggedIn
public class FigureController {

    @Autowired
    private DetectionService detectionService;
    @Autowired
    private JWTService jwtService;

    @PostMapping
    public Result addFigure(@RequestBody Figure figure, @RequestHeader String jwtToken) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        figure.setPaperId(null);
        detectionService.userUploadFigure(userId, figure);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("id", figure.getId());
        return Result.success(resultMap);
    }

    @GetMapping
    public Result getFigureById(@RequestParam Long figureId ,@RequestHeader String jwtToken) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        Figure figure = detectionService.getFigureInfoById(userId, figureId);
        return Result.success(figure);
    }
}
