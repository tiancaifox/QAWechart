package com.nutcracker.wedo.common.framework.cache.redis;

/**
 * Created by huh on 2017/3/27.
 */
public interface RedisClientStatusNotifier {
    void onFaild(RedisClient var1);

    void onOk(RedisClient var1);
}
