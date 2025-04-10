package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.Admin;
import com.group6.defakelogibackend.annotation.LoggedIn;
import com.group6.defakelogibackend.model.Group;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.model.UserToGroup;
import com.group6.defakelogibackend.service.GroupService;
import com.group6.defakelogibackend.utils.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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
        String introduction = requestBody.get("introduction");
        String ddl = requestBody.get("ddl");
        groupService.createGroup(userId, groupname, introduction, ddl);
        return Result.success("创建组织成功");

    }

    @LoggedIn
    @PostMapping("/apply")
    public Result applyGroup(@RequestHeader String jwtToken, @RequestBody Map<String, String> requestBody) {
        Long userIdSent = Long.parseLong(jwtService.getUserId(jwtToken));
        Long groupId = Long.parseLong(requestBody.get("groupId"));
        groupService.applyGroup(userIdSent, groupId);
        return Result.success("发送加入组织申请成功");
    }

    @LoggedIn
    @PostMapping("/invite")
    public Result inviteGroup(@RequestHeader String jwtToken, @RequestBody Map<String, String> requestBody) {
        Long userIdSent = Long.parseLong(jwtService.getUserId(jwtToken));
        Long userIdRec = Long.parseLong(requestBody.get("userIdRec"));
        Long groupId = Long.parseLong(requestBody.get("groupId"));
        String title = requestBody.get("title");
        String content = requestBody.get("content");
        groupService.inviteGroup(userIdSent, userIdRec, groupId, title, content);
        return Result.success("发送邀请成功");
    }

    @LoggedIn
    @PostMapping("/kick")
    public Result kickGroup(@RequestHeader String jwtToken, @RequestBody Map<String, String> requestBody) {
        long userId_sent = Long.parseLong(jwtService.getUserId(jwtToken));
        Long userId_rec = Long.parseLong(requestBody.get("userId_rec"));
        Long groupId = Long.parseLong(requestBody.get("groupId"));
        groupService.kickGroup(userId_sent, userId_rec, groupId);
        return Result.success("踢出成功");
    }

    @LoggedIn
    @GetMapping("/members")
    public Result groupMembers(@RequestParam("groupId") String groupId) {
        List<UserToGroup> list = groupService.groupMembers(Long.parseLong(groupId));
        return Result.success(list);
    }

    @LoggedIn
    @GetMapping("/search")
    public Result searchGroup(@RequestParam("groupname") String groupname) {
        List<Group> list = groupService.searchGroup(groupname);
        return Result.success(list);
    }

    @LoggedIn
    @GetMapping("/joined")
    public Result getGroup(@RequestParam("groupId") String groupId) {
        return Result.success();
    }



}
