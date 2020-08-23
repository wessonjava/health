package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import com.itheima.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/1/2 11:07
 * @Version V1.0
 */
@Service(interfaceClass = MemberService.class) // interfaceClass目的也是为了解决事务问题
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;


    @Override
    public Member findMemberByTelephone(String telephone) {
        return memberDao.findMemberByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        // 生成32位随机数，同一个password，每次生成的密码值是相同的
        if(member.getPassword()!=null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> memberCounts = new ArrayList<>();
        if(months!=null && months.size()>0){
            for (String month : months) {
                // 每个月的最后1天
                String date = month+"-31";
                Integer count = memberDao.findMemberCountByMonth(date);
                memberCounts.add(count);
            }
        }
        return memberCounts;
    }
}
