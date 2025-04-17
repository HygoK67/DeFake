package com.group6.defakelogibackend.mapper;

import com.group6.defakelogibackend.model.Paper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaperMapper {

    public int addPaper(Paper paper);

    public Paper getPaperById(long id);


}