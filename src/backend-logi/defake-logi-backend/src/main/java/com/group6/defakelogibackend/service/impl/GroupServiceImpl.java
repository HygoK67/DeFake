package com.group6.defakelogibackend.service.impl;

import com.group6.defakelogibackend.mapper.GroupMapper;
import com.group6.defakelogibackend.mapper.UserToGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupServiceImpl implements com.group6.defakelogibackend.service.GroupService {

    @Autowired
    GroupMapper groupMapper;
    @Autowired
    UserToGroupMapper userToGroupMapper;

    @Override
    @Transactional
    public boolean createGroup(long userId, String groupname) {
        // 如果该用户已经创建过同名的组织
        if (groupMapper.findGroupByUserIdAndGroupname(userId, groupname) != null) {
            return false;
        }
        groupMapper.createGroup(userId, groupname);
        long groupId = groupMapper.findGroupByUserIdAndGroupname(userId, groupname).getId();
        userToGroupMapper.addUserToGroup(userId, groupId);
        return true;
    }
}
