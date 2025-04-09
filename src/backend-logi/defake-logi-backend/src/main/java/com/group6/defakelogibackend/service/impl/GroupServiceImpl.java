package com.group6.defakelogibackend.service.impl;

import com.group6.defakelogibackend.exception.EntityDuplicateException;
import com.group6.defakelogibackend.exception.EntityMissingException;
import com.group6.defakelogibackend.mapper.GroupMapper;
import com.group6.defakelogibackend.mapper.NotificationMapper;
import com.group6.defakelogibackend.mapper.UserMapper;
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
    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional
    public boolean createGroup(long userId, String groupname) {
        Group group = new Group();
        group.setGroupname(groupname);
        groupMapper.createGroup(group);
        userToGroupMapper.addUserToGroup(userId, group.getId(), "in", "leader");
        return true;
    }


    @Override
    @Transactional
    public boolean applyGroup(long userId_sent, long userId_rec, long groupId, String title, String content) {
        Group group = groupMapper.findGroupByGroupId(groupId);
        // 如果groupId无效
        if (group == null) {
            throw new EntityMissingException("groupId 无效!");
        }
        if (userMapper.findUserById(userId_rec) == null) {
            throw new EntityMissingException("userId_rec 无效!");
        }
        // 如果该用户已经加入该组织或者已经发送过申请，则不能再次发送申请
        if (userToGroupMapper.findUserToGroup(userId_sent, groupId) != null) {
            throw new EntityDuplicateException("该用户 status 为 in / pending!");
        }

        userToGroupMapper.addUserToGroup(userId_sent, groupId, "pending", "member");
        notificationMapper.createNotificationUser2User(userId_sent, userId_rec, groupId, title, content);
        return true;
    }

    @Override
    @Transactional
    public boolean inviteGroup(long userId_sent, long userId_rec, long groupId, String title, String content) {
        Group group = groupMapper.findGroupByGroupId(groupId);
        // 如果groupId无效
        if (group == null) {
            throw new EntityMissingException("groupId 无效!");
        }

        if (userMapper.findUserById(userId_rec) == null) {
            throw new EntityMissingException("userId_rec 无效!");
        }

        // 如果该用户已经加入该组织，则不能发送申请
        if (userToGroupMapper.findUserToGroup(userId_rec, groupId) != null) {
            throw new EntityDuplicateException("该用户 status 为 in / pending!");
        }

        userToGroupMapper.addUserToGroup(userId_rec, groupId, "pending", "member");
        notificationMapper.createNotificationUser2User(userId_sent, userId_rec, groupId, title, content);
        return true;
    }

    @Override
    @Transactional
    public boolean kickGroup(long userId, long groupId) {
        Group group = groupMapper.findGroupByGroupId(groupId);
        // 如果groupId无效
//        if (group == null) {
//            return false;
//        }
//        // 如果该用户未加入该组织，则不能将其踢出组织
//        if (userToGroupMapper.findUserToGroup(userId, groupId) == null) {
//            return false;
//        }
//        long groupLeaderId = group.getGroupLeaderId();
//        // 管理员不能踢自己
//        if (groupLeaderId == userId) {
//            return false;
//        }
//        userToGroupMapper.deleteUserToGroup(userId, groupId);
        return true;
    }
}
