package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);

    List<CheckItem> findPage(String queryString);

    Page<CheckItem> findPage2(String queryString);

    void deleteById(Integer id);

    Integer findCheckGroupAndCheckItemCountByCheckItemId(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemsByCheckGroupId(Integer checkGroupId);
}
