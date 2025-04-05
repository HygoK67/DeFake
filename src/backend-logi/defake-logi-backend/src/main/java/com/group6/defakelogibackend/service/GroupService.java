package com.group6.defakelogibackend.service;

public interface GroupService {
    public boolean createGroup(long userId, String groupname);

    public boolean applyGroup(long userId, long groupId, String title, String content);

    public boolean inviteGroup(long userId, long groupId, String title, String content);
}
