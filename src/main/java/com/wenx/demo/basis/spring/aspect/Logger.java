package com.wenx.demo.basis.spring.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 日志记录器，提取业务方法中的日志公共代码
 *
 * @author Wenx
 * @date 2021/4/4
 */
public class Logger {

    /**
     * 前置通知
     */
    public void beforePrintLog() {
        System.out.println("前置通知：Logger的beforePrintLog方法执行了……");
    }

    /**
     * 后置通知
     */
    public void afterReturningPrintLog() {
        System.out.println("后置通知：Logger的afterReturningPrintLog方法执行了……");
    }

    /**
     * 异常通知
     */
    public void afterThrowingPrintLog() {
        System.out.println("异常通知：Logger的afterThrowingPrintLog方法执行了……");
    }

    /**
     * 最终通知
     */
    public void afterPrintLog() {
        System.out.println("最终通知：Logger的afterPrintLog方法执行了……");
    }

    /**
     * 环绕通知：一种可以在代码中手动控制增强方法何时执行的方式
     *
     * @param pjp Spring框架提供的ProceedingJoinPoint接口，该接口的proceed()方法相当于切入点方法。
     * @return
     */
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
