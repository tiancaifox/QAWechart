package com.nutcracker.wedo.common.framework.cache.redis;

import java.util.List;

/**
 * Created by huh on 2017/3/27.
 */
public interface RedisCallBack<T> {
    boolean operation(List<RedisClient> var1, boolean var2, Object var3, RedisClientStatusNotifier var4);

    String getOptionType();

    T getResult();

    Exception getException();

    boolean isNullValueReGet();

    void setNullValueReGet(boolean var1);
}
