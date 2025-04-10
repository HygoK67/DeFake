package com.group6.defakelogibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationLog {

    public enum OperationType {
        USER_LOGIN, USER_REGISTER, USER_FILE_UPLOAD
    }

    // 数据库表中有的信息
    private Long id;
    private Long userId;
    private OperationType operationType;
    private String operationDetailJsonString;
    private Map<String, Object> operationDetail;
    private String ipAddress;

    // 向前端返回的额外信息
    private String userEmail;
    private String userName;
    private String avatarURL;
}
