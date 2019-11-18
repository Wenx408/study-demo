package com.wenx.demo.study.multithread;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/15 9:19
 * @Modified By：
 */
public class ReadWriteLockDemo {
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 可多个线程读取，共享锁
     *
     * @param thread
     */
    public void read(Thread thread) {
        readWriteLock.readLock().lock();
        try {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName() + "正在进行“读”操作");
            }
            System.out.println(thread.getName() + "“读”操作完毕");
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * 只能单个线程写入，独占锁
     *
     * @param thread
     */
    public void write(Thread thread) {
        readWriteLock.writeLock().lock();
        try {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName() + "正在进行“写”操作");
            }
            System.out.println(thread.getName() + "“写”操作完毕");
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public static void main(String[] args) {
        ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();

        // 多线程同时读/写
        new Thread(() -> {
            readWriteLockDemo.read(Thread.currentThread());
        }).start();

        new Thread(() -> {
            readWriteLockDemo.read(Thread.currentThread());
        }).start();

        new Thread(() -> {
            readWriteLockDemo.write(Thread.currentThread());
        }).start();
    }

}
