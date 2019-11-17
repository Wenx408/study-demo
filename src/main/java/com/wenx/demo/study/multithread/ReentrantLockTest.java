package com.wenx.demo.study.multithread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/14 12:55
 * @Modified By：
 */
public class ReentrantLockTest {
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
