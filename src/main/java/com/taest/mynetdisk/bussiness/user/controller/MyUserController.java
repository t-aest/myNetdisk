package com.taest.mynetdisk.bussiness.user.controller;


import cn.hutool.json.JSON;
import com.taest.mynetdisk.dto.LoginUserDto;
import com.taest.mynetdisk.dto.UserDto;
import com.taest.mynetdisk.response.Result;
import com.taest.mynetdisk.response.ResultStatus;
import com.taest.mynetdisk.util.MyObjectUtils;
import com.taest.mynetdisk.util.MyStringUtils;
import com.taest.mynetdisk.util.SecurityUtils;
import com.taest.mynetdisk.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.taest.mynetdisk.response.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-02-04
 */
@RestController
@RequestMapping("/user")
public class MyUserController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(MyUserController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto, HttpServletRequest request) {
        LOG.info("用户登录开始");
        // 根据验证码token去获取缓存中的验证码，和用户输入的验证码是否一致
        // String imageCode = (String) request.getSession().getAttribute(userDto.getImageCodeToken());
        String imageCode = (String) redisTemplate.opsForValue().get(userDto.getImageCodeToken());
        LOG.info("从redis中获取到的验证码：{}", imageCode);
        if (MyStringUtils.isEmpty(imageCode)) {
            LOG.info("用户登录失败，验证码已过期");
            return failure(ResultStatus.EXPIRED_VERIFICATION_CODE);
        }
        if (!imageCode.toLowerCase().equals(userDto.getImageCode().toLowerCase())) {
            LOG.info("用户登录失败，验证码错误");
            return failure(ResultStatus.ERROR_VERIFICATION_CODE);
        } else {
            // 验证通过后，移除验证码
//            request.getSession().removeAttribute(userDto.getImageCodeToken());
            redisTemplate.delete(userDto.getImageCodeToken());
        }

//        userDto.setPassword(SecurityUtils.encryptPassword(userDto.getPassword()));

        LoginUserDto loginUser = SecurityUtils.login(userDto.getLoginName(),userDto.getPassword(), authenticationManager);

        if (MyObjectUtils.isEmpty(loginUser)){
            return failure(ResultStatus.LOGIN_ERROR);
        }
        return success(loginUser);
    }

}
