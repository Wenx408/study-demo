package com.wenx.demo.basis.spring.controller;

import org.springframework.stereotype.Controller;

/**
 * 表现层
 *
 * @author Wenx
 * @date 2021/4/2
 */
@Controller
public class AnnotationController {
    public void save() {
        System.out.println("AnnotationController的save方法执行了……");
    }
}
