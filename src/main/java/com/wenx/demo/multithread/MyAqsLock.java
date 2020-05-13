package com.wenx.demo.multithread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Wenx
 * @date 2019/11/15
 */
public class MyAqsLock implements Lock {
    // 抽象工具类AQS
    MyAqs aqs = new MyAqs() {
        @Override
        public boolean tryAcquire() {
            return owner.compareAndSet(null, Thread.currentThread());
        }

        @Override
        public boolean tryRelease() {
            return owner.compareAndSet(Thread.currentThread(), null);
        }
    };

    @Override
    public boolean tryLock() {
        return aqs.tryAcquire();
    }

    @Override
    public void lock() {
        aqs.acquire();
    }

    @Override
    public void unlock() {
        aqs.release();
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
