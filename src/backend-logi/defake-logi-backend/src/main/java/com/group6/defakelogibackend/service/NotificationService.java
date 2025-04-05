package com.group6.defakelogibackend.service;

import com.group6.defakelogibackend.model.Notification;

public interface NotificationService {

    public boolean deleteNotification(long notificationId);

    public boolean deleteAllNotification(long userId);
}
