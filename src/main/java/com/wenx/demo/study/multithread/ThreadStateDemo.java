package com.wenx.demo.study.multithread;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/11 21:40
 * @Modified By：
 */
public class ThreadStateDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("线程1当前状态2：" + Thread.currentThread().getState());
        });
        System.out.println("线程1当前状态1：" + t1.getState());
        t1.start();
        Thread.sleep(200L);
        System.out.println("线程1当前状态3：" + t1.getState());

        Thread t2 = new Thread(() -> {
            try {
                System.out.println("线程2当前状态2：" + Thread.currentThread().getState());
                Thread.sleep(200L);
                System.out.println("线程2当前状态4：" + Thread.currentThread().getState());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("线程2当前状态1：" + t2.getState());
        t2.start();
        Thread.sleep(100L);
        System.out.println("线程2当前状态3：" + t2.getState());
        Thread.sleep(200L);
        System.out.println("线程2当前状态5：" + t2.getState());

        Thread t3 = new Thread(() -> {
            synchronized (ThreadStateDemo.class) {
                System.out.println("线程3当前状态4：" + Thread.currentThread().getState());
            }
        });
        synchronized (ThreadStateDemo.class) {
            System.out.println("线程3当前状态1：" + t3.getState());
            t3.start();
            System.out.println("线程3当前状态2：" + t3.getState());
            Thread.sleep(200L);
            System.out.println("线程3当前状态3：" + t3.getState());
        }
        Thread.sleep(200L);
        System.out.println("线程3当前状态5：" + t3.getState());

        Object obj = new Object();
        Thread t4 = new Thread(() -> {
            try {
                synchronized (obj) {
                    System.out.println("线程4当前状态2：" + Thread.currentThread().getState());
                    obj.wait();
                    System.out.println("线程4当前状态4：" + Thread.currentThread().getState());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("线程4当前状态1：" + t4.getState());
        t4.start();
        Thread.sleep(100L);
        System.out.println("线程4当前状态3：" + t4.getState());
        synchronized (obj) {
            obj.notify();
        }
        Thread.sleep(200L);
        System.out.println("线程4当前状态5：" + t4.getState());
    }

}
