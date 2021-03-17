package com.wenx.demo.basis.mybatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询语句的注解
 *
 * @author Wenx
 * @date 2021/3/17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
    /**
     * SQL语句
     *
     * @return
     */
    String value();
}
