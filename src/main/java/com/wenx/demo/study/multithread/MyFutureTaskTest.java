package com.wenx.demo.study.multithread;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/10/19 16:41
 * @Modified By：
 */
// 分析特征：构造函数/泛型/Runnable/GET返回结果
public class MyFutureTaskTest<T> implements Runnable {
    Callable<T> callable; //包装了业务逻辑代码
    T result; //线程执行结果
    volatile String state = "NEW"; //任务执行的状态
    // 容器把等待中的线程保存起来 -- 停车场
    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    public MyFutureTaskTest(Callable<T> callable) {
        this.callable = callable;
    }

    // 查询 -- 异步线程要去调用的
    @Override
    public void run() { //线程启动后，会执行run方法
        System.out.println(Thread.currentThread().getName() + "开始执行");
        // 业务逻辑执行
        try {
            result = callable.call();
        } catch (Exception e) {
            //result =
            //state = "EXP"; //异常状态
            e.printStackTrace();
        } finally {
            state = "END";
        }
        System.out.println(Thread.currentThread().getName() + "执行完了");

        // TODO 唤醒等待的线程
        System.out.println("阻塞进程数：" + waiters.size());
        Thread waiter = waiters.poll(); //停车场取数据
        while (waiter != null) {
            System.out.println(waiter.getName() + "解除阻塞");
            LockSupport.unpark(waiter); //唤醒指定的线程 --- waiter
            System.out.println(waiter.getName() + "解除阻塞完毕");
            waiter = waiters.poll(); //停车场取数据
        }

    }

    // 调用 --- 方法执行主线程
    public T get() { //获取返回值
        if ("END".equals(state)) {
            return result;
        } //else if (exception) 抛出异常

        // TODO 等待...如何控制线程的执行（停），需要等待run 这个线程执行完毕
        // 线程不继续执行代码，去什么地方？ 正在跑的车 ---> 停车场
        while (!"END".equals(state)) {
            waiters.add(Thread.currentThread());
            System.out.println(Thread.currentThread().getName() + "准备阻塞");
            LockSupport.park(); //当前线程阻塞等待 --- native方法控制 C++底层实现
            System.out.println(Thread.currentThread().getName() + "阻塞完毕");
        }

        return result;
    }
}
