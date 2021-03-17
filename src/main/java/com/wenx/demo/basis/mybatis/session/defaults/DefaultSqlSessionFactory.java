package com.wenx.demo.basis.mybatis.session.defaults;

import com.wenx.demo.basis.mybatis.session.Configuration;
import com.wenx.demo.basis.mybatis.session.SqlSession;
import com.wenx.demo.basis.mybatis.session.SqlSessionFactory;
import com.wenx.demo.basis.mybatis.utils.Executor;
import com.wenx.demo.basis.mybatis.utils.JdbcTransaction;

/**
 * SqlSessionFactory接口实现
 *
 * @author Wenx
 * @date 2021/3/17
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 获取一个数据库连接会话
     *
     * @return
     */
    @Override
    public SqlSession openSession() {
        JdbcTransaction tx = new JdbcTransaction(configuration, false);
        Executor executor = new Executor(tx);
        return new DefaultSqlSession(configuration, executor);
    }
}
