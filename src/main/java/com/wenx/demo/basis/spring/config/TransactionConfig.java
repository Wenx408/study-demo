package com.wenx.demo.basis.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 事务相关配置类
 *
 * @author Wenx
 * @date 2021/4/13
 */
public class TransactionConfig {

    /**
     * 创建事务管理器对象
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager createTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
