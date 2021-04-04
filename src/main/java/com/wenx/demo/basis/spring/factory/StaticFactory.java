package com.wenx.demo.basis.spring.factory;

import com.wenx.demo.basis.spring.service.ISpringService;
import com.wenx.demo.basis.spring.service.impl.BeanServiceImpl;

/**
 * 模拟一个静态工厂，创建业务层实现类（部分类可能存在jar包中无默认构造函数）
 *
 * @author Wenx
 * @date 2021/3/31
 */
public class StaticFactory {

    public static ISpringService getBeanService() {
        return new BeanServiceImpl();
    }
}
