package com.wenx.demo.study;

import com.wenx.demo.study.config.daoutils.DaoUtils;
import com.wenx.demo.study.config.daoutils.TestDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @Author: Wenx
 * @Description: 启动类
 * @Date: Created in 2020/1/13 20:13
 * @Modified By：
 */
@SpringBootApplication
public class StudyDemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(StudyDemoApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(StudyDemoApplication.class, args);

        TestDO testDO = DaoUtils.getTest(1);
    }
}
