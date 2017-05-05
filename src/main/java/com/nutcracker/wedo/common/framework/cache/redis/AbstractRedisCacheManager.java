package com.nutcracker.wedo.common.framework.cache.redis;

import com.nutcracker.wedo.common.framework.cache.ExtCacheManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;
import java.util.*;

/**
 * Created by huh on 2017/3/27.
 */
public abstract class AbstractRedisCacheManager implements ExtCacheManager, RedisClientStatusNotifier, InitializingBean {
    private List<RedisClient> clientList;
    private static final Logger LOGGER = Logger.getLogger(AbstractRedisCacheManager.class);
    private static final int DEFALUT_RETRY_TIMES = 3;
    private int retryTimes = 3;
    private boolean nullValueReGet = false;

    public AbstractRedisCacheManager() {
    }

    protected final boolean isNullValueReGet() {
        return this.nullValueReGet;
    }

    public void setNullValueReGet(boolean nullValueReGet) {
        this.nullValueReGet = nullValueReGet;
    }

    protected int getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        if(retryTimes < 1) {
            this.retryTimes = 3;
        } else {
            this.retryTimes = retryTimes;
        }

        LOGGER.warn("set RedisCacheManager retry times to " + this.retryTimes);
    }

    public String put(Object key, Object obj) {
        return this.put(key, Integer.valueOf(-1), obj);
    }

    private boolean checkClients(List<RedisClient> clients) {
        if(CollectionUtils.isEmpty(clients)) {
            throw new RuntimeException("No redis Client available. maybe all clients failed to connect server.");
        } else {
            return true;
        }
    }

    public String put(final Object key, final Integer expiration, final Object obj) {
        List<RedisClient> clients = this.getClients(key);
        String cacheName = null;
        if(this.checkClients(clients)) {
            cacheName = ((RedisClient)clients.get(0)).getCacheName();
            this.exeCommand(new BaseRedisCallBack() {
                public Boolean doOperation(RedisClient redisClient) throws Exception {
                    return Boolean.valueOf(redisClient.set(key.toString(), obj, expiration));
                }

                public String getOptionType() {
                    return "PUT";
                }
            }, key, false);
        }

        return cacheName;
    }

    public Object get(final Object key) {
        List<RedisClient> clients = this.getClients(key);
        return this.checkClients(clients)?this.exeCommand(new BaseRedisCallBack() {
            public Object doOperation(RedisClient redisClient) throws Exception {
                return redisClient.get(key.toString());
            }

            public String getOptionType() {
                return "GET";
            }
        }, key, true):null;
    }

    public String remove(final Object key) {
        List<RedisClient> clients = this.getClients(key);
        String cacheName = null;
        if(this.checkClients(clients)) {
            cacheName = ((RedisClient)clients.get(0)).getCacheName();
            this.exeCommand(new BaseRedisCallBack() {
                public Boolean doOperation(RedisClient redisClient) throws Exception {
                    return Boolean.valueOf(redisClient.delete(key.toString()));
                }

                public String getOptionType() {
                    return "REMOVE";
                }
            }, key, false);
        }

        return cacheName;
    }

    public String replace(Object key, Object obj) {
        return this.replace(key, Integer.valueOf(-1), obj);
    }

    public String replace(Object key, Integer expiration, Object obj) {
        return this.put(key, expiration, obj);
    }

    public boolean existsKey(final String key) {
        List<RedisClient> clients = this.getClients(key);
        return this.checkClients(clients)?((Boolean)this.exeCommand(new BaseRedisCallBack() {
            public Boolean doOperation(RedisClient redisClient) throws Exception {
                return Boolean.valueOf(redisClient.exists(key));
            }

            public String getOptionType() {
                return "EXIST";
            }
        }, key, true)).booleanValue():false;
    }

    public void shutdown() {
        if(this.clientList != null) {
            Iterator i$ = this.clientList.iterator();

            while(i$.hasNext()) {
                RedisClient redisClient = (RedisClient)i$.next();

                try {
                    redisClient.shutdown();
                } catch (Exception var4) {
                    LOGGER.debug(var4.getMessage(), var4);
                }
            }

        }
    }

    protected abstract List<RedisClient> getClients(Object var1);

    protected List<RedisClient> getClientList() {
        return this.clientList;
    }

    public void setClientList(List<RedisClient> clientList) {
        this.clientList = new ArrayList<RedisClient>(clientList);
    }

    private <T> T exeCommand(RedisCallBack<T> redisCallBack, Object key, boolean read) {
        redisCallBack.setNullValueReGet(this.isNullValueReGet());

        for(int i = 0; i < this.getRetryTimes(); ++i) {
            boolean result = redisCallBack.operation(this.getClients(key), read, key, this);
            if(result) {
                return redisCallBack.getResult();
            }
        }

        if(redisCallBack.getException() != null) {
            throw new RuntimeException(redisCallBack.getException().getMessage(), redisCallBack.getException());
        } else {
            return null;
        }
    }

    public boolean extendTime(final String key, final Integer expirationMs) {
        List<RedisClient> clients = this.getClients(key);
        return this.checkClients(clients)?((Boolean)this.exeCommand(new BaseRedisCallBack() {
            public Boolean doOperation(RedisClient redisClient) throws Exception {
                return Boolean.valueOf(redisClient.expire(key, expirationMs.intValue() / 1000));
            }

            public String getOptionType() {
                return "EXPIRE";
            }
        }, key, false)).booleanValue():false;
    }

    public void hput(final String key, final String field, final Serializable fieldValue) {
        List<RedisClient> clients = this.getClients(key);
        if(this.checkClients(clients)) {
            this.exeCommand(new BaseRedisCallBack() {
                public Object doOperation(RedisClient redisClient) throws Exception {
                    redisClient.hput(key, field, fieldValue);
                    return null;
                }

                public String getOptionType() {
                    return "HPUT";
                }
            }, key, false);
        }

    }

    public Object hget(final String key, final String field) {
        List<RedisClient> clients = this.getClients(key);
        return this.checkClients(clients)?this.exeCommand(new BaseRedisCallBack() {
            public Object doOperation(RedisClient redisClient) throws Exception {
                return redisClient.hget(key, field);
            }

            public String getOptionType() {
                return "HGET";
            }
        }, key, true):null;
    }

    public boolean hdel(final String key, final String field) {
        List<RedisClient> clients = this.getClients(key);
        return this.checkClients(clients)?((Boolean)this.exeCommand(new BaseRedisCallBack() {
            public Boolean doOperation(RedisClient redisClient) throws Exception {
                return Boolean.valueOf(redisClient.hdel(key, field));
            }

            public String getOptionType() {
                return "HDEL";
            }
        }, key, false)).booleanValue():false;
    }

    public Set<String> hKeys(final String key) {
        List<RedisClient> clients = this.getClients(key);
        return this.checkClients(clients)?(Set)this.exeCommand(new BaseRedisCallBack() {
            public Set<String> doOperation(RedisClient redisClient) throws Exception {
                return redisClient.hKeys(key);
            }

            public String getOptionType() {
                return "HKEYS";
            }
        }, key, true):Collections.emptySet();
    }

    public List<Object> hValues(final String key) {
        List<RedisClient> clients = this.getClients(key);
        return this.checkClients(clients)?(List)this.exeCommand(new BaseRedisCallBack() {
            public List<Object> doOperation(RedisClient redisClient) throws Exception {
                return redisClient.hValues(key);
            }

            public String getOptionType() {
                return "HVALUES";
            }
        }, key, true):Collections.emptyList();
    }

    public boolean hExists(final String key, final String field) {
        List<RedisClient> clients = this.getClients(key);
        return this.checkClients(clients)?((Boolean)this.exeCommand(new BaseRedisCallBack() {
            public Boolean doOperation(RedisClient redisClient) throws Exception {
                return Boolean.valueOf(redisClient.hExists(key, field));
            }

            public String getOptionType() {
                return "HEXISTS";
            }
        }, key, true)).booleanValue():false;
    }

    public long hLen(final String key) {
        List<RedisClient> clients = this.getClients(key);
        return this.checkClients(clients)?((Long)this.exeCommand(new BaseRedisCallBack() {
            public Long doOperation(RedisClient redisClient) throws Exception {
                return Long.valueOf(redisClient.hLen(key));
            }

            public String getOptionType() {
                return "HLEN";
            }
        }, key, true)).longValue():0L;
    }

    public void hmSet(final String key, final Map<String, Serializable> values) {
        List<RedisClient> clients = this.getClients(key);
        if(this.checkClients(clients)) {
            this.exeCommand(new BaseRedisCallBack() {
                public Object doOperation(RedisClient redisClient) throws Exception {
                    redisClient.hmSet(key, values);
                    return null;
                }

                public String getOptionType() {
                    return "HMSET";
                }
            }, key, false);
        }

    }

    public List<Object> hmGet(final String key, final String... fields) {
        List<RedisClient> clients = this.getClients(key);
        return this.checkClients(clients)?(List)this.exeCommand(new BaseRedisCallBack() {
            public List<Object> doOperation(RedisClient redisClient) throws Exception {
                return redisClient.hmGet(key, fields);
            }

            public String getOptionType() {
                return "HMGET";
            }
        }, key, true):Collections.emptyList();
    }

    public Map<String, Object> hGetAll(final String key) {
        List<RedisClient> clients = this.getClients(key);
        return this.checkClients(clients)?(Map)this.exeCommand(new BaseRedisCallBack() {
            public Map<String, Object> doOperation(RedisClient redisClient) throws Exception {
                return redisClient.hGetAll(key);
            }

            public String getOptionType() {
                return "HGETALL";
            }
        }, key, true):Collections.emptyMap();
    }

    public void onFaild(RedisClient client) {
    }

    public void onOk(RedisClient client) {
    }

    public void afterPropertiesSet() throws Exception {
        if(CollectionUtils.isEmpty(this.getClientList())) {
            throw new IllegalArgumentException("client list is empty!");
        }
    }
}