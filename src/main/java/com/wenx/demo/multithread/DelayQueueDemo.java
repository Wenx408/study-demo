package com.wenx.demo.multithread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author Wenx
 * @date 2019/11/17
 */
public class DelayQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        DelayQueue<DelayedMessage> delayQueue = new DelayQueue<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        long now = System.currentTimeMillis();
        System.out.println("起始时间：" + dateFormat.format(now));

        // 2秒后发送
        DelayedMessage msg1 = new DelayedMessage("title1", "content1", new Date(now + 2000L));
        delayQueue.add(msg1);
        // 5秒后发送
        DelayedMessage msg2 = new DelayedMessage("title2", "content2", new Date(now + 5000L));
        delayQueue.add(msg2);

        while (!delayQueue.isEmpty()) {
            System.out.println(delayQueue.take());
            TimeUnit.SECONDS.sleep(1L);
        }
    }
}

class DelayedMessage implements Delayed {

    String title;
    String content;
    Date sendTime;

    /**
     * 构造函数
     *
     * @param title    标题
     * @param content  内容
     * @param sendTime 发送时间
     */
    public DelayedMessage(String title, String content, Date sendTime) {
        this.title = title;
        this.content = content;
        this.sendTime = sendTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long duration = sendTime.getTime() - System.currentTimeMillis();
        return unit.convert(duration, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return o.getDelay(TimeUnit.NANOSECONDS) < this.getDelay(TimeUnit.NANOSECONDS) ? 1 : -1;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    @Override
    public String toString() {
        return title + " " + content + " " + dateFormat.format(sendTime);
    }

}
