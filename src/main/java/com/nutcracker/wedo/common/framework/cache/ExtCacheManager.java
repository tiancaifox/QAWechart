package com.nutcracker.wedo.common.framework.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by huh on 2017/3/27.
 */
public interface ExtCacheManager extends CacheManager {
    void hput(String var1, String var2, Serializable var3);

    Object hget(String var1, String var2);

    boolean hdel(String var1, String var2);

    Set<String> hKeys(String var1);

    List<Object> hValues(String var1);

    boolean hExists(String var1, String var2);

    long hLen(String var1);

    void hmSet(String var1, Map<String, Serializable> var2);

    List<Object> hmGet(String var1, String... var2);

    Map<String, Object> hGetAll(String var1);
}

