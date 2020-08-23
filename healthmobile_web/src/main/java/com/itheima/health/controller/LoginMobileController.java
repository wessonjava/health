package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName SetmealMobileController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/1/8 9:45
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/login")
public class LoginMobileController {

    @Autowired
    JedisPool jedisPool;

    @Reference
    MemberService memberService;

    // 手机快速登录
    @RequestMapping(value = "/check")
    public Result check(@RequestBody Map map, HttpServletResponse response){
        // 获取用户页面输入的信息
        // 手机号
        String telephone = (String)map.get("telephone");
        // 验证码
        String validateCode = (String)map.get("validateCode");
        // 取出Redis中的验证码
        String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        // 比对验证输入是否正确
        if(redisCode==null || !redisCode.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        else{
            // 判断当前手机号是否是会员，如果不是会员注册会员
            Member member = memberService.findMemberByTelephone(telephone);
            if(member==null){
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
        }
        // SOA架构、微服务架构，将登录用户的信息Cookie中
        Cookie cookie = new Cookie("login_member_cookie83",telephone);
        // cookie的有效路径
        cookie.setPath("/");
        // cookie的有效时间
        cookie.setMaxAge(30*24*60*60); // 秒（30天）
        // 将Cookie存放到Response里
        response.addCookie(cookie);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }

}
