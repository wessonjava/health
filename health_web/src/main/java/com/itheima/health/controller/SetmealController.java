package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisPicConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/1/2 11:08
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    // 图片上传
    @RequestMapping(value = "/upload")
    public Result upload(MultipartFile imgFile){
        try {
            // 文件名（01.jpg)
            String fileName = imgFile.getOriginalFilename();
            // 生成UUID的方式完成上传
            String uuid = UUID.randomUUID().toString();
            String fix = fileName.substring(fileName.lastIndexOf("."));
            fileName = uuid+fix;
            // 使用七牛云上传
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            // 图片上传的时候，存放Redis，使用Set的方式存放，key值使用： setmealPicResource
            jedisPool.getResource().sadd(RedisPicConstant.SETMEAL_PIC_RESOURCE,fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    // 新增套餐
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Setmeal setmeal, Integer [] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    // 分页查询检查组
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.findPage(queryPageBean.getCurrentPage(),
                                                        queryPageBean.getPageSize(),
                                                        queryPageBean.getQueryString());
        return pageResult;
    }

}
