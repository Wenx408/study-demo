package com.wenx.demo.study.multithread;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/17 19:15
 * @Modified By：
 */
public class PriorityQueueDemo {
    public static void main(String[] args) {
        PriorityQueue<PriorityMessage> priorityQueue = new PriorityQueue<>(new Comparator<PriorityMessage>() {
            @Override
            public int compare(PriorityMessage o1, PriorityMessage o2) {
                return o1.order < o2.order ? -1 : 1;
            }
        });

        int num = 5;
        PriorityMessage priorityMessage;
        for (int i = num; i > 0; i--) {
            priorityMessage = new PriorityMessage("title" + i, "content" + i, i);
            priorityQueue.offer(priorityMessage);
            System.out.println("装填 " + priorityMessage);
        }

        while (!priorityQueue.isEmpty()) {
            System.out.println("释放 " + priorityQueue.poll());
        }

    }
}

class PriorityMessage {

    String title;
    String content;
    int order;

    /**
     * 构造函数
     *
     * @param title   标题
     * @param content 内容
     * @param order   序号
     */
    public PriorityMessage(String title, String content, int order) {
        this.title = title;
        this.content = content;
        this.order = order;
    }

    @Override
    public String toString() {
        return title + " " + content + " " + order;
    }

}
