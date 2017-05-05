package com.nutcracker.wedo.common.framework.cache;

/**
 * Created by huh on 2017/3/27.
 */
public interface CacheManager {
    String put(Object var1, Object var2);

    String put(Object var1, Integer var2, Object var3);

    Object get(Object var1);

    String remove(Object var1);

    String replace(Object var1, Object var2);

    String replace(Object var1, Integer var2, Object var3);

    boolean existsKey(String var1);

    boolean extendTime(String var1, Integer var2);

    void shutdown();
}
