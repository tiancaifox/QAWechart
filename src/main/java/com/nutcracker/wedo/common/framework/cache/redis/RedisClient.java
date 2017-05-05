package com.nutcracker.wedo.common.framework.cache.redis;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import javax.annotation.PostConstruct;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by huh on 2017/3/23.
 */
public class RedisClient implements DisposableBean {

    private static final Logger LOG = Logger.getLogger(RedisClient.class);
    private static final String OPERATION_SUCCESS = "OK";
    private String cacheName = "default";
    private String redisServer;
    private String redisAuthKey;
    private JedisPool jedisPool;
    private int port = 6379;
    private int timeout = 2000;
    private int maxIdle = 8; //连接池中最大空闲连接数
    private long maxWait = -1L; //代表当Connection用尽了，多久之后进行回收丢失连接
    private boolean testOnBorrow = false; //默认值是 true ，当从连接池取连接时，验证这个连接是否有效
    private int minIdle = 0;
    private int maxActive = 8; //默认值是 8, 连接池中同时可以分派的最大活跃连接数
    private boolean testOnReturn = false; //默认值是 flase, 当从把该连接放回到连接池的时，验证这个连接是否有效
    private boolean testWhileIdle = false; //默认值是 false, 当连接池中的空闲连接是否有效
    private long timeBetweenEvictionRunsMillis = -1L; //毫秒检查一次连接池中空闲的连接
    private int numTestsPerEvictionRun = 3; //默认值是 3 ，每次验证空闲连接的连接数目
    private long minEvictableIdleTimeMillis = 1800000L; //dbcp默认是30分，需要开启异步线程Evict，否则不生效。原理很简单，就是通过一个异步线程，每次检查connnection上一次使用的时间戳，看看是否已经超过这个timeout时间设置
    private long softMinEvictableIdleTimeMillis = -1L; //在minEvictableIdleTimeMillis基础上，加入了至少 minIdle个对象已经在pool里面了。如果为-1，evicted不会根据idletime驱逐任何对象。
    private boolean lifo = true; //last in first out
    private byte whenExhaustedAction = 1; //表示当pool中的jedis实例都被allocated完时，pool要采取的操作 WHEN_EXHAUSTED_BLOCK=1
    private GenericObjectPool realPool;

    public RedisClient() {
    }

    @PostConstruct
    public void init() {
        GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
        if(this.maxIdle >= 0) {
            poolConfig.maxIdle = this.maxIdle;
        }

        poolConfig.maxWait = this.maxWait;
        if(this.whenExhaustedAction >= 0 && this.whenExhaustedAction < 3) {
            poolConfig.whenExhaustedAction = this.whenExhaustedAction;
        }

        poolConfig.testOnBorrow = this.testOnBorrow;
        poolConfig.minIdle = this.minIdle;
        poolConfig.maxActive = this.maxActive;
        poolConfig.testOnReturn = this.testOnReturn;
        poolConfig.testWhileIdle = this.testWhileIdle;
        poolConfig.timeBetweenEvictionRunsMillis = this.timeBetweenEvictionRunsMillis;
        poolConfig.numTestsPerEvictionRun = this.numTestsPerEvictionRun;
        poolConfig.minEvictableIdleTimeMillis = this.minEvictableIdleTimeMillis;
        poolConfig.softMinEvictableIdleTimeMillis = this.softMinEvictableIdleTimeMillis;
        poolConfig.lifo = this.lifo;
        this.jedisPool = new JedisPool(poolConfig, this.redisServer, this.port, this.timeout, this.redisAuthKey);
        this.realPool = this.getRealPoolInstance();
    }

    protected GenericObjectPool getRealPoolInstance() {
        Field internalPoolField = ReflectionUtils.findField(JedisPool.class, "internalPool", GenericObjectPool.class);
        if(internalPoolField != null) {
            internalPoolField.setAccessible(true);
            GenericObjectPool realPool = (GenericObjectPool)ReflectionUtils.getField(internalPoolField, this.jedisPool);
            return realPool;
        } else {
            return null;
        }
    }

    public Object get(String key) throws Exception {
        Object data = null;
        Jedis jedis = null;

        byte[] data1;
        try {
            jedis = (Jedis)this.jedisPool.getResource();
            long e = System.currentTimeMillis();
            data1 = jedis.get(SafeEncoder.encode(key));
            long end = System.currentTimeMillis();
            LOG.info("getValueFromCache spends：" + (end - e) + " millionseconds.");
        } catch (Exception var11) {
            this.jedisPool.returnBrokenResource(jedis);
            throw var11;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return this.deserialize(data1);
    }

    public String ping() throws Exception {
        String res = null;
        Jedis jedis = null;

        try {
            jedis = (Jedis)this.jedisPool.getResource();
            long e = System.currentTimeMillis();
            res = jedis.ping();
            long end = System.currentTimeMillis();
            LOG.info("Ping spends：" + (end - e) + " millionseconds.");
        } catch (Exception var10) {
            this.jedisPool.returnBrokenResource(jedis);
            throw var10;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return res;
    }

    public boolean set(String key, Object value, Integer expiration) throws Exception {
        String result = "";
        Jedis jedis = null;

        boolean var10;
        try {
            jedis = (Jedis)this.jedisPool.getResource();
            long e = System.currentTimeMillis();
            if(expiration.intValue() > 0) {
                result = jedis.setex(SafeEncoder.encode(key), expiration.intValue(), this.serialize(value));
            } else {
                result = jedis.set(SafeEncoder.encode(key), this.serialize(value));
            }

            long end = System.currentTimeMillis();
            LOG.info("set key:" + key + " spends：" + (end - e) + " millionseconds.");
            var10 = "OK".equalsIgnoreCase(result);
        } catch (Exception var14) {
            LOG.warn(var14.getMessage(), var14);
            this.jedisPool.returnBrokenResource(jedis);
            throw var14;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return var10;
    }

    public boolean set(String key, Object value) throws Exception {
        return this.set(key, value, Integer.valueOf(-1));
    }

    public boolean add(String key, Object value, Integer expiration) throws Exception {
        Jedis jedis = null;

        boolean var10;
        try {
            jedis = (Jedis)this.jedisPool.getResource();
            long e = System.currentTimeMillis();
            Long result = jedis.setnx(SafeEncoder.encode(key), this.serialize(value));
            if(expiration.intValue() > 0) {
                result = Long.valueOf(result.longValue() & jedis.expire(key, expiration.intValue()).longValue());
            }

            long end = System.currentTimeMillis();
            if(result.longValue() == 1L) {
                LOG.info("add key:" + key + " spends：" + (end - e) + " millionseconds.");
            } else {
                LOG.info("add key: " + key + " failed, key has already exists! ");
            }

            var10 = result.longValue() == 1L;
        } catch (Exception var14) {
            LOG.warn(var14.getMessage(), var14);
            this.jedisPool.returnBrokenResource(jedis);
            throw var14;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return var10;
    }

    public boolean add(String key, Object value) throws Exception {
        return this.add(key, value, Integer.valueOf(-1));
    }

    public boolean exists(String key) throws Exception {
        boolean isExist = false;
        Jedis jedis = null;

        try {
            jedis = (Jedis)this.jedisPool.getResource();
            isExist = jedis.exists(SafeEncoder.encode(key)).booleanValue();
        } catch (Exception var8) {
            LOG.warn(var8.getMessage(), var8);
            this.jedisPool.returnBrokenResource(jedis);
            throw var8;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return isExist;
    }

    public boolean delete(String key) {
        Jedis jedis = null;

        try {
            jedis = (Jedis)this.jedisPool.getResource();
            jedis.del(new byte[][]{SafeEncoder.encode(key)});
            LOG.info("delete key:" + key);
            boolean e = true;
            return e;
        } catch (Exception var7) {
            LOG.warn(var7.getMessage(), var7);
            this.jedisPool.returnBrokenResource(jedis);
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return false;
    }

    public boolean expire(String key, int seconds) {
        Jedis jedis = null;

        try {
            jedis = (Jedis)this.jedisPool.getResource();
            jedis.expire(SafeEncoder.encode(key), seconds);
            LOG.info("expire key:" + key + " time after " + seconds + " seconds.");
            boolean e = true;
            return e;
        } catch (Exception var8) {
            LOG.warn(var8.getMessage(), var8);
            this.jedisPool.returnBrokenResource(jedis);
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return false;
    }

    public boolean flushall() {
        String result = "";
        Jedis jedis = null;

        try {
            jedis = (Jedis)this.jedisPool.getResource();
            result = jedis.flushAll();
            LOG.info("redis client name: " + this.getCacheName() + " flushall.");
        } catch (Exception var7) {
            LOG.warn(var7.getMessage(), var7);
            this.jedisPool.returnBrokenResource(jedis);
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return "OK".equalsIgnoreCase(result);
    }

    public void shutdown() {
        try {
            this.jedisPool.destroy();
        } catch (Exception var2) {
            LOG.warn(var2.getMessage(), var2);
        }

    }

    protected byte[] serialize(Object o) {
        if(o == null) {
            return new byte[0];
        } else {
            Object rv = null;

            try {
                ByteArrayOutputStream e = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(e);
                os.writeObject(o);
                os.close();
                e.close();
                byte[] rv1 = e.toByteArray();
                return rv1;
            } catch (IOException var5) {
                throw new IllegalArgumentException("Non-serializable object", var5);
            }
        }
    }

    protected Object deserialize(byte[] in) {
        Object rv = null;

        try {
            if(in != null) {
                ByteArrayInputStream e = new ByteArrayInputStream(in);
                ObjectInputStream is = new ObjectInputStream(e);
                rv = is.readObject();
                is.close();
                e.close();
            }
        } catch (IOException var5) {
            LOG.warn("Caught IOException decoding %d bytes of data", var5);
        } catch (ClassNotFoundException var6) {
            LOG.warn("Caught CNFE decoding %d bytes of data", var6);
        }

        return rv;
    }

    public void hput(String key, String field, Serializable fieldValue) throws Exception {
        Jedis jedis = null;

        try {
            jedis = (Jedis)this.jedisPool.getResource();
            jedis.hset(SafeEncoder.encode(key), SafeEncoder.encode(field), this.serialize(fieldValue));
            LOG.info("hset key:" + key + " field:" + field);
        } catch (Exception var9) {
            LOG.warn(var9.getMessage(), var9);
            this.jedisPool.returnBrokenResource(jedis);
            throw var9;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

    }

    public Object hget(String key, String field) {
        Jedis jedis = null;

        try {
            jedis = (Jedis)this.jedisPool.getResource();
            byte[] e = jedis.hget(SafeEncoder.encode(key), SafeEncoder.encode(field));
            LOG.info("hget key:" + key + " field:" + field);
            Object var5 = this.deserialize(e);
            return var5;
        } catch (Exception var9) {
            LOG.warn(var9.getMessage(), var9);
            this.jedisPool.returnBrokenResource(jedis);
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return null;
    }

    public boolean hdel(String key, String field) throws Exception {
        Jedis jedis = null;

        boolean var6;
        try {
            jedis = (Jedis)this.jedisPool.getResource();
            long e = jedis.hdel(SafeEncoder.encode(key), SafeEncoder.encode(field)).longValue();
            LOG.info("hget key:" + key + " field:" + field);
            var6 = e == 1L;
        } catch (Exception var10) {
            LOG.warn(var10.getMessage(), var10);
            this.jedisPool.returnBrokenResource(jedis);
            throw var10;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return var6;
    }

    public Set<String> hKeys(String key) throws Exception {
        Jedis jedis = null;

        try {
            jedis = (Jedis)this.jedisPool.getResource();
            Set e = jedis.hkeys(SafeEncoder.encode(key));
            LOG.info("hkeys key:" + key);
            HashSet keys;
            if(CollectionUtils.isEmpty(e)) {
                keys = new HashSet(1);
                return keys;
            } else {
                keys = new HashSet(e.size());
                Iterator i$ = e.iterator();

                while(i$.hasNext()) {
                    byte[] bb = (byte[])i$.next();
                    keys.add(SafeEncoder.encode(bb));
                }

                HashSet i$1 = keys;
                return i$1;
            }
        } catch (Exception var10) {
            LOG.warn(var10.getMessage(), var10);
            this.jedisPool.returnBrokenResource(jedis);
            throw var10;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }
    }

    public List<Object> hValues(String key) throws Exception {
        Jedis jedis = null;

        ArrayList ret;
        try {
            jedis = (Jedis)this.jedisPool.getResource();
            List e = jedis.hvals(SafeEncoder.encode(key));
            LOG.info("hvals key:" + key);
            if(!CollectionUtils.isEmpty(e)) {
                ret = new ArrayList(e.size());
                Iterator i$ = e.iterator();

                while(i$.hasNext()) {
                    byte[] bb = (byte[])i$.next();
                    ret.add(this.deserialize(bb));
                }

                ArrayList i$1 = ret;
                return i$1;
            }

            ret = new ArrayList(1);
        } catch (Exception var10) {
            LOG.warn(var10.getMessage(), var10);
            this.jedisPool.returnBrokenResource(jedis);
            throw var10;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return ret;
    }

    public boolean hExists(String key, String field) throws Exception {
        Jedis jedis = null;

        boolean var5;
        try {
            jedis = (Jedis)this.jedisPool.getResource();
            boolean e = jedis.hexists(SafeEncoder.encode(key), SafeEncoder.encode(field)).booleanValue();
            LOG.info("hexists key:" + key + " field:" + field);
            var5 = e;
        } catch (Exception var9) {
            LOG.warn(var9.getMessage(), var9);
            this.jedisPool.returnBrokenResource(jedis);
            throw var9;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return var5;
    }

    public long hLen(String key) throws Exception {
        Jedis jedis = null;

        long var5;
        try {
            jedis = (Jedis)this.jedisPool.getResource();
            long e = jedis.hlen(SafeEncoder.encode(key)).longValue();
            LOG.info("hlen key:" + key);
            var5 = e;
        } catch (Exception var10) {
            LOG.warn(var10.getMessage(), var10);
            this.jedisPool.returnBrokenResource(jedis);
            throw var10;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return var5;
    }

    public Map<String, Object> hGetAll(String key) throws Exception {
        Jedis jedis = null;

        Map var4;
        try {
            jedis = (Jedis)this.jedisPool.getResource();
            Map e = jedis.hgetAll(SafeEncoder.encode(key));
            LOG.info("hgetAll key:" + key);
            var4 = this.decodeMap(e);
        } catch (Exception var8) {
            LOG.warn(var8.getMessage(), var8);
            this.jedisPool.returnBrokenResource(jedis);
            throw var8;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

        return var4;
    }

    private Map<String, Object> decodeMap(Map<byte[], byte[]> values) {
        if(MapUtils.isEmpty(values)) {
            return Collections.emptyMap();
        } else {
            HashMap copy = new HashMap(values);
            Iterator iterator = copy.entrySet().iterator();
            HashMap ret = new HashMap();

            while(iterator.hasNext()) {
                Map.Entry next = (Map.Entry)iterator.next();
                ret.put(SafeEncoder.encode((byte[])next.getKey()), this.deserialize((byte[])next.getValue()));
            }

            return ret;
        }
    }

    private Map<byte[], byte[]> encodeMap(Map<String, Serializable> values) {
        if(MapUtils.isEmpty(values)) {
            return Collections.emptyMap();
        } else {
            HashMap copy = new HashMap(values);
            Iterator iterator = copy.entrySet().iterator();
            HashMap ret = new HashMap();

            while(iterator.hasNext()) {
                Map.Entry next = (Map.Entry)iterator.next();
                ret.put(SafeEncoder.encode((String)next.getKey()), this.serialize(next.getValue()));
            }

            return ret;
        }
    }

    public void hmSet(String key, Map<String, Serializable> values) throws Exception {
        Jedis jedis = null;

        try {
            jedis = (Jedis)this.jedisPool.getResource();
            jedis.hmset(SafeEncoder.encode(key), this.encodeMap(values));
            LOG.info("hmSet key:" + key + " field:" + values.keySet());
        } catch (Exception var8) {
            LOG.warn(var8.getMessage(), var8);
            this.jedisPool.returnBrokenResource(jedis);
            throw var8;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }

    }

    private byte[][] encodeArray(String[] array) {
        if(ArrayUtils.isEmpty(array)) {
            return new byte[0][0];
        } else {
            int len = array.length;
            ArrayList list = new ArrayList(len);

            for(int i = 0; i < len; ++i) {
                list.add(SafeEncoder.encode(array[i]));
            }

            return (byte[][])list.toArray(new byte[len][0]);
        }
    }

    public List<Object> hmGet(String key, String... fields) throws Exception {
        Jedis jedis = null;

        try {
            jedis = (Jedis)this.jedisPool.getResource();
            List e = jedis.hmget(SafeEncoder.encode(key), this.encodeArray(fields));
            LOG.info("hmGet key:" + key + " fields:" + Arrays.toString(fields));
            ArrayList ret;
            if(CollectionUtils.isEmpty(e)) {
                ret = new ArrayList(1);
                return ret;
            } else {
                ret = new ArrayList(e.size());
                Iterator i$ = e.iterator();

                while(i$.hasNext()) {
                    byte[] bb = (byte[])i$.next();
                    ret.add(this.deserialize(bb));
                }

                ArrayList i$1 = ret;
                return i$1;
            }
        } catch (Exception var11) {
            LOG.warn(var11.getMessage(), var11);
            this.jedisPool.returnBrokenResource(jedis);
            throw var11;
        } finally {
            if(jedis != null) {
                this.jedisPool.returnResource(jedis);
            }

        }
    }

    public void destroy() throws Exception {
        this.jedisPool.destroy();
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    protected void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    protected void setRealPool(GenericObjectPool realPool) {
        this.realPool = realPool;
    }

    protected GenericObjectPool getRealPool() {
        return this.realPool;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getRedisServer() {
        return this.redisServer;
    }

    public void setRedisServer(String redisServer) {
        this.redisServer = redisServer;
    }

    public String getCacheName() {
        return this.cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getRedisAuthKey() {
        return this.redisAuthKey;
    }

    public void setRedisAuthKey(String redisAuthKey) {
        this.redisAuthKey = redisAuthKey;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxIdle() {
        return this.maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaxWait() {
        return this.maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isTestOnBorrow() {
        return this.testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public int getMinIdle() {
        return this.minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return this.maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public boolean isTestOnReturn() {
        return this.testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return this.testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public int getNumTestsPerEvictionRun() {
        return this.numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public long getSoftMinEvictableIdleTimeMillis() {
        return this.softMinEvictableIdleTimeMillis;
    }

    public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

    public boolean isLifo() {
        return this.lifo;
    }

    public void setLifo(boolean lifo) {
        this.lifo = lifo;
    }

    public byte getWhenExhaustedAction() {
        return this.whenExhaustedAction;
    }

    public void setWhenExhaustedAction(byte whenExhaustedAction) {
        this.whenExhaustedAction = whenExhaustedAction;
    }

    public int getTimeout() {
        return this.timeout;
    }
}
