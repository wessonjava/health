package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

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
@RequestMapping(value = "/order")
public class OrderMobileController {

    @Autowired
    JedisPool jedisPool;

    @Reference
    OrderService orderService;

    // 提交体检预约
    @RequestMapping(value = "/submit")
    public Result submit(@RequestBody Map map){
        // 一：比对验证码
        // 1：从页面上获取手机号和验证码
        String telephone = (String)map.get("telephone");
        String validateCode = (String)map.get("validateCode");
        // 2：使用手机号从Redis中获取验证码
        String redisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //3：使用页面输入的验证码和Redis中获取的验证码进行比对
                //* 比对不成功：提示【验证码输入有误】
        if(redisValidateCode==null || !redisValidateCode.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 4：将表单信息传递给Service进行处理
        Result result = null;
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN); // 通过微信预约提交
            result = orderService.submitOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    // 使用订单id，查询订单对象（订单详情）
    /**
     * <p>体检人：{{orderInfo.member}}</p>
       <p>体检套餐：{{orderInfo.setmeal}}</p>
       <p>体检日期：{{orderInfo.orderDate}}</p>
       <p>预约类型：{{orderInfo.orderType}}</p>
     * @param map
     * @return
     */
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        try {
            // 使用订单id，查询订单信息
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
