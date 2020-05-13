package com.wenx.demo.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Redis连接池
 *
 * @author Wenx
 * @date 2019/12/15
 */
@Configuration
public class JedisPool {
    private static final Logger logger = LoggerFactory.getLogger(JedisPool.class);

    private static final int RECONNECT_LIMIT = 20;

    @Autowired
    private Environment env;
    private String[] hosts;
    private String[] ports;
    private String[] passwords;
    private String[] databases;
    private boolean isSharded;
    private int maxTotal;
    private int maxIdle;
    private long maxWaitMillis;
    private boolean testOnBorrow;

    /**
     * 单节点连接池
     */
    private static redis.clients.jedis.JedisPool jedisPool;
    /**
     * 分布式连接池
     */
    private static ShardedJedisPool shardedJedisPool;

    @Bean
    public void loadConfig() {
        String host = env.getProperty("redis.host");
        if (Objects.nonNull(host)) {
            hosts = host.split(",");
        }
        String port = env.getProperty("redis.port");
        if (Objects.nonNull(port)) {
            ports = port.split(",");
        }
        String password = env.getProperty("redis.password");
        if (Objects.nonNull(password)) {
            passwords = password.split(",");
        }
        String database = env.getProperty("redis.database");
        if (Objects.nonNull(database)) {
            databases = database.split(",");
        }
        isSharded = env.getProperty("redis.isSharded", Boolean.class);
        maxTotal = env.getProperty("redis.maxTotal", Integer.class);
        maxIdle = env.getProperty("redis.maxIdle", Integer.class);
        maxWaitMillis = env.getProperty("redis.maxWaitMillis", Long.class);
        testOnBorrow = env.getProperty("redis.testOnBorrow", Boolean.class);
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(testOnBorrow);

        return config;
    }

    @Bean
    public String initial(@Qualifier("jedisPoolConfig") JedisPoolConfig config) {
        if (Objects.nonNull(hosts) && hosts.length > 0) {
            if (isSharded) {
                initShardedPool(config);
            } else {
                initPool(config);
            }
        } else {
            logger.error("initial host invalid.");
        }
        return null;
    }

    /**
     * 初始化单节点池
     *
     * @param config JedisPoolConfig
     */
    private void initPool(JedisPoolConfig config) {
        logger.info("one redis are:------------------------------{");
        if (logger.isDebugEnabled()) {
            logger.debug("host:[{}], port:[{}].", hosts[0], ports[0]);
        }

        jedisPool = new redis.clients.jedis.JedisPool(config,
                hosts[0], Integer.parseInt(ports[0]), 2000,
                Objects.isNull(passwords) ? null : passwords[0],
                Objects.isNull(databases) ? 0 : Integer.parseInt(databases[0]));

        logger.info("}------------------------------");
    }

    /**
     * 初始化分布式池
     *
     * @param config JedisPoolConfig
     */
    private void initShardedPool(JedisPoolConfig config) {
        logger.info("shard redis are:------------------------------{");

        // slave连接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        for (int i = 0; i < hosts.length; i++) {
            if (logger.isDebugEnabled()) {
                logger.debug("host:[{}], port:[{}].", hosts[i], ports[i]);
            }
            JedisShardInfo jedisShardInfo = new JedisShardInfo(
                    hosts[i], Integer.parseInt(ports[i]), "master");
            jedisShardInfo.setPassword(Objects.isNull(passwords) ? null : passwords[0]);
            shards.add(jedisShardInfo);
        }
        shardedJedisPool = new ShardedJedisPool(config, shards);

        logger.info("}------------------------------");
    }

    /**
     * 获取连接
     *
     * @return Jedis
     */
    public static Jedis getJedis() {
        int count = RECONNECT_LIMIT;
        while ((count--) > 0) {
            try {
                return jedisPool.getResource();
            } catch (JedisConnectionException e) {
                logger.warn("jedis connection failed, try reconnecting.");
                try {
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException ex) {
                    logger.error("jedis reconnecting sleep failed.", e);
                }
            } catch (Exception e) {
                logger.error("get jedis failed, jedisInfo ... NumActive=[{}], NumIdle=[{}], NumWaiters=[{}], isClosed=[{}], error:",
                        jedisPool.getNumActive(), jedisPool.getNumIdle(),
                        jedisPool.getNumWaiters(), jedisPool.isClosed(), e);
                break;
            }
        }

        return null;
    }

    /**
     * 获取连接
     *
     * @return ShardedJedis
     */
    public static ShardedJedis getShardedJedis() {
        if (Objects.nonNull(shardedJedisPool)) {
            return shardedJedisPool.getResource();
        }
        return null;
    }

    /**
     * 关闭连接
     *
     * @param jedis Jedis
     */
    public static void close(Jedis jedis) {
        try {
            if (Objects.nonNull(jedis)) {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error("close failed.", e);
        }
    }

    /**
     * 关闭连接
     *
     * @param shardedJedis ShardedJedis
     */
    public static void close(ShardedJedis shardedJedis) {
        try {
            if (Objects.nonNull(shardedJedis)) {
                shardedJedis.close();
            }
        } catch (Exception e) {
            logger.error("sharded close failed.", e);
        }
    }

    /**
     * 销毁单节点池
     */
    public static void destroy() {
        try {
            jedisPool.destroy();
        } catch (Exception e) {
            logger.error("destroy failed.", e);
        }
    }

    /**
     * 销毁分布式池
     */
    public static void destroySharded() {
        try {
            shardedJedisPool.destroy();
        } catch (Exception e) {
            logger.error("sharded destroy failed.", e);
        }
    }

}
