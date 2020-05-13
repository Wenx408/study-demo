package com.wenx.demo.multithread;

/**
 * @author Wenx
 * @date 2019/11/11
 */
public class WaitNotifyDemo {
    public static Object lock = new Object();
    public static Object baozi = null;

    public static void main(String[] args) throws InterruptedException {
        // 启动线程
        new Thread(() -> {
            synchronized (lock) {
                while (baozi == null) { // 如果没包子，则进入等待
                    try {
                        System.out.println("没包子，坐等");
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("买到包子，走人");
        }).start();
        // 2秒之后，做了个包子
        Thread.sleep(2000L);
        baozi = new Object();
        synchronized (lock) {
            lock.notifyAll();
            System.out.println("包子出锅了");
        }
    }

}
