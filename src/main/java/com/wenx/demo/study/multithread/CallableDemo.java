package com.wenx.demo.study.multithread;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/18 9:59
 * @Modified By：
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
