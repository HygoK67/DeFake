package com.group6.defakelogibackend.service;

import com.group6.defakelogibackend.model.Notification;

import java.util.LinkedList;
import java.util.List;

public interface NotificationService {

    public boolean deleteNotification(long notificationId);

    public Notification notificationInfo(long notificationId);

    public List<Notification> notificationFilter(long userId, String condition);
}
