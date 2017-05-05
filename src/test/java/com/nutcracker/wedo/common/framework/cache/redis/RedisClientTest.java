package com.nutcracker.wedo.common.framework.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import utils.AbstractTestCase;

import javax.annotation.Resource;

/**
 * Created by huh on 2017/4/14.
 */
@Slf4j
public class RedisClientTest extends AbstractTestCase {

    @Resource(name = "myCacheManager")
    private HaRedisCacheManager myCacheManager;

    @Test
    public void findTest() {
        myCacheManager.put("hello", 300,"world");
        String temp=(String)myCacheManager.get("hello");
        log.info(temp);
    }


}
