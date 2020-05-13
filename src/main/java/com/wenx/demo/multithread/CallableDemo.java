package com.wenx.demo.multithread;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Wenx
 * @date 2019/11/18
 */
public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Random random = new Random();
                return "返回值为：" + random.nextInt(200);
            }
        };

        FutureTask<String> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();

        String result = futureTask.get(); // 阻塞，等待执行结果后继续
        System.out.println(result);
    }
}
