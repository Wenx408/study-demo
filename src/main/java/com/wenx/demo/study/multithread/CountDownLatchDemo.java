package com.wenx.demo.study.multithread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/18 11:17
 * @Modified By：
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch count = new CountDownLatch(3);
        Map<String, String> result = new HashMap<>();

        long time = System.currentTimeMillis();
        executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
                result.put("result1", "任务1 接口执行完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                count.countDown();
            }
        });
        executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5L);
                result.put("result2", "任务2 接口执行完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                count.countDown();
            }
        });
        executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2L);
                result.put("result3", "任务3 接口执行完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                count.countDown();
            }
        });

        count.await(); // 等待计数器归零

        System.out.println(result);
        System.out.println("执行时间为：" + (System.currentTimeMillis() - time));
        executorService.shutdown();
    }
}
