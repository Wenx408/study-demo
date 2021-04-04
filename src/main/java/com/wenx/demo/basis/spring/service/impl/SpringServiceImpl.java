package com.wenx.demo.basis.spring.service.impl;

import com.wenx.demo.basis.spring.dao.ISpringDao;
import com.wenx.demo.basis.spring.service.ISpringService;

/**
 * 业务层接口实现
 *
 * @author Wenx
 * @date 2021/3/27
 */
public class SpringServiceImpl implements ISpringService {
    private ISpringDao springDao;

    /**
     * 构造函数注入（依赖注入）
     *
     * @param springDao 持久层接口实现
     */
    public SpringServiceImpl(ISpringDao springDao) {
        this.springDao = springDao;

        System.out.println("SpringServiceImpl对象实例化了……");
    }

    @Override
    public void save() {
        springDao.save();
    }
}
