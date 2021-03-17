package com.wenx.demo.basis.mybatis.session;

import com.wenx.demo.basis.mybatis.utils.Executor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Wenx
 * @date 2021/3/17
 */
public class MapperProxy implements InvocationHandler {
    /**
     * map的key是dao的全限定类名+方法名
     */
    private final Map<String, MappedStatement> mappers;
    private final Executor executor;

    public MapperProxy(Map<String, MappedStatement> mappers, Executor executor) {
        this.mappers = mappers;
        this.executor = executor;
    }

    /**
     * 用于对dao方法进行增强，实际就是与mapper映射文件做了关联，获取了mapper映射文件的SQL来执行
     *
     * @param proxy  类加载器
     * @param method dao接口方法
     * @param args   参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1.获取方法名
        String methodName = method.getName();
        // 2.获取方法所在类的名称
        String className = method.getDeclaringClass().getName();
        // 3.组合key
        String key = className + "." + methodName;
        // 4.获取mappers中的MappedStatement对象
        MappedStatement mapper = this.mappers.get(key);
        // 5.判断是否有MappedStatement
        if (mapper == null) {
            throw new IllegalArgumentException("参数不合法");
        }
        // 6.调用工具类执行SQL语句
        return this.executor.query(mapper);
    }
}
