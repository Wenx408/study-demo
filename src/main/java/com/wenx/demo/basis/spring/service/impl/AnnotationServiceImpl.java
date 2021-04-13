package com.wenx.demo.basis.spring.service.impl;

import com.wenx.demo.basis.spring.dao.ISpringDao;
import com.wenx.demo.basis.spring.service.ISpringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * 业务层
 *
 * @author Wenx
 * @date 2021/4/2
 */
@Service("annotationService")
@Scope("singleton")
// 只读型事务配置
//@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AnnotationServiceImpl implements ISpringService {

    @Autowired
    private ISpringDao annotationDao1;

    @Autowired
    @Qualifier("annotationDao")
    private ISpringDao annotationDao2;

    @Resource(name = "annotationDao")
    private ISpringDao annotationDao3;

    @Value("root")
    //@Value("${jdbc.username}")
    private String username;

    @Override
    // 读写事务配置
    //@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void save() {
        System.out.println("AnnotationServiceImpl的save方法执行了……");

        annotationDao1.save();
        annotationDao2.save();
        annotationDao3.save();

        System.out.println(username);
    }

    @PostConstruct
    public void init() {
        System.out.println("AnnotationServiceImpl对象初始化了……");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("AnnotationServiceImpl对象销毁了……");
    }
}
