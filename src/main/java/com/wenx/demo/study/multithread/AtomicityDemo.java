package com.wenx.demo.study.multithread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/14 12:55
 * @Modified By：
 */
public class AtomicityDemo {
    //volatile int i = 0;
    //AtomicInteger i = new AtomicInteger(0);
    LongAdder i = new LongAdder();
    //Lock lock = new ReentrantLock();

    //static Unsafe unsafe;
    //private static long valueOffset;
    //
    //static {
    //    try {
    //        // 反射技术获取unsafe值
    //        Field field = Unsafe.class.getDeclaredField("theUnsafe");
    //        field.setAccessible(true);
    //        unsafe = (Unsafe) field.get(null);
    //
    //        // 获取到 i 属性偏移量（用于定位 i 属性在内存中的具体地址）
    //        valueOffset = unsafe.objectFieldOffset(DemoTest.class.getDeclaredField("i"));
    //
    //    } catch (Exception ex) {
    //        ex.printStackTrace();
    //    }
    //}

    public void incr() {
        //i++;
        //i.incrementAndGet();
        i.increment();

        //synchronized (this) {
        //    i++;
        //}

        //lock.lock();
        //try {
        //    i++;
        //} finally {
        //    lock.unlock();
        //}

        //// CAS自旋：CAS + 循环 重试
        //int current;
        //do {
        //    // 操作耗时的话，那么线程就会占用大量的CPU执行时间
        //    current = unsafe.getIntVolatile(this, valueOffset);
        //} while (!unsafe.compareAndSwapInt(this, valueOffset, current, current + 1));
        //// 可能会失败
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicityDemo demoTest = new AtomicityDemo();

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    demoTest.incr();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(2);
        //System.out.println("结果为：" + demoTest.i);
        System.out.println("结果为：" + demoTest.i.sum());
    }
}
