package com.group6.defakelogibackend.service.impl;

import com.group6.defakelogibackend.exception.EntityDuplicateException;
import com.group6.defakelogibackend.exception.EntityMissingException;
import com.group6.defakelogibackend.mapper.GroupMapper;
import com.group6.defakelogibackend.mapper.NotificationMapper;
import com.group6.defakelogibackend.mapper.UserMapper;
import com.group6.defakelogibackend.mapper.UserToGroupMapper;
import com.group6.defakelogibackend.model.Group;
import com.group6.defakelogibackend.model.GroupDTO;
import com.group6.defakelogibackend.model.UserToGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public boolean createGroup(long userId, String groupname, String introduction, String ddl) {
        Group group = new Group();
        group.setGroupname(groupname);
        group.setIntroduction(introduction);
        group.setDdl(ddl);
        System.out.println(groupname);
        System.out.println(groupname);
        System.out.println(groupname);
        System.out.println(groupMapper.findGroupByGroupname(groupname));
        if (groupMapper.findGroupByGroupname(groupname) != null) {
            throw new EntityDuplicateException("该 groupname 已存在!");
        }
        groupMapper.createGroup(group);
        userToGroupMapper.addUserToGroup(userId, group.getId(), "in", "leader");
        return true;
    }


    @Override
    @Transactional
    public boolean applyGroup(long userId_sent, long groupId) {
        Group group = groupMapper.findGroupByGroupId(groupId);
        // 如果groupId无效
        if (group == null) {
            throw new EntityMissingException("groupId 无效!");
        }

        // 如果该用户已经加入该组织或者已经发送过申请，则不能再次发送申请
        if (userToGroupMapper.findUserToGroup(userId_sent, groupId) != null) {
            throw new EntityDuplicateException("该用户 status 为 in / pending!");
        }

        userToGroupMapper.addUserToGroup(userId_sent, groupId, "pending_apply", "member");
        return true;
    }

    @Override
    @Transactional
    public boolean inviteGroup(long userIdSent, long userIdRec, long groupId, String title, String content) {
        Group group = groupMapper.findGroupByGroupId(groupId);
        // 如果groupId无效
        if (group == null) {
            throw new EntityMissingException("groupId 无效!");
        }

        if (userMapper.findUserById(userIdRec) == null) {
            throw new EntityMissingException("userIdRec 无效!");
        }

        // 如果该用户已经加入该组织，则不能发送申请
        if (userToGroupMapper.findUserToGroup(userIdRec, groupId) != null) {
            throw new EntityDuplicateException("该用户 status 为 in / pending!");
        }

        userToGroupMapper.addUserToGroup(userIdRec, groupId, "pending_invite", "member");
        notificationMapper.createNotificationUser2User(userIdSent, userIdRec, groupId, title, content);
        return true;
    }

    @Override
    @Transactional
    public boolean kickGroup(long userIdSent, long userIdRec, long groupId) {
        Group group = groupMapper.findGroupByGroupId(groupId);
        // 如果groupId无效
        if (group == null) {
            throw new EntityMissingException("groupId 无效!");
        }

        if (userToGroupMapper.findUserToGroup(userIdSent, groupId).getRole() != UserToGroup.Role.leader) {
            throw new EntityMissingException("发起者并非该组织的管理员!");
        }

        // 如果该用户未加入该组织，则不能将其踢出组织
        if (userToGroupMapper.findUserToGroup(userIdRec, groupId) == null ||
                userToGroupMapper.findUserToGroup(userIdRec, groupId).getStatus() != UserToGroup.Status.in) {
            throw new EntityMissingException("(userId_rec, groupId) 无效!");
        }

        if (userIdRec == userIdSent) {
            throw new EntityDuplicateException("自己不能踢自己!");
        }

        userToGroupMapper.deleteUserToGroup(userIdRec, groupId);
        String title = group.getGroupname() + " 组织通知";
        String content = "您已不再是 " + group.getGroupname() + " 中的成员。";
        notificationMapper.createNotificationUser2User(userIdSent, userIdRec, groupId, title, content);
        return true;
    }

    @Override
    @Transactional
    public List<Group> showAllGroups() {
        return groupMapper.getAllGroups();
    }

    @Override
    @Transactional
    public List<UserToGroup> groupMembers(long groupId) {
        return groupMapper.findGroupMembersByGroupId(groupId);
    }

    @Override
    @Transactional
    public List<Group> searchGroup(String groupname) {
        return groupMapper.searchGroupByGroupname(groupname);
    }

    @Override
    @Transactional
    public List<GroupDTO> listGroupByUserId(long userId) {
        List<UserToGroup> userToGroups = userToGroupMapper.findGroupsByUserId(userId);
        List<GroupDTO> groupDTOList = new ArrayList<>();
        for (UserToGroup userToGroup : userToGroups) {
            long groupId = userToGroup.getGroupId();
            Group group = groupMapper.findGroupByGroupId(groupId);
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setGroupname(group.getGroupname());
            groupDTO.setId(groupId);
            groupDTO.setStatus(userToGroup.getStatus());
            groupDTO.setRole(userToGroup.getRole());
            groupDTO.setCreatedAt(group.getCreatedAt());
            groupDTO.setUpdatedAt(group.getUpdatedAt());
            groupDTO.setDdl(group.getDdl());
            groupDTO.setIntroduction(group.getIntroduction());
            groupDTOList.add(groupDTO);
        }
        return groupDTOList;
    }

    @Override
    @Transactional
    public void dealApply(long groupLeaderId, long userIdSent, long groupId, int isAgree) {
        if (userMapper.findUserById(userIdSent) == null) {
            throw new EntityMissingException("userIdSent 无效!");
        }

        Group group = groupMapper.findGroupByGroupId(groupId);
        if (group == null) {
            throw new EntityMissingException("groupId 无效!");
        }

        if (userToGroupMapper.findUserToGroup(groupLeaderId, groupId).getRole() != UserToGroup.Role.leader) {
            throw new EntityMissingException("该用户不是当前组织的管理员!");
        }

        if (userToGroupMapper.findUserToGroup(userIdSent, groupId) == null ||
                userToGroupMapper.findUserToGroup(userIdSent, groupId).getStatus() != UserToGroup.Status.pending_apply) {
            throw new EntityMissingException("该用户没有发送申请!");
        }

        String groupname = group.getGroupname();

        // 组织管理员同意用户申请
        if (isAgree != 0) {
            userToGroupMapper.updateUserToGroupStatus(userIdSent, groupId);
            notificationMapper.createNotificationUser2User(groupLeaderId, userIdSent, groupId, "组织通知", "您成功加入" + groupname + "!");
        }
        // 组织管理员拒绝用户申请
        else {
            userToGroupMapper.deleteUserToGroup(userIdSent, groupId);
            notificationMapper.createNotificationUser2User(groupLeaderId, userIdSent, groupId, "组织通知", groupname + "不允许您加入!");
        }
    }

    public Group getGroupInfo(long groupId) {
        Group group = groupMapper.findGroupByGroupId(groupId);
        if (group == null) {
            throw new EntityMissingException("groupId 无效!");
        }
        return group;
    }

}
