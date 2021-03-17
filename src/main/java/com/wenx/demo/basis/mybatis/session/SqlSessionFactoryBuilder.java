package com.wenx.demo.basis.mybatis.session;

import com.wenx.demo.basis.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.wenx.demo.basis.mybatis.utils.XMLConfigBuilder;

import java.io.InputStream;

/**
 * SqlSessionFactory构造者
 *
 * @author Wenx
 * @date 2021/3/17
 */
public class SqlSessionFactoryBuilder {

    /**
     * 构建SqlSessionFactory工厂对象
     *
     * @param inputStream 配置文件字节输入流
     * @return
     */
    public SqlSessionFactory build(InputStream inputStream) {
        XMLConfigBuilder parser = new XMLConfigBuilder(inputStream);
        return new DefaultSqlSessionFactory(parser.parse());
    }
}
