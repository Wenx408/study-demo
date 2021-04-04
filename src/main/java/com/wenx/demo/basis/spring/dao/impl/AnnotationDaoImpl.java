package com.wenx.demo.basis.spring.dao.impl;

import com.wenx.demo.basis.spring.dao.ISpringDao;
import org.springframework.stereotype.Repository;

/**
 * 持久层
 *
 * @author Wenx
 * @date 2021/4/2
 */
@Repository("annotationDao")
public class AnnotationDaoImpl implements ISpringDao {
    @Override
    public void save() {
        System.out.println("AnnotationDaoImpl的save方法执行了……");
    }
}
