package com.itheima.health.dao;

import com.itheima.health.pojo.Role;

import java.util.Set;

public interface RoleDao {

    public Set<Role> findRolesByUserId(Integer userId);

}
