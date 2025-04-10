package com.group6.defakelogibackend.mapper;

import com.group6.defakelogibackend.model.Group;
import com.group6.defakelogibackend.model.UserToGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {

    public void createGroup(Group group);

    public Group findGroupByGroupId(long groupId);

    public List<Group> getAllGroups();

    public List<UserToGroup> findGroupMembersByGroupId(long groupId);

    public List<Group> findGroupByGroupname(String groupname);

}
