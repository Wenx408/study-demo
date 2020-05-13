package com.wenx.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * 启动类
 *
 * @author Wenx
 * @date 2020/1/13
 */
@SpringBootApplication
public class StudyDemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(StudyDemoApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(StudyDemoApplication.class, args);
    }
}
