package com.wenx.demo.multithread;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Wenx
 * @date 2019/11/17
 */
public class ConcurrentLinkedQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        // 不需要指定队列大小
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

        // 消费者取数据
        new Thread(() -> {
            for (; ; ) {
                try {
                    System.out.println("取到数据：" + queue.poll()); // poll非阻塞（如果队列为空，则返回null）

                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                }
            }
        }).start();

        // 等待3秒，让消费者先跑起来
        TimeUnit.SECONDS.sleep(3L);

        // 生成者存数据
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    String threadName = Thread.currentThread().getName();

                    // offer非阻塞（如果队列已满，则返回false）
                    System.out.println(threadName + "存入" + queue.offer(threadName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
