package com.wenx.demo.study.multithread;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/18 9:50
 * @Modified By：
 */
public class RunnableDemo {
    public static void main(String[] args) {
        // 可以定义好一个类实现Runnable接口
        RunnableImpl runnable = new RunnableImpl();
        new Thread(runnable).start();

        // 省事写法
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "当前线程");
            }
        }).start();

        // lambda函数式接口，更优雅
        new Thread(() -> System.out.println(Thread.currentThread().getName() + "当前线程")).start();
    }

}

class RunnableImpl implements Runnable {

    @Override
    public void run() {
        // 具体执行逻辑
        System.out.println(Thread.currentThread().getName() + "当前线程");
    }
}
