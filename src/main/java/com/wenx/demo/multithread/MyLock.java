package com.wenx.demo.multithread;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Wenx
 * @date 2019/11/15
 */
public class MyLock implements Lock {
    // 当前锁的拥有者
    volatile AtomicReference<Thread> owner = new AtomicReference<>();
    // 等待集合（锁池）
    volatile LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    /**
     * 尝试获取一次锁
     *
     * @return
     */
    @Override
    public boolean tryLock() {
        return owner.compareAndSet(null, Thread.currentThread());
    }

    /**
     * 获取锁
     */
    @Override
    public void lock() {
        boolean addQ = true;
        while (!tryLock()) {
            if (addQ) {
                // 没获取到锁，加入到等待集合中
                waiters.offer(Thread.currentThread());
                addQ = false;
            } else {
                // 挂起当前线程，等待其他线程释放
                LockSupport.park(); // 收到 unpark 通知之后唤醒，继续循环
            }
        }
        waiters.remove(Thread.currentThread()); // 从等待集合中移除线程
    }

    /**
     * 释放锁
     */
    @Override
    public void unlock() {
        // CAS 修改 owner 拥有者
        if (owner.compareAndSet(Thread.currentThread(), null)) {
            // 通知其他等待线程
            Iterator<Thread> iterator = waiters.iterator();
            while (iterator.hasNext()) {
                Thread waiter = iterator.next();
                LockSupport.unpark(waiter); // 唤醒线程继续 抢锁
            }
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
