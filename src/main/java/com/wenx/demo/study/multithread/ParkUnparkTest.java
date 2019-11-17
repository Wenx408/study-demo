package com.wenx.demo.study.multithread;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/11 21:42
 * @Modified By：
 */
public class ParkUnparkTest {
    public static Object baozi = null;

    public static void main(String[] args) throws InterruptedException {
        // 启动线程
        Thread t1 = new Thread(() -> {
            while (baozi == null) { // 如果没包子，则进入等待
                System.out.println("没包子，坐等");
                LockSupport.park();
            }
            System.out.println("买到包子，走人");
        });
        t1.start();
        // 2秒之后，做了个包子
        Thread.sleep(2000L);
        baozi = new Object();
        LockSupport.unpark(t1);
        System.out.println("包子出锅了");
    }

}
