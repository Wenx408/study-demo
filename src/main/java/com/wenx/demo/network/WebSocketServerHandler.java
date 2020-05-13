package com.wenx.demo.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author Wenx
 * @date 2019/11/30
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final String WEBSOCKET_PATH = "/websocket";
    private WebSocketServerHandshaker handshaker;

    public static final LongAdder counter = new LongAdder();

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        counter.add(1);
        if (msg instanceof FullHttpRequest) {
            System.out.println("Http请求");
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            System.out.println("WebSocket请求");
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 如果http解码失败，则返回http异常，并且判断消息头有没有包含Upgrade字段(协议升级)
        if (!req.decoderResult().isSuccess() || req.method() != GET || (!"websocket".equals(req.headers().get("Upgrade")))) {
            System.out.println("Http异常");
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }

        // 构造握手响应返回
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                getWebSocketLocation(req), null, true, 5 * 1024 * 1024);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            // 版本不支持
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            System.out.println("WebSocket握手响应");
            handshaker.handshake(ctx.channel(), req);

            // 解析请求，判断token
            Map<String, List<String>> parameters = new QueryStringDecoder(req.uri()).parameters();
            // 连接限制，需要验证token，拒绝陌生连接
            String token = parameters.get("token").get(0);
            // 保存token
            ctx.channel().attr(AttributeKey.valueOf("token")).getAndSet(token);
            // 保存会话
            WebSocketSession.saveSession(token, ctx.channel());

            // 结束
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 关闭
        if (frame instanceof CloseWebSocketFrame) {
            Object token = ctx.channel().attr(AttributeKey.valueOf("token")).get();
            WebSocketSession.removeSession(token.toString());
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // ping/pong作为心跳
        if (frame instanceof PingWebSocketFrame) {
            System.out.println("ping: " + frame);
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 文本
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            String outStr = text
                    + ", 欢迎使用Netty WebSocket服务， 现在时刻:"
                    + new java.util.Date().toString();
            System.out.println("收到：" + text);
            System.out.println("发送：" + outStr);
            //发送到客户端websocket
            ctx.channel().write(new TextWebSocketFrame(outStr));
            return;
        }
        // 不处理二进制消息
        if (frame instanceof BinaryWebSocketFrame) {
            // Echo the frame
            ctx.write(frame.retain());
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HttpHeaderNames.HOST) + WEBSOCKET_PATH;
        return "ws://" + location;
    }
}
