package com.wenx.demo.study.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/18 21:02
 * @Modified By：
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        // 服务端在20480端口监听客户端请求的TCP连接
        ServerSocket server = new ServerSocket(20480);
        System.out.println("Socket服务端-启动");
        while (!server.isClosed()) {
            // 等待客户端的连接，如果没有获取连接
            Socket client = server.accept();
            System.out.println("接收到客户端连接：" + client);
            // 为每个客户端连接开启一个线程
            executorService.execute(() -> {
                try {
                    // 获取Socket的输入流，用来接收从客户端发送过来的数据
                    BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
                    // 获取Socket的输出流，用来向客户端发送数据
                    PrintStream out = new PrintStream(client.getOutputStream(), false, "UTF-8");
                    String msg;
                    // 接收从客户端发送过来的数据
                    while ((msg = buf.readLine()) != null) {
                        if (msg.length() == 0) {
                            out.println("");
                            break;

                            // 响应结果 200
                            //OutputStream outputStream = client.getOutputStream();
                            //outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
                            //outputStream.write("Content-Length: 11\r\n\r\n".getBytes());
                            //outputStream.write("Hello World".getBytes());
                            //outputStream.flush();
                        }
                        System.out.println(msg);
                        // 将接收到的字符串前面加上“已收到”，发送到对应的客户端
                        out.println("已收到：" + msg);
                    }
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        System.out.println("客户端连接关闭：" + client);
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        server.close();
        System.out.println("Socket服务端-关闭");
    }
}
