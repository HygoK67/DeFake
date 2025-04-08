package com.group6.defakelogibackend.service;

public interface GroupService {
    public boolean createGroup(long userId, String groupname);

    public boolean applyGroup(long userId_sent, long userId_rec, long groupId, String title, String content);

    public boolean inviteGroup(long userId_sent, long userId_rec, long groupId, String title, String content);

    public boolean kickGroup(long userId, long groupId);
}
