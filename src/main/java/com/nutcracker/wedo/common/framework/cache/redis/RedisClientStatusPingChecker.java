package com.nutcracker.wedo.common.framework.cache.redis;

import org.apache.log4j.Logger;

/**
 * Created by huh on 2017/3/31.
 */
public class RedisClientStatusPingChecker implements RedisClientStatusChecker {
    private static final Logger LOGGER = Logger.getLogger(RedisClientStatusPingChecker.class);

    public RedisClientStatusPingChecker() {
    }

    public boolean checkStatus(RedisClient redisClient) {
        if(redisClient == null) {
            return false;
        } else {
            try {
                redisClient.ping();
                return true;
            } catch (Exception var3) {
                LOGGER.error("check status by ping from client failed.", var3);
                return false;
            }
        }
    }
}

