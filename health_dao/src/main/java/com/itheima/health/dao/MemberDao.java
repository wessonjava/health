package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

public interface MemberDao {

    Member findMemberByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByMonth(String date);

    Integer findTodayNewMember(String date);

    Integer findTotalMember();

    Integer findNewMemberAfterDate(String date);
}
