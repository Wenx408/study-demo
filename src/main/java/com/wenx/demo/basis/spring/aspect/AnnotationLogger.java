package com.wenx.demo.basis.spring.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 日志记录器，提取业务方法中的日志公共代码
 *
 * @author Wenx
 * @date 2021/4/4
 */
@Component("logger")
@Aspect
public class AnnotationLogger {

    @Pointcut("execution(* com.wenx.demo.basis.spring.service.impl.*.*(..))")
    private void pc1() {
    }

    /**
     * 前置通知
     */
    @Before("pc1()")
    public void beforePrintLog() {
        System.out.println("前置通知：Logger的beforePrintLog方法执行了……");
    }

    /**
     * 后置通知
     */
    @AfterReturning("pc1()")
    public void afterReturningPrintLog() {
        System.out.println("后置通知：Logger的afterReturningPrintLog方法执行了……");
    }

    /**
     * 异常通知
     */
    @AfterThrowing("pc1()")
    public void afterThrowingPrintLog() {
        System.out.println("异常通知：Logger的afterThrowingPrintLog方法执行了……");
    }

    /**
     * 最终通知
     */
    @After("pc1()")
    public void afterPrintLog() {
        System.out.println("最终通知：Logger的afterPrintLog方法执行了……");
    }

    /**
     * 环绕通知：一种可以在代码中手动控制增强方法何时执行的方式
     *
     * @param pjp Spring框架提供的ProceedingJoinPoint接口，该接口的proceed()方法相当于切入点方法。
     * @return
     */
    //@Around("pc1()")
    public Object aroundPrintLog(ProceedingJoinPoint pjp) {
        Object returnValue = null;
        try {
            System.out.println("前置通知：Logger的aroundPrintLog方法执行了……");

            // 获取方法的参数列表
            Object[] args = pjp.getArgs();
            // 调用业务层方法（切入点方法）
            returnValue = pjp.proceed(args);

            System.out.println("后置通知：Logger的aroundPrintLog方法执行了……");

            return returnValue;
        } catch (Throwable t) {
            System.out.println("异常通知：Logger的aroundPrintLog方法执行了……");
            throw new RuntimeException(t);
        } finally {
            System.out.println("最终通知：Logger的aroundPrintLog方法执行了……");
        }
    }
}
