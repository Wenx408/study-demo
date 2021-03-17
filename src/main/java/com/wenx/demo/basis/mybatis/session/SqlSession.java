package com.wenx.demo.basis.mybatis.session;

/**
 * 数据库连接会话，可创建dao接口的代理对象
 *
 * @author Wenx
 * @date 2021/3/17
 */
public interface SqlSession {

    /**
     * 创建dao接口的代理对象
     *
     * @param daoInterfaceClass dao的接口字节码
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<T> daoInterfaceClass);

    /**
     * 释放资源
     */
    void close();
}
