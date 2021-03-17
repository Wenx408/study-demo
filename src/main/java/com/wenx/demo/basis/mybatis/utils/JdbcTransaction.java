package com.wenx.demo.basis.mybatis.utils;

import com.wenx.demo.basis.mybatis.session.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Jdbc事务相关
 *
 * @author Wenx
 * @date 2021/3/17
 */
public class JdbcTransaction {
    private final Configuration configuration;
    protected Connection connection;
    protected boolean autoCommmit;

    public JdbcTransaction(Configuration configuration, boolean desiredAutoCommit) {
        this.configuration = configuration;
        this.autoCommmit = desiredAutoCommit;
    }

    /**
     * 获取一个数据库连接
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        if (this.connection == null) {
            this.openConnection();
        }

        return this.connection;
    }

    /**
     * 创建一个数据库连接
     *
     * @throws SQLException
     */
    protected void openConnection() throws SQLException {
        try {
            Class.forName(this.configuration.getDriver());
            this.connection = DriverManager.getConnection(
                    this.configuration.getUrl(),
                    this.configuration.getUsername(),
                    this.configuration.getPassword());
            this.connection.setAutoCommit(this.autoCommmit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 事务提交
     *
     * @throws SQLException
     */
    public void commit() throws SQLException {
        if (this.connection != null && !this.connection.getAutoCommit()) {
            this.connection.commit();
        }

    }

    /**
     * 事务回滚
     *
     * @throws SQLException
     */
    public void rollback() throws SQLException {
        if (this.connection != null && !this.connection.getAutoCommit()) {
            this.connection.rollback();
        }

    }

    /**
     * 关闭数据库连接
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        if (this.connection != null) {
            this.resetAutoCommit();

            this.connection.close();
        }

    }

    /**
     * 重置事务自动提交
     */
    protected void resetAutoCommit() {
        try {
            if (!this.connection.getAutoCommit()) {
                this.connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
