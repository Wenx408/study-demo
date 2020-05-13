package com.wenx.demo.multithread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Wenx
 * @date 2019/11/17
 */
public class ArrayBlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        // 构造时设置队列大小，还可以设置 公平（FIFO先进先出原则） / 非公平
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3, true);

        // 消费者取数据
        new Thread(() -> {
            for (; ; ) {
                try {
                    //System.out.println("取到数据：" + queue.poll()); // poll非阻塞（如果队列为空，则返回null）
                    System.out.println("取到数据：" + queue.take()); // take阻塞（如果队列为空，则阻塞）

                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                }
            }
        }).start();

        // 等待3秒，让消费者先跑起来
        TimeUnit.SECONDS.sleep(3L);

        // 生成者存数据
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    String threadName = Thread.currentThread().getName();

                    // offer非阻塞（如果队列已满，则返回false）
                    //System.out.println(threadName + "存入" + queue.offer(threadName));

                    // put阻塞（如果队列已满，则阻塞）
                    System.out.println(threadName + "存入");
                    queue.put(threadName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
