package com.wenx.demo.study.network;

import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2019/11/30 15:51
 * @Modified By：
 */
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    private static final String WEBSOCKET_PATH = "/websocket";
    private WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    static AtomicInteger counter = new AtomicInteger(0);

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (handshaker == null) {
            InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
            URI uri = null;
            try {
                uri = new URI("ws://" + address.getHostString() + ":" + address.getPort()
                        + WEBSOCKET_PATH + "?token=" + counter.incrementAndGet());
            } catch (Exception e) {
                e.printStackTrace();
            }
            handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                    uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        }
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端已断开");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                System.out.println("客户端已连接");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                System.out.println("客户端连接失败");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            System.out.println("已收到：" + textFrame.text());
        } else if (frame instanceof PongWebSocketFrame) {
            System.out.println("收到心跳");
        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("收到关闭");
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }
}
