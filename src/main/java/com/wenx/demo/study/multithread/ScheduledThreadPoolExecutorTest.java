package com.wenx.demo.study.multithread;

import java.text.SimpleDateFormat;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/11 21:47
 * @Modified By：
 */
public class ScheduledThreadPoolExecutorTest {
    public static void main(String[] args) throws InterruptedException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(5);

        // 定时3秒后执行一次任务
        threadPoolExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("定时任务1 开始执行，当前时间为：" + dateFormat.format(System.currentTimeMillis()));
            }
        }, 3000, TimeUnit.MILLISECONDS);
        System.out.println("定时任务1 提交成功，当前时间为：" + dateFormat.format(System.currentTimeMillis()));
        System.out.println("当前线程池中线程数量：" + threadPoolExecutor.getPoolSize());

        // 周期性执行任务
        // scheduleAtFixedRate：2秒后执行第一次任务，之后每间隔1秒执行一次（如果上次执行还未结束，则等待结束后立刻执行）
        threadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("定时任务2 开始执行，当前时间为：" + dateFormat.format(System.currentTimeMillis()));
            }
        }, 2000, 1000, TimeUnit.MILLISECONDS);
        System.out.println("定时任务2 提交成功，当前时间为：" + dateFormat.format(System.currentTimeMillis()));
        System.out.println("当前线程池中线程数量：" + threadPoolExecutor.getPoolSize());

        // scheduleWithFixedDelay：2秒后执行第一次任务，之后每间隔1秒执行一次（如果上次执行还未结束，则等待结束后，再等待1秒执行）
        threadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("定时任务3 开始执行，当前时间为：" + dateFormat.format(System.currentTimeMillis()));
            }
        }, 2000, 1000, TimeUnit.MILLISECONDS);
        System.out.println("定时任务3 提交成功，当前时间为：" + dateFormat.format(System.currentTimeMillis()));
        System.out.println("当前线程池中线程数量：" + threadPoolExecutor.getPoolSize());
    }

}
