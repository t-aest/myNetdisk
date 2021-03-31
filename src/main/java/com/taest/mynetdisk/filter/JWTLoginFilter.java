package com.taest.mynetdisk.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taest.mynetdisk.dto.LoginUserDto;
import com.taest.mynetdisk.dto.UserDto;
import com.taest.mynetdisk.util.JwtTokenUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    /**
     * 接收并解析用户凭证，出現错误时，返回json数据前端
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserDto user =new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getLoginName(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        } catch (Exception e) {
            try {
                //未登錄出現賬號或密碼錯誤時，使用json進行提示
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter out = response.getWriter();
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("code",HttpServletResponse.SC_UNAUTHORIZED);
                map.put("message","账号或密码错误！");
                out.write(new ObjectMapper().writeValueAsString(map));
                out.flush();
                out.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户登录成功后，生成token,并且返回json数据给前端
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //json web token构建
        JwtTokenUtil tokenUtil = new JwtTokenUtil();
        String token = tokenUtil.generateToken(((LoginUserDto) authResult.getPrincipal()).getUsername());

        //返回token
        res.addHeader("Authorization", "Bearer " + token);
        try {
            //登录成功時，返回json格式进行提示
            res.setContentType("application/json;charset=utf-8");
            res.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = res.getWriter();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("code",HttpServletResponse.SC_OK);
            map.put("message","登陆成功！");
            out.write(new ObjectMapper().writeValueAsString(map));
            out.flush();
            out.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    }

