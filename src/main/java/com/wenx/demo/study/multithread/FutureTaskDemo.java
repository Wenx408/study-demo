package com.wenx.demo.study.multithread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/18 10:32
 * @Modified By：
 */
public class FutureTaskDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 实际业务场景中会有一个方法 调用多次接口 / 查询多个数据库表 / 多个sql
        // 如果顺序执行的话执行时间是累加的，但是要是使用FutureTask执行的话呢？执行时间是最慢的那个，这不效率就提升了~
        // 下面举个栗子

        // 这里可以用我们之前写的MyFutureTask，效果是一样的
        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            TimeUnit.SECONDS.sleep(3L);
            return "任务1 接口执行完毕";
        });
        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            TimeUnit.SECONDS.sleep(5L);
            return "任务2 接口执行完毕";
        });
        FutureTask<String> futureTask3 = new FutureTask<>(() -> {
            TimeUnit.SECONDS.sleep(2L);
            return "任务3 接口执行完毕";
        });

        long time = System.currentTimeMillis();
        new Thread(futureTask1).start();
        new Thread(futureTask2).start();
        new Thread(futureTask3).start();

        Map<String, String> result = new HashMap<>();
        result.put("result1", futureTask1.get());
        result.put("result2", futureTask2.get());
        result.put("result3", futureTask3.get());

        System.out.println(result);
        System.out.println("执行时间为：" + (System.currentTimeMillis() - time));
    }
}
