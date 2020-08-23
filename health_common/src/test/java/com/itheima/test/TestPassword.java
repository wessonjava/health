package com.itheima.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName TestPassword
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/1/11 12:00
 * @Version V1.0
 */
public class TestPassword {

    /**
     * （1）加密(encode)：注册用户时，使用SHA-256+随机盐+密钥把用户输入的密码进行hash处理，得到密码的hash值，然后将其存入数据库中。
     （2）密码匹配(matches)：用户登录时，密码匹配阶段并没有进行密码解密（因为密码经过Hash处理，是不可逆的），而是使用相同的算法把用户输入的密码进行hash处理，得到密码的hash值，然后将其与从数据库中查询到的密码hash值进行比较。如果两者相同，说明用户输入的密码正确。
     */
    @Test
    public void test(){
        // encode：对密码进行加密，用在向数据库保存数据
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password1 = bCryptPasswordEncoder.encode("123");
        System.out.println(password1);
        String password2 = bCryptPasswordEncoder.encode("123");
        System.out.println(password2);

        //matches：使用页面登录的密码和数据库中的密码进行比对，返回是一个boolean类型，true：匹配成功，可以登录
        boolean matches = bCryptPasswordEncoder.matches("1234", "$2a$10$g.l1fdM2BhGgc8Aw2kS1uutRJDApXU8L1uIi/fVMNCKNLGgcoELze");
        System.out.println(matches);
    }
}
