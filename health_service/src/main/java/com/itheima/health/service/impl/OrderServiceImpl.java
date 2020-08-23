package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/1/2 11:07
 * @Version V1.0
 */
@Service(interfaceClass = OrderService.class) // interfaceClass目的也是为了解决事务问题
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderSettingDao orderSettingDao;

    @Autowired
    MemberDao memberDao;

    @Override
    public Result submitOrder(Map map) throws RuntimeException {

        try {
            // 1：获取体检日期的时间
            String orderDate = (String)map.get("orderDate");
            Date date = DateUtils.parseString2Date(orderDate);
            // 2：使用提交日期的时间，查询预约设置表（t_ordersetting），判断当前预约时间是否可以进行预约
            OrderSetting orderSetting = orderSettingDao.findOrderSettingByOrderDate(date);
            //    * 如果没有查询到结果，则不可以进行预约，提示【当前选中的时间，不能进行预约】
            if(orderSetting==null){
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            //    * 如果查询到数据库，但是 reservations字段（已经预约人数）>= number字段（最多预约人数），此时不能进行预约，提示【预约已满】
            else{
                if(orderSetting.getReservations()>=orderSetting.getNumber()){
                    return new Result(false, MessageConstant.ORDER_FULL);
                }
            }
            // 3：获取手机号，使用当前手机号查询会员表（t_member），判断当前手机号是否是会员
            // 手机号
            String telephone = (String)map.get("telephone");
            Member member = memberDao.findMemberByTelephone(telephone);
            //        * 如果是会员，使用会员ID、预约时间、套餐ID，查询订单表，判断当前时间、当前会员、当前套餐，是否重复预约
            if(member!=null){
                Order order = new Order(member.getId(),date,null,null,Integer.parseInt((String)map.get("setmealId")));
                List<Order> list = orderDao.findOrderListByCondition(order);
                //          * 如果是重复预约：提示【你已经预约，不能重复预约】
                if(list!=null && list.size()>0){
                    return new Result(false, MessageConstant.HAS_ORDERED);
                }
            }
            //        * 如果不是会员，注册会员，组织数据，保存会员表（t_member)
            else{
                member = new Member();
                member.setName((String)map.get("name")); // 会员姓名
                member.setSex((String)map.get("sex")); // 会员性别
                member.setPhoneNumber((String)map.get("telephone")); // 会员的手机号
                member.setIdCard((String)map.get("idCard")); // 会员身份证号
                member.setRegTime(new Date()); // 会员的注册时间（当前时间）
                memberDao.add(member);

            }
            // 4：组织数据，保存订单表（t_order)
            Order order = new Order(member.getId(),date,(String)map.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt((String)map.get("setmealId")));
            orderDao.add(order);
            // 5：使用预约时间，更新预约设置表，让reservations字段+1
            orderSettingDao.updateReservationsByOrderDate(date);
            return new Result(true,MessageConstant.ORDER_SUCCESS,order);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抛出运行时异常");
            //return new Result(true,MessageConstant.ORDER_FAIL);
        }
    }

    @Override
    public Map findById(Integer id) {
        /**
         * map集合的key：
         * member,
         * setmeal,
         * orderDate,
         * orderType
         */
        Map map = orderDao.findById(id);
        if(map!=null && map.size()>0){
            Date orderDate = (Date)map.get("orderDate");
            try {
                String strOrderDate = DateUtils.parseDate2String(orderDate);
                map.put("orderDate",strOrderDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
