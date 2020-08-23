package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
@Service(interfaceClass = ReportService.class) // interfaceClass目的也是为了解决事务问题
@Transactional
public class ReportServiceImpl implements ReportService {

    // 统计会员信息
    @Autowired
    MemberDao memberDao;

    // 统计预约信息
    @Autowired
    OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() {
        try {
            // 获取当前日
            String today = DateUtils.parseDate2String(DateUtils.getToday());
            // 获取当前日对应的周一
            String monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            // 获取当前日对应的周日
            String sunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            // 获取当前日对应的本月1号
            String firstMonthDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            // 获取当前日对应的本月的最后1天
            String lastMonthDay = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
            // 今日新增会员数
            Integer todayNewMember = memberDao.findTodayNewMember(today);
            // 总会员数
            Integer totalMember = memberDao.findTotalMember();
            // 本周新增会员数
            Integer thisWeekNewMember = memberDao.findNewMemberAfterDate(monday);
            // 本月新增会员数
            Integer thisMonthNewMember = memberDao.findNewMemberAfterDate(firstMonthDay);
            // 今天预约数
            Integer todayOrderNumber = orderDao.findTodayOrderNumber(today);
            // 今天到诊数
            Integer todayVisitsNumber = orderDao.findTodayVisitsNumber(today);
            // 本周预约数
            Map weekMap = new HashMap();
            weekMap.put("begin",monday);
            weekMap.put("end",sunday);
            Integer thisWeekOrderNumber = orderDao.findOrderNumberBetweenDate(weekMap);
            // 本周到诊数
            Integer thisWeekVisitsNumber = orderDao.findVisitsNumberBetweenDate(weekMap);
            // 本月预约数
            Map monthMap = new HashMap();
            monthMap.put("begin",firstMonthDay);
            monthMap.put("end",lastMonthDay);
            Integer thisMonthOrderNumber = orderDao.findOrderNumberBetweenDate(monthMap);
            // 本月到诊数
            Integer thisMonthVisitsNumber = orderDao.findVisitsNumberBetweenDate(monthMap);
            List<Map> hotSetmeal = orderDao.findHotSetmeal();

            Map<String,Object> map = new HashMap<>();
            map.put("reportDate",today);  // 字符串
            map.put("todayNewMember",todayNewMember ); // 整型（Integer，Long）
            map.put("totalMember",totalMember );
            map.put("thisWeekNewMember",thisWeekNewMember );
            map.put("thisMonthNewMember",thisMonthNewMember );
            map.put("todayOrderNumber",todayOrderNumber );
            map.put("todayVisitsNumber",todayVisitsNumber );
            map.put("thisWeekOrderNumber",thisWeekOrderNumber );
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber );
            map.put("thisMonthOrderNumber",thisMonthOrderNumber );
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber );
            map.put("hotSetmeal",hotSetmeal );// List<Map>
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
