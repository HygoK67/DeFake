package com.group6.defakelogibackend.mapper;

import com.group6.defakelogibackend.model.OperationLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OperationLogMapper {

    public int addLog(OperationLog operationLog);

    public List<OperationLog> filterLog();

}
