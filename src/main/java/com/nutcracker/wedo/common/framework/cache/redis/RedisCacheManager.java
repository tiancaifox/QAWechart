package com.nutcracker.wedo.common.framework.cache.redis;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by huh on 2017/3/31.
 */
public class RedisCacheManager extends AbstractRedisCacheManager {
    private static final Logger LOGGER = Logger.getLogger(RedisCacheManager.class);

    public RedisCacheManager() {
    }

    public String toString() {
        int size = 0;
        if(CollectionUtils.isNotEmpty(this.getClientList())) {
            size = this.getClientList().size();
        }

        return "RedisCacheManager clientSize[" + size + "]";
    }

    protected List<RedisClient> getClients(Object key) {
        RedisClient client = this.getClient(key);
        if(client == null) {
            LOGGER.error("Redis Cache Manager get redis client is null.");
            return Collections.emptyList();
        } else {
            return Arrays.asList(new RedisClient[]{client});
        }
    }

    private RedisClient getClient(Object key) {
        if(CollectionUtils.isEmpty(this.getClientList())) {
            return null;
        } else {
            ArrayList copied = new ArrayList(this.getClientList());
            int index = Math.abs(key.toString().hashCode() % copied.size());
            return (RedisClient)copied.get(index);
        }
    }
}

