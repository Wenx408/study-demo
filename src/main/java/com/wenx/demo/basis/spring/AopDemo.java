package com.wenx.demo.basis.spring;

import com.wenx.demo.basis.spring.config.SpringConfiguration;
import com.wenx.demo.basis.spring.service.ISpringService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Wenx
 * @date 2021/4/4
 */
public class AopDemo {
    public static void main(String[] args) {
        // 1.获取IoC核心容器对象
        ApplicationContext ac = new ClassPathXmlApplicationContext("AopConfig.xml");
        //ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        // 2.获取bean对象
        ISpringService ss = ac.getBean("aopService", ISpringService.class);
        // 3.触发环绕通知
        ss.save();

        System.out.println(ss);
    }
}
