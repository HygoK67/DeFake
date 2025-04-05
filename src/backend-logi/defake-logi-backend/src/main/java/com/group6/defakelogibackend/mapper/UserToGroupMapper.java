package com.group6.defakelogibackend.mapper;

import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.model.UserToGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserToGroupMapper {
    public void addUserToGroup(long userId, long groupId);

    public UserToGroup findUserToGroup(long userId, long groupId);
}
