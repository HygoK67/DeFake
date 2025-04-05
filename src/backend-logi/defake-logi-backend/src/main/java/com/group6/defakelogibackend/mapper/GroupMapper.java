package com.group6.defakelogibackend.mapper;

import com.group6.defakelogibackend.model.Group;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupMapper {
    public Group findGroupByUserIdAndGroupname(long userId, String groupname);

    public void createGroup(long userId, String groupname);

}
