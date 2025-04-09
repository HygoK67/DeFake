package com.group6.defakelogibackend.service.impl;

import com.group6.defakelogibackend.exception.EntityMissingException;
import com.group6.defakelogibackend.exception.FieldMissingException;
import com.group6.defakelogibackend.mapper.NotificationMapper;
import com.group6.defakelogibackend.mapper.UserMapper;
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
    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional
    public boolean deleteNotification(long notificationId) {
        // 检查是否存在该ID的消息
        if (notificationMapper.findNotificationById(notificationId) == null) {
            throw new EntityMissingException("notificationId 错误，不存在对应的notification!");
        }
        notificationMapper.deleteNotificationById(notificationId);
        return true;
    }

    @Override
    @Transactional
    public Notification notificationInfo(long notificationId) {
        Notification notification = notificationMapper.findNotificationById(notificationId);
        if (notification == null) {
            throw new EntityMissingException("notificationId 错误, 找不到对应的通知!");
        }
        return notification;
    }

    @Override
    @Transactional
    public List<Notification> notificationFilter(long userId, String condition) {
        if (userMapper.findUserById(userId) == null){
            throw new EntityMissingException("userId 错误，不存在对应的user!");
        }
        if (condition.equals("sent_at_desc")) {
            return notificationMapper.findNotificationByUserId_sent_at_desc(userId);
        } else if (condition.equals("sent_at_asc")) {
            return notificationMapper.findNotificationByUserId_sent_at_asc(userId);
        } else {
            throw new FieldMissingException("输入的 condition 字段暂不支持!");
        }
    }

    @Override
    @Transactional
    public List<Notification> showAllNotifications(){
        return notificationMapper.getAllNotifications();
    }
}
