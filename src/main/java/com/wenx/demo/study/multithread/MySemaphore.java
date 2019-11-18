package com.wenx.demo.study.multithread;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/18 13:25
 * @Modified By：
 */
public class MySemaphore {
    MyAqs aqs = new MyAqs() {

        @Override
        public int tryAcquireShared() { // 信号量获取， 数量 - 1
            for (; ; ) {
                int count = getState().get();
                int n = count - 1;
                if (count <= 0 || n < 0) {
                    return -1;
                }
                if (getState().compareAndSet(count, n)) {
                    return 1;
                }
            }
        }

        @Override
        public boolean tryReleaseShared() { // 信号量释放， 数量 + 1
            return this.getState().incrementAndGet() >= 0;
        }
    };

    /**
     * 设置令牌数量
     */
    public MySemaphore(int count) {
        aqs.getState().set(count);
    }

    /**
     * 获取令牌
     */
    public void acquire() {
        aqs.acquireShared();
    }

    /**
     * 释放令牌
     */
    public void release() {
        aqs.releaseShared();
    }

}
