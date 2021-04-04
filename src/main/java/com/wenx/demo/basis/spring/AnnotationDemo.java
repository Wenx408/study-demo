package com.wenx.demo.basis.spring;

import com.wenx.demo.basis.spring.config.SpringConfiguration;
import com.wenx.demo.basis.spring.service.ISpringService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Wenx
 * @date 2021/4/2
 */
public class AnnotationDemo {
    public static void main(String[] args) {
        // ApplicationContext没有close方法，为触发对象的销毁方法我们使用AnnotationConfigApplicationContext
        //ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("AnnotationConfig.xml");
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        // 获取bean对象
        ISpringService ss1 = (ISpringService) ac.getBean("annotationService");
        // 调用对象方法
        ss1.save();
        System.out.println(ss1);

        // 再次获取bean检测是否单例
        ISpringService ss2 = ac.getBean("annotationService", ISpringService.class);
        System.out.println(ss2);

        // 单例对象随容器的销毁而消亡，多例对象由Java的垃圾回收器回收
        ac.close();
    }
}
