package com.wenx.demo.multithread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Wenx
 * @date 2019/11/11
 */
public class ThreadPoolExecutorDemo {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个 核心线程数量为2，最大线程数量为5，等待队列为3 的线程池，相当于最大容纳8个任务
        // 默认的策略是抛出RejectedExecutionException异常，java.util.concurrent.ThreadPoolExecutor.AbortPolicy
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2, 5, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(3),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.err.println("快递网点：快递小哥不够仓库已满 / 停止业务 -> 拒收快递包裹");
                    }
                });

        // 测试： 提交 10个 执行时间需要3秒的任务，看超过2个任务时的对应处理情况
        int num = 10;
        for (int i = 0; i < num; i++) {
            int n = i + 1;
            System.out.println("快递包裹" + n + "：到达快递网点");
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("------------------------------快递小哥正在送 快递包裹" + n);
                        Thread.sleep(3000L);
                        System.out.println("------------------------------快递包裹" + n + " 已送达");
                    } catch (InterruptedException e) {
                        System.err.println("------------------------------快递小哥翻车了：" + e.getMessage());
                    }
                }
            });
            System.out.println("派件快递小哥人数为：" + threadPoolExecutor.getPoolSize());
            System.out.println("网点仓库包裹数量为：" + threadPoolExecutor.getQueue().size());
        }

        // 8秒后快递包裹应该都配送完了（具体延迟时间可以调整至都配送完，还有5人=2核心+3临时）
        Thread.sleep(8000L);
        System.out.println("快递网点：快递包裹应该差不多配送完了");
        System.out.println("派件快递小哥人数为：" + threadPoolExecutor.getPoolSize());
        System.out.println("网点仓库包裹数量为：" + threadPoolExecutor.getQueue().size());

        // 没活干5秒的临时快递小哥要被辞退了（空闲5秒就要被辞退，就剩核心2人）
        Thread.sleep(5000L);
        System.out.println("快递网点：没活干5秒的临时快递小哥要被辞退");
        System.out.println("派件快递小哥人数为：" + threadPoolExecutor.getPoolSize());
        System.out.println("网点仓库包裹数量为：" + threadPoolExecutor.getQueue().size());

        threadPoolExecutor.shutdown(); // 快递网点停止业务，但现有快递包裹还会配送
        //threadPoolExecutor.shutdownNow(); // 快递网点停止业务，现有快递包裹也不会配送
        System.out.println("快递网点：停止业务");
        Thread.sleep(1000L);
        System.out.println("派件快递小哥人数为：" + threadPoolExecutor.getPoolSize());
        System.out.println("网点仓库包裹数量为：" + threadPoolExecutor.getQueue().size());
        // 再次提交提示失败
        System.out.println("快递包裹" + (num + 1) + "：到达快递网点");
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("又来一个快递包裹");
            }
        });

        // 结果分析
        // 1、 2个任务（快递包裹）直接分配线程（快递小哥）开始执行（配送）
        // 2、 3个任务（快递包裹）进入等待队列（快递网点仓库）
        // 3、 队列（仓库）不够用，临时加开3个线程（快递小哥）来执行任务(5秒没活干就辞退)
        // 4、 队列（仓库）和线程池（快递小哥）都满了，剩下2个任务（快递包裹），没资源了，被拒绝执行（拒收）。
        // 5、 等待现有任务执行结束（配送完），应当还有5个任务（5人=2核心+3临时）
        // 6、 5秒后，如果无任务可执行（没活干），销毁临时创建的3个线程(辞退临时快递小哥)
        // 7、 调用shutdown（停止业务），不接收新的任务（快递包裹）
        // 8、 在线程池关闭（停止业务）后追加的任务（快递包裹），无法再提交，会被拒绝执行（拒收）
    }

}
