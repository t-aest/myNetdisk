package com.taest.mynetdisk.bussiness.user.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;
@RestController
@RequestMapping("/kaptcha")
public class KaptchaController {

    @GetMapping("/image-code/{imageCodeToken}")
    public void imageCode(@PathVariable(value = "imageCodeToken") String imageCodeToken, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        try {
            // 生成验证码字符串
            //定义图形验证码的长、宽、验证码字符数、干扰元素个数
            CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(100, 40, 4, 20);
            //图形验证码写出，可以写出到文件，也可以写出到流
            captcha.write(httpServletResponse.getOutputStream());
            System.out.println("captcha = " + captcha.getCode());
            //验证图形验证码的有效性，返回boolean值
//            captcha.verify("1234");
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }
}
