package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.LoggedIn;
import com.group6.defakelogibackend.model.Group;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.service.GroupService;
import com.group6.defakelogibackend.utils.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    GroupService groupService;
    @Autowired
    JWTService jwtService;

    @LoggedIn
    @PostMapping("/create")
    public Result createGroup(@RequestHeader String jwtToken, @RequestBody Map<String, String> requestBody) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        String groupname = requestBody.get("groupname");
        groupService.createGroup(userId, groupname);
        return Result.success("创建组织成功");

    }

    @LoggedIn
    @PostMapping("/apply")
    public Result applyGroup(@RequestHeader String jwtToken, @RequestBody Map<String, String> requestBody) {
        Long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        Long groupId = Long.parseLong(requestBody.get("groupId"));
        String title = requestBody.get("title");
        String content = requestBody.get("content");
        groupService.applyGroup(userId, groupId, title, content);
        return Result.success("发送加入组织申请成功");

    }

    @PostMapping("/invite")
    public Result inviteGroup(@RequestBody Map<String, String> requestBody) {
        Long userId = Long.parseLong(requestBody.get("userId"));
        Long groupId = Long.parseLong(requestBody.get("groupId"));
        String title = requestBody.get("title");
        String content = requestBody.get("content");
        if (groupService.inviteGroup(userId, groupId, title, content)) {
            return Result.success("发送邀请成功");
        }
        return Result.error("发送邀请失败");
    }

    @PostMapping("/kick")
    public Result kickGroup(@RequestBody Map<String, String> requestBody) {
        Long userId = Long.parseLong(requestBody.get("userId"));
        Long groupId = Long.parseLong(requestBody.get("groupId"));
        if (groupService.kickGroup(userId, groupId)) {
            return Result.success("踢出成功");
        }
        return Result.error("踢出失败");
    }
}
