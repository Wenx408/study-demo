package com.wenx.demo.multithread;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Wenx
 * @date 2019/11/17
 */
public class SynchronousQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

        //System.out.println(synchronousQueue.add("a")); // IllegalStateException
        //System.out.println(synchronousQueue.offer("a"));
        //System.out.println(synchronousQueue.poll()); // 非阻塞

        // 阻塞：已写入，等待读取
        //new Thread(() -> {
        //    try {
        //        System.out.println("VIP快递服务送快递来了！没人取不走啊~");
        //        synchronousQueue.put("快递包裹");
        //        System.out.println("终于来人取了……走人~");
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //}).start();
        //
        //TimeUnit.SECONDS.sleep(3L);
        //
        //System.out.println("我的快递，那给我吧！");
        //System.out.println("取到了" + synchronousQueue.take());


        // 阻塞：已读取，等待写入
        new Thread(() -> {
            try {
                System.out.println("大哥行行好吧，吃不上饭了，给点钱吧！");
                System.out.println("拿到了" + synchronousQueue.take());
                System.out.println("比我还穷了，还是快走吧~");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        TimeUnit.SECONDS.sleep(3L);

        System.out.println("臭要饭的，拿了钱快走！");
        synchronousQueue.put("5毛钱");
    }
}
