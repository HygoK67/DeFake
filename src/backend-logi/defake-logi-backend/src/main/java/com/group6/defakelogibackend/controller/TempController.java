package com.group6.defakelogibackend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.defakelogibackend.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TempController {

    String data = "{\n" +
            "  \"path\": \"/permission\",\n" +
            "  \"meta\": {\n" +
            "    \"title\": \"权限管理\",\n" +
            "    \"icon\": \"ep:lollipop\",\n" +
            "    \"rank\": 10\n" +
            "  },\n" +
            "  \"children\": [\n" +
            "    {\n" +
            "      \"path\": \"/permission/page/index\",\n" +
            "      \"name\": \"PermissionPage\",\n" +
            "      \"meta\": {\n" +
            "        \"title\": \"页面权限\",\n" +
            "        \"roles\": [\"admin\", \"common\"]\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"path\": \"/permission/button\",\n" +
            "      \"meta\": {\n" +
            "        \"title\": \"按钮权限\",\n" +
            "        \"roles\": [\"admin\", \"common\"]\n" +
            "      },\n" +
            "      \"children\": [\n" +
            "        {\n" +
            "          \"path\": \"/permission/button/router\",\n" +
            "          \"component\": \"permission/button/index\",\n" +
            "          \"name\": \"PermissionButtonRouter\",\n" +
            "          \"meta\": {\n" +
            "            \"title\": \"路由返回按钮权限\",\n" +
            "            \"auths\": [\n" +
            "              \"permission:btn:add\",\n" +
            "              \"permission:btn:edit\",\n" +
            "              \"permission:btn:delete\"\n" +
            "            ]\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"path\": \"/permission/button/login\",\n" +
            "          \"component\": \"permission/button/perms\",\n" +
            "          \"name\": \"PermissionButtonLogin\",\n" +
            "          \"meta\": {\n" +
            "            \"title\": \"登录接口返回按钮权限\"\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    @GetMapping("/get-async-routes")
    public Object getAsyncRoutes() {
        Map<String, Object> jsonObject = stringToMap(data);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("success", Boolean.TRUE);
        returnMap.put("data", jsonObject);
        List<Object> returnList = new ArrayList<>();
        returnList.add(returnMap);
        return returnList;
    }

    private Map<String, Object> stringToMap(String jsonStr) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

}
