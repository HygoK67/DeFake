package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.model.Group;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    GroupService groupService;

    @PostMapping("/createGroup")
    public Result createGroup(@RequestBody Map<String, String> requestBody) {
        Long userId = Long.parseLong(requestBody.get("userId"));
        String groupname = requestBody.get("groupname");
        if (groupService.createGroup(userId, groupname)) {
            return Result.success("创建组织成功");
        }
        return Result.error("创建组织失败");
    }

    @PostMapping("/applyGroup")
    public Result applyGroup(@RequestBody Map<String, String> requestBody) {
        Long userId = Long.parseLong(requestBody.get("userId"));
        Long groupId = Long.parseLong(requestBody.get("groupId"));
        String title = requestBody.get("title");
        String content = requestBody.get("content");
        if (groupService.applyGroup(userId, groupId, title, content)) {
            return Result.success("发送加入组织申请成功");
        }
        return Result.error("发送加入组织申请失败");
    }

    @PostMapping("/inviteGroup")
    public Result inviteGroup(@RequestBody Map<String, String> requestBody) {
        Long userId = Long.parseLong(requestBody.get("userId"));
        Long groupId = Long.parseLong(requestBody.get("groupId"));
        String title = requestBody.get("title");
        String content = requestBody.get("content");
        if (groupService.inviteGroup(userId, groupId, title, content)){
            return Result.success("发送邀请成功");
        }
        return Result.error("发送邀请失败");
    }
}
