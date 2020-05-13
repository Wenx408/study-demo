package com.wenx.demo.multithread;

import java.util.concurrent.*;

/**
 * @author Wenx
 * @date 2019/11/18
 */
public class ForkJoinDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 本质是一个线程池，默认的线程数量：CPU的核数
        // 默认情况下，并行线程数量等于可用处理器的数量
        // ForkJoinPool与其他类型的ExecutorService的区别主要在于它使用了工作窃取:
        // 池中的所有线程都试图查找和执行提交给池的任务和/或其他活动任务创建的任务
        // (如果不存在工作，则最终阻塞等待工作)。
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // 适合数据处理、结果汇总、统计等场景
        // 举个栗子计算1+2+3+…100W的结果，拆分多个线程进行计算，合并汇总返回
        // 查询多个接口的数据，合并返回
        // 其他例子，查数据库的多个表数据，分多次查询
        // fork/join
        // forkJoinPool.submit()

        // 计算1+2+3+…100W的结果，这里只是举个例子
        long time1 = System.currentTimeMillis();
        // 提交可分解的RecursiveTask任务
        ForkJoinTask<Long> forkJoinTask = forkJoinPool.submit(new SumTask(1, 1000000));
        System.out.println("结果为：" + forkJoinTask.get() + " 执行时间" + (System.currentTimeMillis() - time1));
        //forkJoinPool.shutdown();

        // 实际执行Fork、Join、从队列存取等操作都需要消耗时间，请在合适的场景使用
        long time2 = System.currentTimeMillis();
        long sum = 0L;
        for (int i = 1; i <= 1000000; i++) {
            sum += i;
        }
        System.out.println("结果为：" + sum + " 执行时间" + (System.currentTimeMillis() - time2));

        // 提交可分解的RecursiveAction任务
        forkJoinPool.submit(new PrintAction(1, 1000));
        //阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束
        forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);
        forkJoinPool.shutdown();
    }
}

class SumTask extends RecursiveTask<Long> {

    private static final int THRESHOLD = 1000;
    private int start; // 开始下标
    private int end; // 结束下标

    public SumTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        // 如果当前任务的计算量在阈值范围内，则直接进行计算
        if (end - start < THRESHOLD) {
            return computeByUnit();
        } else { // 如果当前任务的计算量超出阈值范围，则进行计算任务拆分
            // 计算中间索引
            int middle = (start + end) / 2;
            //定义子任务-迭代思想
            SumTask left = new SumTask(start, middle);
            SumTask right = new SumTask(middle + 1, end);
            //划分子任务-fork
            left.fork();
            right.fork();
            //合并计算结果
            return left.join() + right.join();
        }
    }

    private long computeByUnit() {
        long sum = 0L;
        for (int i = start; i <= end; i++) {
            sum += i;
        }
        return sum;
    }
}

class PrintAction extends RecursiveAction {

    private static final int THRESHOLD = 20;
    private int start; // 开始下标
    private int end; // 结束下标

    public PrintAction(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        //当end-start的值小于MAX时，开始打印
        if ((end - start) < THRESHOLD) {
            computeByUnit();
        } else {
            // 将大任务分解成两个小任务
            int middle = (start + end) / 2;
            PrintAction left = new PrintAction(start, middle);
            PrintAction right = new PrintAction(middle + 1, end);
            left.fork();
            right.fork();
        }
    }

    private void computeByUnit() {
        for (int i = start; i <= end; i++) {
            System.out.println(Thread.currentThread().getName() + "的值：" + i);
        }
    }
}
