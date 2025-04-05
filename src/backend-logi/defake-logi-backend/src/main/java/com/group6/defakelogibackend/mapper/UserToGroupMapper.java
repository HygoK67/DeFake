package com.group6.defakelogibackend.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserToGroupMapper {
    public void addUserToGroup(long userId, long groupId);

}
