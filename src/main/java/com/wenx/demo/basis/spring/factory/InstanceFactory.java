package com.wenx.demo.basis.spring.factory;

import com.wenx.demo.basis.spring.service.ISpringService;
import com.wenx.demo.basis.spring.service.impl.BeanServiceImpl;

/**
 * 模拟一个实例工厂，创建业务层实现类（部分类可能存在jar包中无默认构造函数）
 * 此工厂创建对象，必须现有工厂实例对象，再调用方法
 *
 * @author Wenx
 * @date 2021/3/31
 */
public class InstanceFactory {

    public ISpringService getBeanService() {
        return new BeanServiceImpl();
    }
}
