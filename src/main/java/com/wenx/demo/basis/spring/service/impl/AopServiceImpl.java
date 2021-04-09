package com.wenx.demo.basis.spring.service.impl;

import com.wenx.demo.basis.spring.service.ISpringService;
import org.springframework.stereotype.Service;

/**
 * 业务层接口实现
 *
 * @author Wenx
 * @date 2021/4/4
 */
//@Service("aopService")
public class AopServiceImpl implements ISpringService {

    @Override
    public void save() {
        System.out.println("AopServiceImpl的save方法执行了……");
    }
}
