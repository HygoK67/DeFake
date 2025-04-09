package com.group6.defakelogibackend.service;

import com.group6.defakelogibackend.model.Group;

import java.util.List;

public interface GroupService {
    public boolean createGroup(long userId, String groupname);

    public boolean applyGroup(long userId_sent, long userId_rec, long groupId, String title, String content);

    public boolean inviteGroup(long userIdSent, long userIdRec, long groupId, String title, String content);

    public boolean kickGroup(long userId_sent, long userId_rec, long groupId);

    public List<Group> showAllGroups();
}
