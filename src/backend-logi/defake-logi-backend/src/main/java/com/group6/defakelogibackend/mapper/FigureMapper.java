package com.group6.defakelogibackend.mapper;

import com.group6.defakelogibackend.model.Figure;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FigureMapper {

    public int addFigure(Figure figure);

    public List<Figure> getFiguresByPaperId(long paperId);

    public Figure getFigureById(long id);

}
