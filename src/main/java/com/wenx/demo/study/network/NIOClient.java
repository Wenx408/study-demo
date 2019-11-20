package com.wenx.demo.study.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/19 13:26
 * @Modified By：
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        // 客户端请求与本机在20480端口建立TCP连接
        SocketChannel client = SocketChannel.open();
        client.configureBlocking(false); // 设置为非阻塞模式
        client.connect(new InetSocketAddress("127.0.0.1", 20480));
        while (!client.finishConnect()) {
            // 没连接上，则一直等待
            Thread.yield();
        }
        System.out.println("Socket客户端-启动");

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        // 发送内容
        String msg = scanner.nextLine();
        ByteBuffer writeBuff = ByteBuffer.wrap(msg.getBytes());
        while (writeBuff.hasRemaining()) {
            client.write(writeBuff);
        }

        // 读取响应
        System.out.println("收到服务端响应：");
        ByteBuffer readBuff = ByteBuffer.allocate(1024);
        while (client.isOpen() && client.read(readBuff) != -1) {
            // 长连接情况下，需要手动判断数据有没有读取结束
            // 此处做一个简单的判断：超过0字节就认为请求结束了
            if (readBuff.position() > 0) break;
        }

        readBuff.flip();
        byte[] content = new byte[readBuff.limit()];
        readBuff.get(content);
        System.out.println("发送数据：" + new String(content));

        scanner.close();
        client.close();
    }
}
