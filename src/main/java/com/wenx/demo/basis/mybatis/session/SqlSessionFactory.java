package com.wenx.demo.basis.mybatis.session;

/**
 * SqlSession工厂
 *
 * @author Wenx
 * @date 2021/3/17
 */
public interface SqlSessionFactory {

    /**
     * 打开一个会话，获得SqlSession对象
     *
     * @return
     */
    SqlSession openSession();
}
