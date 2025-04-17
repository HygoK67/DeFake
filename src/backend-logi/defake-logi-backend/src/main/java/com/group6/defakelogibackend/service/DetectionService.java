package com.group6.defakelogibackend.service;

import com.group6.defakelogibackend.model.Figure;
import com.group6.defakelogibackend.model.Paper;

import java.util.List;

public interface DetectionService {

    public void userUploadPaper(long userId, Paper paper);

    public void userUploadFigure(long userId, Figure figure);

    public Paper getPaperInfoById(long userId, long paperId);

    public List<Figure> getImagesByPaperId(long userId, long paperId);

    public Figure getFigureInfoById(long userId, long figureId);

}
