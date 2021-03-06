package com.wenx.demo.multithread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Wenx
 * @date 2019/11/14
 */
public class ReentrantLockDemo {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + "获取锁的次数 " + lock.getHoldCount());
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "获取锁的次数 " + lock.getHoldCount());
        } finally {
            //lock.unlock();
            lock.unlock();
        }
        System.out.println(Thread.currentThread().getName() + "获取锁的次数 " + lock.getHoldCount());

        // 若不释放锁，其他线程是抢不到锁的
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 期望抢到锁");
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " 抢到了锁");
        }).start();
    }
}
