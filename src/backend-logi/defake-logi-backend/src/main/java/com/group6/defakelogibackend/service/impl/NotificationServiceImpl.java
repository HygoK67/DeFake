package com.group6.defakelogibackend.service.impl;

import com.group6.defakelogibackend.mapper.NotificationMapper;
import com.group6.defakelogibackend.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

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

    @Override
    @Transactional
    public Notification notificationInfo(long notificationId) {
        return notificationMapper.findNotificationById(notificationId);
    }

    @Override
    @Transactional
    public List<Notification> notificationFilter(long userId, String condition) {
        if (condition.equals("send_at_desc")) {
            return notificationMapper.findNotificationByUserId_sent_at_desc(userId);
        } else if (condition.equals("send_at_asc")) {
            return notificationMapper.findNotificationByUserId_sent_at_asc(userId);
        } else {
            return null;
        }
    }
}
