package com.group6.defakelogibackend;

import com.group6.defakelogibackend.mapper.FigureMapper;
import com.group6.defakelogibackend.mapper.PaperMapper;
import com.group6.defakelogibackend.model.Figure;
import com.group6.defakelogibackend.model.Paper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DefakeLogiBackendApplicationTests {

    @Autowired
    PaperMapper paperMapper;

    @Autowired
    FigureMapper figureMapper;

    @Test
    void contextLoads() {
//        paperMapper.addPaper(new Paper(
//                null,
//                (long) 100,
//                "基于 xx 的 xx 研究",
//                "王xx 李xx 开xx",
//                "这是一个摘要",
//                "1002.10.12",
//                20,
//                "https://asdasd",
//                null,
//                null,
//                null
//        ));
//        String test = System.getProperty("java.io.tmpdir");
//        System.out.println(test);
    }

}