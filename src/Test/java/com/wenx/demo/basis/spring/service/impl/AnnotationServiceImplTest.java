package com.wenx.demo.basis.spring.service.impl;

import com.wenx.demo.basis.spring.config.SpringConfiguration;
import com.wenx.demo.basis.spring.service.ISpringService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Spring整合Junit4进行单元测试
 *
 * @author Wenx
 * @date 2021/4/2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class AnnotationServiceImplTest {

    @Autowired
    private ISpringService annotationService;

    @Before
    public void setUp() throws Exception {
        System.out.println("执行测试方法之前运行……");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("执行测试方法之后运行……");
    }

    @Test
    public void save() {
        annotationService.save();
    }
}