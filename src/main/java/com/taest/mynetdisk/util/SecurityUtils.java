package com.taest.mynetdisk.util;

import cn.hutool.system.UserInfo;
import com.taest.mynetdisk.bussiness.user.controller.MyUserController;
import com.taest.mynetdisk.dto.LoginUserDto;
import com.taest.mynetdisk.dto.UserDto;
import com.taest.mynetdisk.exception.file.UserException;
import com.taest.mynetdisk.response.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);


    /**
     * 描述根据账号密码进行调用security进行认证授权 主动调
     * 用AuthenticationManager的authenticate方法实现
     * 授权成功后将用户信息存入SecurityContext当中
     * @param authenticationManager 认证授权管理器,
     * @see  AuthenticationManager
     * @return UserInfo  用户信息
     */
    public static LoginUserDto login(String username,String password, AuthenticationManager authenticationManager){
        try {
            //使用security框架自带的验证token生成器  也可以自定义。
            UsernamePasswordAuthenticationToken token =new UsernamePasswordAuthenticationToken(username,password);
            Authentication authenticate = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            LoginUserDto userInfo = (LoginUserDto) authenticate.getPrincipal();
            return userInfo;
        }catch (BadCredentialsException e){
            LOG.error(e.getMessage());
            throw new UserException(ResultStatus.USERNAME_OR_PASSWORD_ERROR,null);
        }

    }

    /**
     * 获取当前登录的所有认证信息
     * @return
     */
    public static Authentication getAuthentication(){
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    public static UserDto getUserInfo(){
        Authentication authentication = getAuthentication();
        if(authentication!=null){
            Object principal = authentication.getPrincipal();
            if(principal!=null){
                UserDto userInfo = (UserDto) authentication.getPrincipal();
                return userInfo;
            }
        }
        throw new UserException(ResultStatus.GET_USERINFO_ERROR,null);
    }

    /**
     * 获取当前登录用户ID
     * @return
     */
    public static String getUserId(){
        UserDto userInfo = getUserInfo();
        return userInfo.getId();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
