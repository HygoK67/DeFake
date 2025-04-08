package com.group6.defakelogibackend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.defakelogibackend.mapper.OperationLogMapper;
import com.group6.defakelogibackend.mapper.UserMapper;
import com.group6.defakelogibackend.model.OperationLog;
import com.group6.defakelogibackend.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;

@Component
public class OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;
    @Autowired
    private UserMapper userMapper;

    public void addUserLoginLog(long userId, String email, String jwtToken) {
        OperationLog operationLog = new OperationLog();
        operationLog.setUserId(userId);
        operationLog.setOperationType(OperationLog.OperationType.USER_LOGIN);
        operationLog.setIpAddress(getIP());
        HashMap<String, Object> detail = new HashMap<>();
        detail.put("email", email);
        detail.put("jwtToken", jwtToken);
        try {
            String detailJsonString = new ObjectMapper().writeValueAsString(detail);
            operationLog.setOperationDetailJsonString(detailJsonString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        operationLogMapper.addLog(operationLog);
    }

    public void addUserUploadLog(long userId, String originalFileName, String url) {
        OperationLog operationLog = new OperationLog();
        operationLog.setUserId(userId);
        operationLog.setOperationType(OperationLog.OperationType.USER_FILE_UPLOAD);
        operationLog.setIpAddress(getIP());
        HashMap<String, Object> detail = new HashMap<>();
        detail.put("fileName", originalFileName);
        detail.put("url", url);
        try {
            String detailJsonString = new ObjectMapper().writeValueAsString(detail);
            operationLog.setOperationDetailJsonString(detailJsonString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        operationLogMapper.addLog(operationLog);
    }

    public void addUserRegisterLog(long userId, String email) {
        OperationLog operationLog = new OperationLog();
        operationLog.setUserId(userId);
        operationLog.setOperationType(OperationLog.OperationType.USER_REGISTER);
        operationLog.setIpAddress(getIP());
        HashMap<String, Object> detail = new HashMap<>();
        detail.put("email", email);
        try {
            String detailJsonString = new ObjectMapper().writeValueAsString(detail);
            operationLog.setOperationDetailJsonString(detailJsonString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        operationLogMapper.addLog(operationLog);
    }

    // 根据条件来筛选操作日志
    public List<OperationLog> getOperationLog() {
        List<OperationLog> logs = operationLogMapper.filterLog();
        for (OperationLog log : logs) {
            User user = userMapper.findUserById(log.getUserId());
            log.setUserName(user.getUsername());
            log.setUserEmail(user.getEmail());
            log.setAvatarURL(user.getAvatarPath());
        }
        return logs;
    }

    private String getIP() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 优先从 X-Forwarded-For 获取真实 IP
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP"); // 尝试从 X-Real-IP 获取
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP"); // 兼容某些代理
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // WebLogic 代理
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // 最后尝试直接获取 IP
        }
        // 如果通过代理访问，X-Forwarded-For 可能返回多个 IP，取第一个非未知的 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
