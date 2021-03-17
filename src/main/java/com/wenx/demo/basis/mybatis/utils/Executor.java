package com.wenx.demo.basis.mybatis.utils;

import com.wenx.demo.basis.mybatis.session.MappedStatement;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于执行SQL语句
 *
 * @author Wenx
 * @date 2021/3/17
 */
public class Executor {
    private final JdbcTransaction transaction;

    public Executor(JdbcTransaction transaction) {
        this.transaction = transaction;
    }

    public void close() {
        try {
            if (this.transaction != null) {
                this.transaction.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <E> List<E> query(MappedStatement mapper) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // 1.取出MappedStatement中的数据
            String sqlStatement = mapper.getSqlStatement();
            String resultType = mapper.getResultType();
            Class entityClass = Class.forName(resultType);
            // 2.获取PreparedStatement对象
            Connection conn = this.transaction.getConnection();
            pstmt = conn.prepareStatement(sqlStatement);
            // 3.执行SQL语句，获取结果集
            rs = pstmt.executeQuery();
            // 4.封装结果集
            List<E> list = new ArrayList<E>();
            while (rs.next()) {
                // 实例化要封装的实体类对象
                E obj = (E) entityClass.newInstance();

                // 取出结果集的元信息：ResultSetMetaData
                ResultSetMetaData rsmd = rs.getMetaData();
                // 取出总列数
                int columnCount = rsmd.getColumnCount();
                // 遍历总列数
                for (int i = 1; i <= columnCount; i++) {
                    // 获取每列的名称，列名的序号是从1开始的
                    String columnName = rsmd.getColumnName(i);
                    // 根据得到列名，获取每列的值
                    Object columnValue = rs.getObject(columnName);
                    // 给obj赋值：使用Java内省机制（借助PropertyDescriptor实现属性的封装）
                    // 要求：实体类的属性和数据库表的列名保持一种
                    PropertyDescriptor pd = new PropertyDescriptor(columnName, entityClass);
                    // 获取它的写入方法
                    Method writeMethod = pd.getWriteMethod();
                    // 把获取的列的值，给对象赋值
                    writeMethod.invoke(obj, columnValue);
                }
                // 把赋好值的对象加入到集合中
                list.add(obj);
            }

            this.transaction.commit();

            return list;
        } catch (Exception e) {
            e.printStackTrace();

            try {
                this.transaction.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            release(pstmt, rs);
        }
        return null;
    }

    private void release(PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
