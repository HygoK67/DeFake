package com.group6.defakelogibackend.service;

import com.group6.defakelogibackend.model.Group;
import com.group6.defakelogibackend.model.GroupDTO;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.model.UserToGroup;

import java.util.List;

public interface GroupService {
    public boolean createGroup(long userId, String groupname);

    public boolean applyGroup(long userId_sent, long groupId);

    public boolean inviteGroup(long userIdSent, long userIdRec, long groupId, String title, String content);

    public boolean kickGroup(long userIdSent, long userIdRec, long groupId);

    public List<Group> showAllGroups();

    public List<UserToGroup> groupMembers(long groupId);

    public List<Group> searchGroup(String groupname);

    public List<GroupDTO> listGroupByUser(long userId);

}
