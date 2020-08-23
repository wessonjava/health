package com.itheima.health.test;

import com.itheima.health.constant.RedisPicConstant;
import com.itheima.health.utils.QiniuUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName TestRedis
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/1/5 11:10
 * @Version V1.0
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-redis.xml")
public class TestRedis {

    @Autowired
    JedisPool jedisPool;

    // 删除垃圾图片
    @Test
    public void testDeletePic(){
        Set<String> set = jedisPool.getResource().sdiff(RedisPicConstant.SETMEAL_PIC_RESOURCE, RedisPicConstant.SETMEAL_PIC_DB_RESOUCE);
        // 获取不同的pic文件名
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            String pic = iterator.next();
            System.out.println("需要删除的图片："+pic);
            // 删除Redis中的2个集合不一样的数据，删除key值为setmealPicResource
            jedisPool.getResource().srem(RedisPicConstant.SETMEAL_PIC_RESOURCE,pic);
            // 删除七牛云上的垃圾图片
            QiniuUtils.deleteFileFromQiniu(pic);
        }
    }
}
