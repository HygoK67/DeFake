package com.group6.defakelogibackend.service.impl;

import com.group6.defakelogibackend.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements com.group6.defakelogibackend.service.NotificationService {
    @Autowired
    NotificationMapper notificationMapper;

    @Override
    @Transactional
    public boolean deleteNotification(long notificationId) {
        // 检查是否存在该ID的消息
        if (notificationMapper.findNotificationById(notificationId) != null) {
            notificationMapper.deleteNotificationById(notificationId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteAllNotification(long userId) {
        notificationMapper.deleteAllNotificationByUserId(userId);
        return true;
    }
}
