package com.wenx.demo.study.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/19 13:26
 * @Modified By：
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建Socket服务端
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false); // 设置为非阻塞模式
        // 2. 构建一个Selector选择器，并且将ServerSocketChannel的accept事件注册绑定到selector选择器上
        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
        // 3. 绑定端口
        server.socket().bind(new InetSocketAddress(20480)); // 绑定端口
        System.out.println("Socket服务端-启动");

        ByteBuffer readBuff = ByteBuffer.allocate(1024);
        ByteBuffer writeBuff;

        while (true) {
            // 不再轮询通道，改用下面轮询事件的方式。select方法有阻塞效果，直到有事件通知才会有返回
            selector.select();
            // 获取事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 遍历查询结果e
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                // 被封装的查询结果
                SelectionKey key = iterator.next();
                // 关注 Accept 和 Read 两个事件
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    // 拿到客户端的连接通道，并且将SocketChannel的read事件注册绑定到selector选择器上
                    SocketChannel client = channel.accept(); // mainReactor 轮询accept
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("接收到客户端连接：" + client.getRemoteAddress());
                } else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    try {
                        readBuff.clear();
                        while (client.isOpen() && client.read(readBuff) != -1) {
                            // 长连接情况下，需要手动判断数据有没有读取结束
                            // 此处做一个简单的判断：超过0字节就认为请求结束了
                            if (readBuff.position() > 0) break;
                        }
                        // 如果没数据了, 则不继续后面的处理
                        if (readBuff.position() == 0) continue;

                        readBuff.flip();
                        byte[] content = new byte[readBuff.limit()];
                        readBuff.get(content);
                        System.out.println("已收到：" + new String(content));
                        System.out.println("来自：" + client.getRemoteAddress());

                        // TODO 业务操作 数据库 接口调用等等

                        // 响应结果 200
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Length: 11\r\n\r\n" +
                                "Hello World";
                        writeBuff = ByteBuffer.wrap(response.getBytes());
                        while (writeBuff.hasRemaining()) {
                            client.write(writeBuff);
                        }

                    } catch (IOException e) {
                        // e.printStackTrace();
                        key.cancel(); // 取消事件订阅
                    }
                }
                iterator.remove();
            }
            selector.selectNow();
        }
        //channel.close();
        //System.out.println("Socket服务端-关闭");
    }
}
