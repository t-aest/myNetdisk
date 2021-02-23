package com.taest.mynetdisk.bussiness.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.taest.mynetdisk.bussiness.user.entity.MyUser;
import com.taest.mynetdisk.bussiness.user.mapper.MyUserMapper;
import com.taest.mynetdisk.dto.LoginUserDto;
import com.taest.mynetdisk.exception.file.UserException;
import com.taest.mynetdisk.response.ResultStatus;
import com.taest.mynetdisk.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MyUserMapper myUserMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        QueryWrapper<MyUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name", userName);
        MyUser myUser = myUserMapper.selectOne(queryWrapper);
        if (StringUtils.checkValNull(myUser)){
            throw new UserException(ResultStatus.USER_NOT_EXIST,null);
        }
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("role");

        return new LoginUserDto(userName, SecurityUtils.encryptPassword(myUser.getPassword()), auths);
    }
}
