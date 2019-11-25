package com.wenx.demo.study.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/25 13:46
 * @Modified By：
 */
public class MyNettyServer {
    public static void main(String[] args) {
        // 创建EventLoopGroup   accept线程组
        MyEventLoopGroup mainGroup = new MyEventLoopGroup(1);
        // 创建EventLoopGroup   I/O线程组
        MyEventLoopGroup subGroup = new MyEventLoopGroup(2);
        try {
            // 服务端启动引导工具类
            MyServerBootstrap b = new MyServerBootstrap();
            // 配置服务端处理的reactor线程组以及服务端的其他配置
            b.group(mainGroup, subGroup).channel(ServerSocketChannel.class)
                    .option("SO_BACKLOG", 100)
                    .handler("new LoggingHandler()")
                    .childHandler("new EchoServerHandler()");
            // 通过bind启动服务
            FutureTask f = b.bind(20480);
            // 阻塞主线程，直到网络服务被关闭
            f.get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭线程组
            mainGroup.shutdown();
            subGroup.shutdown();
        }
    }
}

class MyServerBootstrap {
    /**
     * main线程组
     */
    private volatile MyEventLoopGroup group;
    /**
     * sub线程组
     */
    private volatile MyEventLoopGroup childGroup;

    /**
     * 绑定线程组，单reactor模式
     *
     * @param group main/sub线程组
     * @return
     */
    public MyServerBootstrap group(MyEventLoopGroup group) {
        return group(group, group);
    }

    /**
     * 绑定线程组，多路reactor模式
     *
     * @param parentGroup main线程组
     * @param childGroup  sub线程组
     * @return
     */
    public MyServerBootstrap group(MyEventLoopGroup parentGroup, MyEventLoopGroup childGroup) {
        if (parentGroup == null) {
            throw new NullPointerException("group");
        }
        if (group != null) {
            throw new IllegalStateException("group set already");
        }
        group = parentGroup;

        if (childGroup == null) {
            throw new NullPointerException("childGroup");
        }
        if (this.childGroup != null) {
            throw new IllegalStateException("childGroup set already");
        }
        this.childGroup = childGroup;

        return this;
    }

    /**
     * 配置channel工厂
     *
     * @param channelClass channel工厂类
     * @return
     */
    public MyServerBootstrap channel(Class<?> channelClass) {
        // 伪代码，这里为channelFactory赋值，方便后续初始化channel使用
        // 因为我们是NIO TCP，后续就直接使用ServerSocketChannel，SocketChannel

        return this;
    }

    /**
     * 配置参数
     *
     * @param option 参数KEY
     * @param value  参数值
     * @return
     */
    public MyServerBootstrap option(Object option, Object value) {
        // 伪代码，这里为往options添加option，方便后续配置使用

        return this;
    }

    /**
     * main线程组处理器
     *
     * @param handler 处理器
     * @return
     */
    public MyServerBootstrap handler(Object handler) {
        // 伪代码，这里将handler添加进groupHandler，给后续pipeline使用
        // 我们后面就模拟日志Handler，直接打印accept时客户端信息

        return this;
    }

    /**
     * sub线程组处理器
     *
     * @param childHandler 处理器
     * @return
     */
    public MyServerBootstrap childHandler(Object childHandler) {
        // 伪代码，这里将handler添加进childHandler，给后续pipeline使用
        // 我们后面就模拟EchoHandler，直接打印客户端发送的数据及将数据处理返回

        return this;
    }

    /**
     * 绑定端口开启服务
     *
     * @param inetPort
     * @return
     */
    public FutureTask bind(int inetPort) {
        InetSocketAddress localAddress = new InetSocketAddress(inetPort);
        if (localAddress == null) {
            throw new NullPointerException("localAddress");
        }

        try {
            // 1. 初始化和注册
            ServerSocketChannel channel = initAndRegister();
            // 2. 绑定端口开始服务
            channel.bind(localAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FutureTask futureTask = new FutureTask(() -> null);
        return futureTask;
    }

    /**
     * 初始化及注册
     *
     * @return Channel
     * @throws Exception
     */
    final ServerSocketChannel initAndRegister() throws Exception {
        // 1. 初始化channel，通过channelFactory来newChannel，这里直接创建ServerSocketChannel
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        // 2. 初始化pipeline责任链
        // 伪代码……
        // group载入handler，处理accept，模拟日志Handler打印客户端信息
        // childGroup载入childHandler，处理I/O，模拟EchoHandler打印客户端发送的数据及将数据处理返回

        // 3. 将serverSocketChannel注册绑定到selector
        // Netty把Channel封装增强带许多参数，我们直接传this就不封装了，方便理解
        SelectionKey selectionKey = group.register(channel, this);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);

        return channel;
    }

    /**
     * 本应把Channel封装增强带许多参数，我们就不封装了，方便理解
     *
     * @param key
     * @throws Exception
     */
    public void channelHandler(SelectionKey key) throws Exception {
        SelectableChannel channel = key.channel();
        if (channel instanceof ServerSocketChannel) {
            acceptHandler((ServerSocketChannel) key.channel());
        } else if (channel instanceof SocketChannel) {
            ioHandler((SocketChannel) key.channel());
        }
    }

    /**
     * mainReactor 处理accept
     *
     * @param server 服务端Channel
     * @throws Exception
     */
    public void acceptHandler(ServerSocketChannel server) throws Exception {
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        // 收到接收到客户端连接后，将SocketChannel的read事件注册绑定到selector选择器上，分发给I/O线程继续去读取数据
        SelectionKey selectionKey = childGroup.register(client, this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        System.out.println(Thread.currentThread().getName() + "接收连接 : " + client.getRemoteAddress());
    }

    /**
     * subReactor 处理I/O
     *
     * @param client 客户端Channel
     * @throws Exception
     */
    public void ioHandler(SocketChannel client) throws Exception {
        ByteBuffer readBuff = ByteBuffer.allocate(1024);
        while (client.isOpen() && client.read(readBuff) != -1) {
            // 长连接情况下，需要手动判断数据有没有读取结束
            // 此处做一个简单的判断：超过0字节就认为请求结束了
            if (readBuff.position() > 0) {
                break;
            }
        }
        // 如果没数据了, 则不继续后面的处理
        if (readBuff.position() == 0) {
            return;
        }
        readBuff.flip();
        byte[] content = new byte[readBuff.limit()];
        readBuff.get(content);
        String response = "已收到：" + new String(content);
        System.out.println(response);
        System.out.println("来自：" + client.getRemoteAddress());

        // TODO 业务操作 数据库 接口调用等等

        ByteBuffer writeBuff = ByteBuffer.wrap(response.getBytes());
        while (writeBuff.hasRemaining()) {
            client.write(writeBuff);
        }
    }
}

class MyEventLoopGroup {
    /**
     * 默认EventLoop线程数量：cpus*2
     */
    private static final int DEFAULT_EVENT_LOOP_THREADS = Runtime.getRuntime().availableProcessors() * 2;
    /**
     * EventLoop集合
     */
    private final MyEventLoop[] children;
    /**
     * Chooser索引
     */
    private final AtomicInteger idx = new AtomicInteger();

    /**
     * 构造函数
     */
    public MyEventLoopGroup() {
        this(0);
    }

    /**
     * 构造函数
     *
     * @param nThreads EventLoop线程数量
     */
    public MyEventLoopGroup(int nThreads) {
        if (nThreads <= 0) {
            nThreads = DEFAULT_EVENT_LOOP_THREADS;
        }

        children = new MyEventLoop[nThreads];
        for (int i = 0; i < nThreads; i++) {
            try {
                children[i] = newChild();
            } catch (Exception e) {
                throw new IllegalStateException("failed to create a child event loop", e);
            }
        }
    }

    /**
     * 创建EventLoop线程
     *
     * @return EventLoop
     * @throws Exception
     */
    public MyEventLoop newChild() throws Exception {
        return new MyEventLoop();
    }

    /**
     * 选择一个EventLoop线程
     *
     * @return EventLoop
     */
    public MyEventLoop next() {
        // Chooser
        int index = Math.abs(idx.getAndIncrement() % children.length);
        return children[index];
    }

    /**
     * 将Channel注册绑定到EventLoop的Selector上
     *
     * @param channel
     * @return
     * @throws Exception
     */
    public SelectionKey register(SelectableChannel channel, MyServerBootstrap b) throws Exception {
        return next().register(channel, b);
    }

    /**
     * 关闭EventLoopGroup
     */
    public void shutdown() {
        for (MyEventLoop el : children) {
            el.shutdown();
        }
    }
}

class MyEventLoop implements Executor {
    /**
     * 选择器
     */
    private Selector selector;
    /**
     * 任务队列
     */
    private final Queue<Runnable> taskQueue;
    /**
     * 任务线程
     */
    private volatile Thread thread;
    /**
     * 线程运行状态
     */
    private volatile boolean running = false;

    /**
     * 构造函数
     *
     * @throws IOException
     */
    public MyEventLoop() throws IOException {
        // 初始化选择器及任务队列
        selector = Selector.open();
        taskQueue = new LinkedBlockingQueue<>();
    }

    /**
     * 将任务加入任务队列等待线程执行
     *
     * @param task 待执行的任务
     */
    @Override
    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }

        taskQueue.offer(task);
        if (!running || thread == null) {
            startThread();
        }
    }

    /**
     * 开启任务线程
     */
    private void startThread() {
        if (!running) {
            running = true;
            if (thread == null) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (running) {
                            try {
                                processSelectedKeys();
                            } finally {
                                runAllTasks();
                            }
                        }
                    }
                });
            }
            thread.start();
            System.out.println("启动：" + thread.getName());
            System.out.println("来自：" + Thread.currentThread().getName());
        }
    }

    /**
     * 处理SelectedKeys
     */
    private void processSelectedKeys() {
        try {
            selector.select(1000);
            // 获取查询结果
            Set<SelectionKey> selected = selector.selectedKeys();
            // 遍历查询结果
            Iterator<SelectionKey> iterator = selected.iterator();
            while (iterator.hasNext()) {
                // 被封装的查询结果
                SelectionKey key = iterator.next();
                iterator.remove();
                int readyOps = key.readyOps();
                // 关注 Read 和 Accept两个事件
                if ((readyOps & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT)) != 0 || readyOps == 0) {
                    try {
                        ((MyServerBootstrap) key.attachment()).channelHandler(key);
                        if (!key.channel().isOpen()) {
                            key.cancel(); // 如果关闭了，就取消这个KEY的订阅
                        }
                    } catch (Exception ex) {
                        key.cancel(); // 如果有异常，就取消这个KEY的订阅
                    }
                }
            }
            selector.selectNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行全部任务
     */
    private void runAllTasks() {
        // 执行队列中的任务
        Runnable task;
        while ((task = taskQueue.poll()) != null) {
            task.run();
        }
    }

    public SelectionKey register(SelectableChannel channel, MyServerBootstrap b) throws Exception {
        // register以任务形式提交，确保selector与正在selector.select()线程不会发生争抢
        FutureTask<SelectionKey> f = new FutureTask<>(() -> channel.register(selector, 0, b));
        execute(f);
        System.out.println("register：" + channel.getClass().getName());
        return f.get();
    }

    /**
     * 关闭EventLoop
     */
    public void shutdown() {
        try {
            running = false;
            if (thread != null) {
                thread.interrupt();
            }
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
