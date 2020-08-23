package com.itheima.health.dao;

import com.itheima.health.pojo.User;

/**
 * @ClassName UserDao
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/1/12 8:57
 * @Version V1.0
 */
public interface UserDao {

    User findUserByUsername(String username);
}
