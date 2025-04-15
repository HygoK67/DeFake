package com.group6.defakelogibackend.mapper;

import com.group6.defakelogibackend.model.UserToGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserToGroupMapper {
    public void addUserToGroup(long userId, long groupId, String status, String role);

    public UserToGroup findUserToGroup(long userId, long groupId);

    public List<UserToGroup> findGroupsByUserId(long userId);

    public void deleteUserToGroup(long userId, long groupId);

    public void updateUserToGroupStatus(long userId, long groupId);
}
