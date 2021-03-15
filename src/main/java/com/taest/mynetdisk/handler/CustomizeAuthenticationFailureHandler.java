package com.taest.mynetdisk.handler;

import cn.hutool.json.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taest.mynetdisk.response.Result;
import com.taest.mynetdisk.response.ResultStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: taest
 * @Description: 登录失败处理逻辑
 * @Date Create in 2019/9/3 15:52
 */
@Component
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //返回json数据
        Result result = new Result();
        if (e instanceof AccountExpiredException) {
            //账号过期
            result.setMessage(ResultStatus.USER_NOT_EXIST.getMessage());
        } else if (e instanceof BadCredentialsException) {
            //密码错误
            result.setMessage(ResultStatus.USER_NOT_EXIST.getMessage());
        } else if (e instanceof CredentialsExpiredException) {
            //密码过期
            result.setMessage(ResultStatus.USER_NOT_EXIST.getMessage());
        } else if (e instanceof DisabledException) {
            //账号不可用
            result.setMessage(ResultStatus.USER_NOT_EXIST.getMessage());
        } else if (e instanceof LockedException) {
            //账号锁定
            result.setMessage(ResultStatus.USER_NOT_EXIST.getMessage());
        } else if (e instanceof InternalAuthenticationServiceException) {
            //用户不存在
            result.setMessage(ResultStatus.USER_NOT_EXIST.getMessage());
        } else {
            //其他错误
            result.setMessage(ResultStatus.USER_NOT_EXIST.getMessage());
        }
        //处理编码方式，防止中文乱码的情况
        httpServletResponse.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(result);
        httpServletResponse.getWriter().write(value);
    }
}