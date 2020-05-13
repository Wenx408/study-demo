package com.wenx.demo.multithread;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Wenx
 * @date 2019/11/18
 */
public class MyCountDownLatch {
    //MyAqs aqs = new MyAqs() {
    //
    //    @Override
    //    public int tryAcquireShared() {
    //        return (this.getState().get() == 0) ? 1 : -1;
    //    }
    //
    //    @Override
    //    public boolean tryReleaseShared() {
    //        return this.getState().decrementAndGet() == 0;
    //    }
    //};
    //
    //public MyCountDownLatch(int count) {
    //    aqs.getState().set(count);
    //}
    //
    //public void await() {
    //    aqs.acquireShared();
    //}
    //
    //public void countDown() {
    //    aqs.releaseShared();
    //}


    volatile AtomicInteger count;
    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    public MyCountDownLatch(int count) {
        this.count = new AtomicInteger(count);
    }

    public void await() {
        boolean addQ = true;
        while (this.count.get() != 0) {
            if (addQ) {
                waiters.offer(Thread.currentThread());
                addQ = false;
            } else {
                LockSupport.park(); // 收到 unpark 通知之后唤醒，继续循环
            }
        }
        waiters.remove(Thread.currentThread()); // 从等待集合中移除线程
    }

    public void countDown() {
        if (this.count.decrementAndGet() == 0) {
            // 通知其他等待线程
            Iterator<Thread> iterator = waiters.iterator();
            while (iterator.hasNext()) {
                Thread waiter = iterator.next();
                LockSupport.unpark(waiter); // 唤醒线程继续
            }
        }
    }

}
