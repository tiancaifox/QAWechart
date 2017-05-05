package com.nutcracker.wedo.common.framework.cache.redis;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
/**
 * Created by huh on 2017/3/27.
 */
public abstract class BaseRedisCallBack<T> implements RedisCallBack<T> {
    private static final Logger LOGGER = Logger.getLogger(BaseRedisCallBack.class);
    private Exception e;
    private T result;
    private boolean nullValueReGet;

    public BaseRedisCallBack() {
    }

    public T getResult() {
        return this.result;
    }

    public Exception getException() {
        return this.e;
    }

    protected abstract T doOperation(RedisClient var1) throws Exception;

    public final boolean operation(List<RedisClient> clients, boolean read, Object key, RedisClientStatusNotifier notifier) {
        boolean success = false;
        Iterator i$ = clients.iterator();

        while(i$.hasNext()) {
            RedisClient client = (RedisClient)i$.next();
            long time1 = System.currentTimeMillis();

            try {
                this.result = this.doOperation(client);
                long e = System.currentTimeMillis();
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[cacheTask:" + this.getOptionType() + "]" + " <key:" + key + "> <redis client : " + client.getCacheName() + "> <server：" + client.getRedisServer() + "> success ! (use ：" + (e - time1) + " ms)");
                }

                if(read) {
                    if(!this.nullValueReGet || this.result != null) {
                        return true;
                    }
                } else {
                    if(!success) {
                        ;
                    }

                    success = true;
                }
            } catch (Exception var13) {
                if(notifier != null) {
                    notifier.onFaild(client);
                }

                success = success;
                this.e = var13;
                long time2 = System.currentTimeMillis();
                LOGGER.error("[[cacheTask:" + this.getOptionType() + "]" + " <key:" + key + "> <redis client : " + client.getCacheName() + "> <server：" + client.getRedisServer() + "> fail ! (use ：" + (time2 - time1) + " ms)");
            }
        }

        return success;
    }

    public boolean isNullValueReGet() {
        return this.nullValueReGet;
    }

    public void setNullValueReGet(boolean nullValueReGet) {
        this.nullValueReGet = nullValueReGet;
    }
}
