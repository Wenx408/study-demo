package com.wenx.demo.network;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Wenx
 * @date 2019/11/30
 */
public class WebSocketSession {
    /**
     * 会话连接池
     */
    static ConcurrentHashMap<String, Channel> sessionMap = new ConcurrentHashMap<>();

    /**
     * 保存会话
     *
     * @param sessionID
     * @param channel
     */
    public static void saveSession(String sessionID, Channel channel) {
        sessionMap.put(sessionID, channel);
    }

    /**
     * 移除会话
     *
     * @param sessionID
     */
    public static void removeSession(String sessionID) {
        sessionMap.remove(sessionID);
    }

    /**
     * 推送消息
     * @param msg
     */
    public static void pushMsg(String msg) {
        try {
            if (sessionMap.isEmpty()) {
                return;
            }

            int size = sessionMap.size();
            ConcurrentHashMap.KeySetView<String, Channel> keySetView = sessionMap.keySet();
            String[] keys = keySetView.toArray(new String[]{});
            System.out.println(WebSocketServerHandler.counter.sum() + " : 当前用户数量" + keys.length);

            for (int i = 0; i < size; i++) {
                // 提交任务给它执行
                String key = keys[new Random().nextInt(size)];
                Channel channel = sessionMap.get(key);
                if (channel == null) {
                    continue;
                }
                if (!channel.isActive()) {
                    sessionMap.remove(key);
                    continue;
                }
                channel.eventLoop().execute(() -> {
                    System.out.println("推送：" + msg);
                    channel.writeAndFlush(new TextWebSocketFrame(msg));
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
