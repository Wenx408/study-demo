package com.wenx.demo.config.daoutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 静态工具类中使用@Autowired的方法
 *
 * @author Wenx
 * @date 2020/1/13
 */
@Component
public class DaoUtils {
    private static final Logger logger = LoggerFactory.getLogger(DaoUtils.class);
    private static DaoUtils utils;

    @Autowired
    private TestMapper testMapper;

    @PostConstruct
    public void init() {
        utils = this;
    }

    public static TestDO getTest(Integer id) {
        TestDO testDO = utils.testMapper.selectTestById(1);
        return testDO;
    }

}
