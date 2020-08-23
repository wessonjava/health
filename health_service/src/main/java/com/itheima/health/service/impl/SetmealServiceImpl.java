package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.RedisPicConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

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
@Service(interfaceClass = SetmealService.class) // interfaceClass目的也是为了解决事务问题
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;

    // 保存套餐
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 1：新增套餐，同时返回套餐id
        setmealDao.add(setmeal);
        // 2：向套餐和检查组的中间表中关联数据
        setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        // 套餐保存的时候，存放Redis，使用Set的方式存放
        jedisPool.getResource().sadd(RedisPicConstant.SETMEAL_PIC_DB_RESOUCE,setmeal.getImg());
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

//    // 使用代码的方式完成
//    @Override
//    public Setmeal findById(Integer id) {
//        // 1：使用套餐id，查询套餐对象
//        Setmeal setmeal = setmealDao.findById(id);
//        // 2：使用套餐id，查询套餐对应的检查组的集合，并封装到CheckGroups集合中
//        List<CheckGroup> checkGroups = setmealDao.findCheckGroupsBySetmealId(id);
//        // 3：使用检查组id，查询检查组对应的检查项的集合，并封装到checkItems集合中
//        if(checkGroups!=null && checkGroups.size()>0){
//            for (CheckGroup checkGroup : checkGroups) {
//                List<CheckItem> checkItems = setmealDao.findCheckItmesByCheckgroupId(checkGroup.getId());
//                checkGroup.setCheckItems(checkItems);
//            }
//        }
//        setmeal.setCheckGroups(checkGroups);
//        return setmeal;
//    }

    // 使用Mybatis映射的方式完成封装
    @Override
    public Setmeal findById(Integer id) {
        // 1：使用套餐id，查询套餐对象
        Setmeal setmeal = setmealDao.findById(id);
        return setmeal;
    }

    @Override
    public List<Map> getSetmealReport() {
        return setmealDao.getSetmealReport();
    }

    // 向套餐和检查组的中间表中关联数据
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        if(checkgroupIds!=null && checkgroupIds.length>0){
            for (Integer checkgroupId : checkgroupIds) {
                Map map = new HashMap();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                setmealDao.addSetmealAndCheckGroup(map);
            }
        }
    }
}
