package com.wenx.demo.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

/**
 * Redis订阅
 *
 * @author Wenx
 * @date 2018/8/14
 */
public abstract class BaseSubscribe extends JedisPubSub {
    private static final Logger logger = LoggerFactory.getLogger(BaseSubscribe.class);

    public BaseSubscribe() {
    }

    @Override
    public void onMessage(String channel, String message) {
        // 收到消息会调用
        if (logger.isDebugEnabled()) {
            logger.debug("receive redis published message, channel:[{}], message:[{}].",
                    channel, message);
        }

        doMessageHandle(channel, message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        // 订阅了频道会调用
        if (logger.isDebugEnabled()) {
            logger.debug("subscribe redis channel success, channel:[{}], subscribedChannels:[{}].",
                    channel, subscribedChannels);
        }
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        // 取消订阅会调用
        if (logger.isDebugEnabled()) {
            logger.debug("unsubscribe redis channel, channel:[{}], subscribedChannels:[{}].",
                    channel, subscribedChannels);
        }
    }

    /**
     * Redis订阅消息处理
     *
     * @param channel 订阅的通道
     * @param message 收到的消息
     */
    public abstract void doMessageHandle(String channel, String message);

}
