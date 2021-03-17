package com.wenx.demo.basis.mybatis.session.defaults;

import com.wenx.demo.basis.mybatis.session.Configuration;
import com.wenx.demo.basis.mybatis.session.MapperProxy;
import com.wenx.demo.basis.mybatis.session.SqlSession;
import com.wenx.demo.basis.mybatis.utils.Executor;

import java.lang.reflect.Proxy;

/**
 * SqlSession接口实现
 *
 * @author Wenx
 * @date 2021/3/17
 */
public class DefaultSqlSession implements SqlSession {
    private final Configuration configuration;
    private final Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    /**
     * 创建dao接口的代理对象
     *
     * @param daoInterfaceClass dao的接口字节码
     * @param <T>
     * @return
     */
    @Override
    public <T> T getMapper(Class<T> daoInterfaceClass) {
        return (T) Proxy.newProxyInstance(
                daoInterfaceClass.getClassLoader(),
                new Class[]{daoInterfaceClass},
                new MapperProxy(configuration.getMappers(), this.executor));
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        if (executor != null) {
            try {
                executor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
