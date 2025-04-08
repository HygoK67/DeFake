package com.group6.defakelogibackend.service.impl;

import com.group6.defakelogibackend.exception.EntityDuplicateException;
import com.group6.defakelogibackend.exception.EntityMissingException;
import com.group6.defakelogibackend.mapper.GroupMapper;
import com.group6.defakelogibackend.mapper.NotificationMapper;
import com.group6.defakelogibackend.mapper.UserToGroupMapper;
import com.group6.defakelogibackend.model.Group;
import com.group6.defakelogibackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class GroupServiceImpl implements com.group6.defakelogibackend.service.GroupService {

    @Autowired
    GroupMapper groupMapper;
    @Autowired
    UserToGroupMapper userToGroupMapper;
    @Autowired
    NotificationMapper notificationMapper;

    @Override
    @Transactional
    public boolean createGroup(long userId, String groupname) {
        // 如果该用户已经创建过同名的组织
        if (groupMapper.findGroupByUserIdAndGroupname(userId, groupname) != null) {
            throw new EntityDuplicateException("该 userId 已经创建过相同 groupname 的组织!");
        }
        groupMapper.createGroup(userId, groupname);
        long groupId = groupMapper.findGroupByUserIdAndGroupname(userId, groupname).getId();
        userToGroupMapper.addUserToGroup(userId, groupId);
        return true;
    }

    @Override
    @Transactional
    public boolean applyGroup(long userId, long groupId, String title, String content) {
        Group group = groupMapper.findGroupByGroupId(groupId);
        // 如果groupId无效
        if (group == null) {
            throw new EntityMissingException("groupId 无效!");
        }
        // 如果该用户已经加入该组织，则不能发送申请
        if (userToGroupMapper.findUserToGroup(userId, groupId) != null) {
            throw new EntityDuplicateException("该用户已经加入该组织!");
        }
        long groupLeaderId = group.getGroupLeaderId();
        notificationMapper.createNotificationUser2User(groupLeaderId, userId, title, content);
        return true;
    }

    @Override
    @Transactional
    public boolean inviteGroup(long userId, long groupId, String title, String content) {
        Group group = groupMapper.findGroupByGroupId(groupId);
        // 如果groupId无效
        if (group == null) {
            return false;
        }
        // 如果该用户已经加入该组织，则不能发送申请
        if (userToGroupMapper.findUserToGroup(userId, groupId) != null) {
            return false;
        }
        long groupLeaderId = group.getGroupLeaderId();
        notificationMapper.createNotificationUser2User(userId, groupLeaderId, title, content);
        return true;
    }

    @Override
    @Transactional
    public boolean kickGroup(long userId, long groupId) {
        Group group = groupMapper.findGroupByGroupId(groupId);
        // 如果groupId无效
        if (group == null) {
            return false;
        }
        // 如果该用户未加入该组织，则不能将其踢出组织
        if (userToGroupMapper.findUserToGroup(userId, groupId) == null) {
            return false;
        }
        long groupLeaderId = group.getGroupLeaderId();
        // 管理员不能踢自己
        if (groupLeaderId == userId) {
            return false;
        }
        userToGroupMapper.deleteUserToGroup(userId, groupId);
        return true;
    }
}
