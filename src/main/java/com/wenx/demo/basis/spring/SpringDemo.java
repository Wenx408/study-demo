package com.wenx.demo.basis.spring;

import com.wenx.demo.basis.spring.service.ISpringService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Wenx
 * @date 2021/3/27
 */
public class SpringDemo {
    public static void main(String[] args) {
        // 1.获取IoC核心容器对象
        ApplicationContext ac = new ClassPathXmlApplicationContext("SpringConfig.xml");
        // 2.获取bean对象
        ISpringService ss = ac.getBean("springService", ISpringService.class);
        // 3.调用对象方法
        ss.save();

        System.out.println(ss);
    }
}
