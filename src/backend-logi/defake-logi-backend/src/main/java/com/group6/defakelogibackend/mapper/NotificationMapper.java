package com.group6.defakelogibackend.mapper;

import com.group6.defakelogibackend.model.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {

    public Notification findNotificationById(long notificationId);

    public void deleteNotificationById(long notificationId);

    public List<Notification> findNotificationByUserId_sent_at_desc(long userId);

    public List<Notification> findNotificationByUserId_sent_at_asc(long userId);

    public void createNotificationUser2User(long userId_sent, long userId_rec, long groupId, String title, String content);

    public List<Notification> getAllNotifications();

    public void updateNotificationReadAt(long userIdRec, long notificationId);
}
