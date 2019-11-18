package com.wenx.demo.study.multithread;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/13 20:19
 * @Modified By：
 */
public class VisibilityDemo {
    //private static boolean flag = true;
    private static volatile boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        // 创建线程循环进行累加
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (VisibilityDemo.flag) {
                    //synchronized (this) {
                        i++;
                    //}
                }
                System.out.println("结果为：" + i);
            }
        }).start();

        TimeUnit.SECONDS.sleep(2);
        // 设置flag为false，使线程结束while循环，输出结果
        VisibilityDemo.flag = false;
        System.out.println("flag设置为false");
    }
}
