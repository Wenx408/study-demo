package com.wenx.demo.study.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/18 21:31
 * @Modified By：
 */
public class BIOClient {
    public static void main(String[] args) throws IOException {
        // 客户端请求与本机在20480端口建立TCP连接
        Socket client = new Socket("127.0.0.1", 20480);
        client.setSoTimeout(600000);
        System.out.println("Socket客户端-启动");
        // 获取Socket的输入流，用来接收从服务端发送过来的数据
        BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
        // 获取Socket的输出流，用来发送数据到服务端
        PrintStream out = new PrintStream(client.getOutputStream(), false, "UTF-8");
        // 获取键盘输入
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

        new Thread(() -> {
            String msg;
            // 接收从键盘发送过来的数据
            try {
                System.out.print("请输入信息：");
                while ((msg = input.readLine()) != null) {
                    out.println(msg);
                    if (msg.length() == 0) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            String echo;
            // 从服务器端接收数据有个时间限制（系统自设，也可以自己设置），超过了这个时间，便会抛出该异常
            while ((echo = buf.readLine()) != null) {
                if (echo.length() == 0) {
                    System.out.println("服务端连接关闭");
                    break;
                }
                System.out.println(echo);
                System.out.print("请输入信息：");
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Time out, No response");
        }

        input.close();
        if (client != null) {
            // 如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
            client.close(); // 只关闭socket，其关联的输入输出流也会被关闭
            System.out.println("Socket客户端-关闭");
        }
    }
}
