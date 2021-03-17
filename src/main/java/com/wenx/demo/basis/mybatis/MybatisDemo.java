package com.wenx.demo.basis.mybatis;

import com.wenx.demo.basis.mybatis.dao.ITest1;
import com.wenx.demo.basis.mybatis.entity.Test1;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * 原生Mybatis演示
 *
 * @author Wenx
 * @date 2021/3/17
 */
public class MybatisDemo {
    public static void main(String[] args) throws Exception {
        // 1.读取配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        // 2.创建 SqlSessionFactory 的构建者对象
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        // 3.使用构建者创建工厂对象 SqlSessionFactory
        SqlSessionFactory factory = builder.build(in);
        // 4.使用 SqlSessionFactory 生产 SqlSession 对象
        SqlSession session = factory.openSession();
        // 5.使用 SqlSession 创建 dao 接口的代理对象
        ITest1 test1Dao = session.getMapper(ITest1.class);
        // 6.使用代理对象执行查询所有方法
        List<Test1> test1s = test1Dao.findAll();
        for (Test1 test1 : test1s) {
            System.out.println(test1);
        }
        // 7.释放资源
        session.close();
        in.close();
    }
}
