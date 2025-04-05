package com.group6.defakelogibackend.mapper;

import com.group6.defakelogibackend.model.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {

    public Notification findNotificationById(long notificationId);

    public void deleteNotificationById(long notificationId);

    public void deleteAllNotificationByUserId(long userId);
}
