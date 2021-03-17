package com.wenx.demo.basis.mybatis.dao;

import com.wenx.demo.basis.mybatis.entity.Test1;

import java.util.List;

/**
 * Test1持久层接口
 *
 * @author Wenx
 * @date 2021/3/16
 */
public interface ITest1 {

    /**
     * 查询所有数据
     *
     * @return
     */
    List<Test1> findAll();

}
