package com.wenx.demo.multithread;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Wenx
 * @date 2019/11/15
 */
public class MyAqs {
    // 当前锁的拥有者
    volatile AtomicReference<Thread> owner = new AtomicReference<>();
    // 等待集合（锁池）
    volatile LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();
    // 记录锁的状态
    volatile AtomicInteger state = new AtomicInteger(0);

    /**
     * 获取独占锁
     */
    public void acquire() {
        boolean addQ = true;
        while (!tryAcquire()) {
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
     * 释放独占锁
     */
    public void release() {
        if (tryRelease()) {
            // 通知其他等待线程
            Iterator<Thread> iterator = waiters.iterator();
            while (iterator.hasNext()) {
                Thread waiter = iterator.next();
                LockSupport.unpark(waiter); // 唤醒线程继续 抢锁
            }
        }
    }

    /**
     * 获取共享锁
     */
    public void acquireShared() {
        boolean addQ = true;
        while (tryAcquireShared() < 0) {
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
     * 释放共享锁
     */
    public void releaseShared() {
        if (tryReleaseShared()) {
            // 通知其他等待线程
            Iterator<Thread> iterator = waiters.iterator();
            while (iterator.hasNext()) {
                Thread waiter = iterator.next();
                LockSupport.unpark(waiter); // 唤醒线程继续 抢锁
            }
        }
    }

    /**
     * 获取独占锁逻辑，实际使用者去实现
     *
     * @return
     */
    public boolean tryAcquire() {
        throw new UnsupportedOperationException();
    }

    /**
     * 释放独占锁逻辑，实际使用者去实现
     *
     * @return
     */
    public boolean tryRelease() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取共享锁逻辑，实际使用者去实现
     *
     * @return
     */
    public int tryAcquireShared() {
        throw new UnsupportedOperationException();
    }

    /**
     * 释放共享锁逻辑，实际使用者去实现
     *
     * @return
     */
    public boolean tryReleaseShared() {
        throw new UnsupportedOperationException();
    }

    public AtomicInteger getState() {
        return state;
    }

    public void setState(AtomicInteger state) {
        this.state = state;
    }

}
