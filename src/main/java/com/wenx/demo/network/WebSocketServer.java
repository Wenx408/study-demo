package com.wenx.demo.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Wenx
 * @date 2019/11/30
 */
public class WebSocketServer {
    static int port = 20480;

    public static void main(String[] args) throws Exception {
        EventLoopGroup mainGroup = new NioEventLoopGroup(1);
        EventLoopGroup subGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(mainGroup, subGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(new WebSocketServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_REUSEADDR, true);

            ChannelFuture f = b.bind(port).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("端口绑定完成：" + future.channel().localAddress());
                }
            }).sync();

            // 获取键盘输入
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            new Thread(() -> {
                String msg;
                // 接收从键盘发送过来的数据
                try {
                    System.out.print("请输入信息：");
                    while ((msg = input.readLine()) != null) {
                        WebSocketSession.pushMsg(msg);
                        if (msg.length() == 0) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            f.channel().closeFuture().sync();
        } finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
