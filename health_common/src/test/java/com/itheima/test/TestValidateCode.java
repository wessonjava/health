package com.itheima.test;

import com.itheima.health.utils.ValidateCodeUtils;
import org.junit.Test;

/**
 * @ClassName TestValidateCode
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/1/8 9:07
 * @Version V1.0
 */
public class TestValidateCode {

    // 生成验证码
    @Test
    public void testValidate(){
        Integer code4 = ValidateCodeUtils.generateValidateCode(4);
        System.out.println("生成4位验证码："+code4);

        Integer code6 = ValidateCodeUtils.generateValidateCode(6);
        System.out.println("生成6位验证码："+code6);
    }
}
