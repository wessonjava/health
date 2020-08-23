package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void addCheckGroupAndCheckItem(Map map);

    void addCheckGroupAndCheckItem2(@Param(value = "groupId") Integer checkGroupId, @Param(value = "itemId") Integer checkItemId);

    Page<CheckGroup> findPage(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteCheckGroupAndCheckItemByCheckGroupId(Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupsBySetmealId(Integer setmealId);
}
