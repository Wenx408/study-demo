package com.wenx.demo.study.multithread;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/11 21:42
 * @Modified By：
 */
public class ThreadLocalDemo {
    public static ThreadLocal<String> tl = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        tl.set("主线程值为1"); // 主线程设置值
        System.out.println("子线程执行之前：" + tl.get());

        new Thread(() -> {
            System.out.println("子线程取到的值：" + tl.get());
            tl.set("子线程的值为2");

            System.out.println("子线程设置后：" + tl.get());
            System.out.println("子线程结束");
        }).start();

        Thread.sleep(2000L);
        System.out.println("子线程执行之后：" + tl.get());
    }

}
