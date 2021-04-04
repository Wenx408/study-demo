package com.wenx.demo.basis.spring.service.impl;

import com.wenx.demo.basis.spring.service.ISpringService;

import java.util.Date;

/**
 * @author Wenx
 * @date 2021/3/31
 */
public class DI1ServiceImpl implements ISpringService {
    private String name;
    private Integer age;
    private Date birthday;

    /**
     * 构造函数注入（依赖注入）
     *
     * @param name     姓名
     * @param age      年龄
     * @param birthday 生日
     */
    public DI1ServiceImpl(String name, Integer age, Date birthday) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    @Override
    public void save() {
        System.out.println("DI1ServiceImpl的save方法执行了……\r\n" +
                "姓名：" + name + "，年龄：" + age + "，生日：" + birthday);
    }
}
