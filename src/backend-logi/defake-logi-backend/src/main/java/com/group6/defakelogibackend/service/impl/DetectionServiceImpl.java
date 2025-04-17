package com.group6.defakelogibackend.service.impl;

import com.group6.defakelogibackend.exception.EntityMissingException;
import com.group6.defakelogibackend.exception.FileHandleException;
import com.group6.defakelogibackend.mapper.FigureMapper;
import com.group6.defakelogibackend.mapper.PaperMapper;
import com.group6.defakelogibackend.model.Figure;
import com.group6.defakelogibackend.model.Paper;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.service.DetectionService;
import com.group6.defakelogibackend.utils.ImageService;
import com.group6.defakelogibackend.utils.PDFService;
import com.group6.defakelogibackend.utils.TencentCOSService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DetectionServiceImpl implements DetectionService {

    @Autowired
    PaperMapper paperMapper;
    @Autowired
    FigureMapper figureMapper;
    @Autowired
    PDFService pdfService;
    @Autowired
    TencentCOSService cosService;
    @Autowired
    ImageService imageService;

    @Override
    @Transactional
    public void userUploadPaper(long userId, Paper paper) {
        paper.setUploaderId(userId);
        { // 将作者列表转换为由空格分隔的一整个字符串
            paper.setAuthors("");
            for (String author : paper.getAuthorList()) {
                paper.setAuthors(paper.getAuthors() + author + " ");
            }
            paper.setAuthors(paper.getAuthors().substring(0, paper.getAuthors().length() - 1));
        }
        File pdfFile = new File(
                System.getProperty("java.io.tmpdir") +
                "/" +
                paper.getFilePath().substring(paper.getFilePath().lastIndexOf('/'))
        );
        try {
            paper.setPageNum(pdfService.getPageNumber(pdfFile));
        } catch (IOException e) {
            throw new FileHandleException("尝试获取文件页数时发生错误");
        }
        paperMapper.addPaper(paper);
        // 尝试获取文件中的所有图像
        try {
            FileUtils.copyURLToFile(new URL(paper.getFilePath()), pdfFile);
        } catch (IOException e) {
            throw new FileHandleException("下载所选文件时出现错误");
        }
        List<File> imageFiles;
        try {
            imageFiles = pdfService.extractImages(pdfFile);
        } catch (Exception e) {
            throw new FileHandleException("从 PDF 提取图像时出现错误");
        }
        // 将所有的图像上传至 COS, 并将相关信息保存至数据库中
        for (File imageFile : imageFiles) {
            String figureFilePath;
            try {
                figureFilePath = cosService.upload(imageFile);
            } catch (Exception e) {
                throw new FileHandleException("向对象存储上传图像时发生错误");
            }
            Figure figure = new Figure();
            figure.setPaperId(paper.getId());
            figure.setFilePath(figureFilePath);
            userUploadFigure(userId, figure);
        }
    }

    @Override
    @Transactional
    public void userUploadFigure(long userId, Figure figure) {
        try {
            String base64Thumbnail = imageService.downloadAndGenerateThumbnail(figure.getFilePath(), 128, 128);
            figure.setThumbnail(base64Thumbnail);
        } catch (Exception e) {
            throw new FileHandleException("生成图片缩略图时发生错误!");
        }
        figure.setUploaderId(userId);
        figureMapper.addFigure(figure);
    }

    @Override
    @Transactional
    public Paper getPaperInfoById(long userId, long paperId) {
        Paper paper = paperMapper.getPaperById(paperId);
        if (paper == null) {
            throw new EntityMissingException(paperId + " 号论文不存在!");
        }
        return paper;
    }

    @Override
    public List<Figure> getImagesByPaperId(long userId, long paperId) {
        List<Figure> figures = figureMapper.getFiguresByPaperId(paperId);
        return figures;
    }

    @Override
    public Figure getFigureInfoById(long userId, long figureId) {
        Figure figure = figureMapper.getFigureById(figureId);
        if (figure == null) {
            throw new EntityMissingException(figureId + " 号图片不存在!");
        }
        return figure;
    }


}
