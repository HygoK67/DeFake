package com.group6.defakelogibackend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.defakelogibackend.annotation.Admin;
import com.group6.defakelogibackend.annotation.LoggedIn;
import com.group6.defakelogibackend.model.Result;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.utils.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.lang.reflect.Method;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTService jwtService;

    private static ObjectMapper jacksonObjectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. 检查是否是 HandlerMethod（排除静态资源）
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 2. 检查类级别注解
        Class<?> beanType = handlerMethod.getBeanType();
        Admin classAdmin = beanType.getAnnotation(Admin.class);
        LoggedIn classLoggedIn = beanType.getAnnotation(LoggedIn.class);

        // 3. 检查方法级别注解（优先级高于类）
        Method method = handlerMethod.getMethod();
        Admin methodAdmin = method.getAnnotation(Admin.class);
        LoggedIn methodLoggedIn = method.getAnnotation(LoggedIn.class);

        // 合并注解（方法注解覆盖类注解）
        boolean requireAdmin = methodAdmin != null || classAdmin != null;
        boolean requireLogin = methodLoggedIn != null || classLoggedIn != null;

        // 4. 从请求头中获取 Token
        String token = request.getHeader("jwtToken");

        // 5. 校验权限
        if (requireLogin || requireAdmin) {
            // 必须提供 Token
            if (token == null) {
                sendErrorResponse(response, "未登录，请先登录");
                return false;
            }

            try {
                String userRole = jwtService.getUserRole(token);
                // 检查管理员权限
                if (requireAdmin && !userRole.equals("admin")) {
                    sendErrorResponse(response, "不是管理员，没有权限!");
                    return false;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                sendErrorResponse(response, "jwtToken 错误!, 请重新登录获取 Token");
                return false;
            }
        }
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        Result result = Result.error(message);
        response.getWriter().write(jacksonObjectMapper.writeValueAsString(result));
    }
}