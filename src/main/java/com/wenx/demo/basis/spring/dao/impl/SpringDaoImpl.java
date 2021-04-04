package com.wenx.demo.basis.spring.dao.impl;

import com.wenx.demo.basis.spring.dao.ISpringDao;

/**
 * 持久层接口实现
 *
 * @author Wenx
 * @date 2021/3/27
 */
public class SpringDaoImpl implements ISpringDao {
    public SpringDaoImpl() {
        System.out.println("SpringDaoImpl对象实例化了……");
    }

    @Override
    public void save() {
        System.out.println("保存成功！");
    }
}
