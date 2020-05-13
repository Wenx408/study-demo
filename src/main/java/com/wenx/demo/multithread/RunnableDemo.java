package com.wenx.demo.multithread;

/**
 * @author Wenx
 * @date 2019/11/18
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
