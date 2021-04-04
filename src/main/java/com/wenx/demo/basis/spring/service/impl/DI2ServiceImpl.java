package com.wenx.demo.basis.spring.service.impl;

import com.wenx.demo.basis.spring.service.ISpringService;

import java.util.Date;

/**
 * @author Wenx
 * @date 2021/3/31
 */
public class DI2ServiceImpl implements ISpringService {
    private String name;
    private Integer age;
    private Date birthday;

    /**
     * set方法注入（依赖注入）
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * set方法注入（依赖注入）
     *
     * @param age 年龄
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * set方法注入（依赖注入）
     *
     * @param birthday 生日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public void save() {
        System.out.println("DI2ServiceImpl的save方法执行了……\r\n" +
                "姓名：" + name + "，年龄：" + age + "，生日：" + birthday);
    }
}
