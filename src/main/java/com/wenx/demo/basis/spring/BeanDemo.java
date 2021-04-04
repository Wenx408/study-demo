package com.wenx.demo.basis.spring;

import com.wenx.demo.basis.spring.service.ISpringService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Wenx
 * @date 2021/3/31
 */
public class BeanDemo {
    public static void main(String[] args) {
        // ApplicationContext没有close方法，为触发对象的销毁方法我们使用ClassPathXmlApplicationContext
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("BeanConfig.xml");
        // bean配置中lazy-init置为true开启延时加载，此处可断点测试
        ISpringService ss1 = (ISpringService) ac.getBean("beanService");
        // 调用对象方法
        ss1.save();
        System.out.println(ss1);

        // 再次获取bean检测是否单例
        ISpringService ss2 = ac.getBean("beanService", ISpringService.class);
        System.out.println(ss2);

        // 单例对象随容器的销毁而消亡，多例对象由Java的垃圾回收器回收
        ac.close();
    }
}
