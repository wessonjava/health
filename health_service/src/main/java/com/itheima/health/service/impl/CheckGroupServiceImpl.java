package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/1/2 11:07
 * @Version V1.0
 */
@Service(interfaceClass = CheckGroupService.class) // interfaceClass目的也是为了解决事务问题
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 1：新增检查组，向t_checkgroup表中添加1条数据，同时返回检查组id
        checkGroupDao.add(checkGroup);
        // 2：向检查组和检查项的中间表添加数据t_checkgroup_checkitem，多条数据，与检查项的数组有关
        this.setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 1：初始化分页参数
        PageHelper.startPage(currentPage,pageSize);
        // 2：执行查询
        Page<CheckGroup> page = checkGroupDao.findPage(queryString);
        // 3：封装结果
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 1：使用CheckGroup，更新检查组（动态sql更新）
        checkGroupDao.edit(checkGroup);
        // 2：删除检查项和检查组的中间表，使用检查组的id作为条件删除
        checkGroupDao.deleteCheckGroupAndCheckItemByCheckGroupId(checkGroup.getId());
        // 3：向检查组和检查项的中间表添加数据t_checkgroup_checkitem，多条数据，与检查项的数组有关
        this.setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    // 向检查组和检查项的中间表添加数据t_checkgroup_checkitem，多条数据，与检查项的数组有关
    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if(checkitemIds!=null && checkitemIds.length>0){
            for (Integer checkItemId : checkitemIds) {
                // 如果传递多个参数，使用3种方式完成
                // 方式一：封装实体，使用OGNL表达式，获取实体中的属性（麻烦，创建javabean）
                // 方式二：封装Map集合
//                Map map = new HashMap();
//                map.put("checkGroupId",checkGroupId);
//                map.put("checkItemId",checkItemId);
//                checkGroupDao.addCheckGroupAndCheckItem(map);
                // 方式三：使用@Param
                checkGroupDao.addCheckGroupAndCheckItem2(checkGroupId,checkItemId);
            }
        }
    }
}
