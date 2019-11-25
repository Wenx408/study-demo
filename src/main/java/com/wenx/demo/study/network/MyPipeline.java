package com.wenx.demo.study.network;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/23 11:38
 * @Modified By：
 */
public class MyPipeline {
    public static void main(String[] args) {
        LinkedHandlerContext handlerContext = new LinkedHandlerContext();
        handlerContext.addLast(new Handler1());
        handlerContext.addLast(new Handler2());
        handlerContext.addLast(new Handler1());
        handlerContext.addLast(new Handler2());

        // 发起请求
        handlerContext.process(0);
    }
}

/**
 * 链表式处理器上下文，主要负责链表的维护，链表的执行
 */
class LinkedHandlerContext {
    /**
     * 链表头节点
     */
    AbstractLinkedHandler head;

    /**
     * 添加处理器至末尾
     *
     * @param handler 处理器
     */
    public void addLast(AbstractLinkedHandler handler) {
        if (head == null) {
            head = handler;
            return;
        }

        AbstractLinkedHandler context = head;
        while (context.next != null) {
            context = context.next;
        }
        context.next = handler;
    }

    /**
     * 执行处理过程
     *
     * @param request 请求参数
     */
    public void process(Object request) {
        head.handle(this, request);
    }

    /**
     * 执行下一个处理方法
     */
    public void processNext(AbstractLinkedHandler handler, Object param) {
        if (handler.next != null) {
            handler.next.handle(this, param);
        }
    }
}

/**
 * 链表式处理器抽象类
 */
abstract class AbstractLinkedHandler {
    /**
     * 下一个节点
     */
    AbstractLinkedHandler next;

    /**
     * 处理方法
     *
     * @param context 处理器上下文
     * @param param   请求参数
     */
    abstract void handle(LinkedHandlerContext context, Object param);
}

/**
 * 处理器具体实现类
 */
class Handler1 extends AbstractLinkedHandler {
    @Override
    void handle(LinkedHandlerContext context, Object param) {
        param = (Integer) param + 10;
        System.out.println("打败野猪获得10经验，当前经验值：" + param);
        context.processNext(this, param);
    }
}

/**
 * 处理器具体实现类
 */
class Handler2 extends AbstractLinkedHandler {
    @Override
    void handle(LinkedHandlerContext context, Object param) {
        param = (Integer) param + 100;
        System.out.println("打败恶龙获得100经验，当前经验值：" + param);
        context.processNext(this, param);
    }
}
