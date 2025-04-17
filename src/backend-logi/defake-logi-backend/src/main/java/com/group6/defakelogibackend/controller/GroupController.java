package com.group6.defakelogibackend.controller;

import com.group6.defakelogibackend.annotation.LoggedIn;
import com.group6.defakelogibackend.model.*;
import com.group6.defakelogibackend.service.GroupService;
import com.group6.defakelogibackend.utils.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/group")
@LoggedIn
public class GroupController {

    @Autowired
    GroupService groupService;
    @Autowired
    JWTService jwtService;

    @PostMapping("/create")
    public Result createGroup(@RequestHeader String jwtToken, @RequestBody Map<String, String> requestBody) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        String groupname = requestBody.get("groupname");
        String introduction = requestBody.get("introduction");
        String ddl = requestBody.get("ddl");
        groupService.createGroup(userId, groupname, introduction, ddl);
        return Result.success("创建组织成功");

    }

    @PostMapping("/apply")
    public Result applyGroup(@RequestHeader String jwtToken, @RequestBody Map<String, String> requestBody) {
        Long userIdSent = Long.parseLong(jwtService.getUserId(jwtToken));
        Long groupId = Long.parseLong(requestBody.get("groupId"));
        groupService.applyGroup(userIdSent, groupId);
        return Result.success("发送加入组织申请成功");
    }

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

    @PostMapping("/kick")
    public Result kickGroup(@RequestHeader String jwtToken, @RequestBody Map<String, String> requestBody) {
        long userIdSent = Long.parseLong(jwtService.getUserId(jwtToken));
        Long userIdRec = Long.parseLong(requestBody.get("userIdRec"));
        Long groupId = Long.parseLong(requestBody.get("groupId"));
        groupService.kickGroup(userIdSent, userIdRec, groupId);
        return Result.success("踢出成功");
    }

    @GetMapping("/members")
    public Result groupMembers(@RequestParam("groupId") String groupId) {
        List<UserToGroupDTO> list = groupService.groupMembers(Long.parseLong(groupId));
        return Result.success(list);
    }

    @GetMapping("/search")
    public Result searchGroup(@RequestParam("groupname") String groupname) {
        List<Group> list = groupService.searchGroup(groupname);
        return Result.success(list);
    }

    @GetMapping("/list") // 获取所有和当前登录用户相关的组织
    public Result getGroupByUserId(@RequestHeader String jwtToken) {
        long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        List<GroupDTO> groups = groupService.listGroupByUserId(userId);
        return Result.success(groups);
    }

    @GetMapping("/all")
    public Result getAllGroups() {
        List<Group> allGroups = groupService.showAllGroups();
        return Result.success(allGroups);
    }

    @PostMapping("/deal")
    public Result deal(@RequestHeader String jwtToken, @RequestBody Map<String, String> requestBody) {
        long groupLeaderId = Long.parseLong(jwtService.getUserId(jwtToken));
        long userIdSent = Long.parseLong(requestBody.get("userIdSent"));
        long groupId = Long.parseLong(requestBody.get("groupId"));
        int isAgree = Integer.parseInt(requestBody.get("isAgree"));
        groupService.dealApply(groupLeaderId, userIdSent, groupId, isAgree);
        return Result.success();
    }

    @GetMapping("/info")
    public Result info(@RequestHeader String jwtToken, @RequestParam String groupId) {
        Long userId = Long.parseLong(jwtService.getUserId(jwtToken));
        return Result.success(groupService.getGroupInfo(userId, Long.parseLong(groupId)));
    }


}
