package com.wenx.demo.basis.spring.service.impl;

import com.wenx.demo.basis.spring.service.ISpringService;

/**
 * @author Wenx
 * @date 2021/3/31
 */
public class BeanServiceImpl implements ISpringService {

    public BeanServiceImpl() {
        System.out.println("BeanServiceImpl对象实例化了……");
    }

    @Override
    public void save() {
        System.out.println("BeanServiceImpl的save方法执行了……");
    }

    public void init() {
        System.out.println("BeanServiceImpl对象初始化了……");
    }

    public void destroy() {
        System.out.println("BeanServiceImpl对象销毁了……");
    }
}
