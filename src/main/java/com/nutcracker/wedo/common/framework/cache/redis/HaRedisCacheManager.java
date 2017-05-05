package com.nutcracker.wedo.common.framework.cache.redis;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huh on 2017/3/31.
 */
public class HaRedisCacheManager extends AbstractRedisCacheManager implements DisposableBean {
    private static final Logger LOGGER = Logger.getLogger(HaRedisCacheManager.class);
    private ExecutorService es;
    private Set<RedisClient> failedClients;
    private long validInterval = 1000L;
    private HaRedisCacheManager.RedisClientHeartBeat rchb;
    private boolean enableHeartBeat = true;
    private RedisClientStatusChecker redisClientStatusChecker;

    public HaRedisCacheManager() {
    }

    public long getValidInterval() {
        return this.validInterval;
    }

    public void setValidInterval(long validInterval) {
        this.validInterval = validInterval;
    }

    protected Set<RedisClient> getFailedClients() {
        return this.failedClients;
    }

    public void setRedisClientStatusChecker(RedisClientStatusChecker redisClientStatusChecker) {
        this.redisClientStatusChecker = redisClientStatusChecker;
    }

    public void setEnableHeartBeat(boolean enableHeartBeat) {
        this.enableHeartBeat = enableHeartBeat;
    }

    public String toString() {
        int size = 0;
        if(CollectionUtils.isNotEmpty(this.getClientList())) {
            size = this.getClientList().size();
        }

        return "HaRedisCacheManager clientSize[" + size + "]";
    }

    protected List<RedisClient> getClients(Object key) {
        return new ArrayList(this.getClientList());
    }

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if(this.failedClients == null) {
            this.failedClients = new CopyOnWriteArraySet();
        }

        if(!this.enableHeartBeat) {
            LOGGER.warn("HA redis manager is disabled heart beat. if you want to enable it to set enableHeartBeat=true");
        } else if(this.redisClientStatusChecker == null) {
            this.redisClientStatusChecker = new RedisClientStatusPingChecker();
            LOGGER.warn("property \'redisClientStatusChecker\' is null use RedisClientStatusPingChecker");
        }

    }

    public void onFaild(RedisClient client) {
        this.failedClients.add(client);
        this.getClientList().remove(client);
        this.executeHeartBeat();
    }

    public void onOk(RedisClient client) {
        if(!this.getClientList().contains(client)) {
            this.getClientList().add(client);
        }

        this.failedClients.remove(client);
    }

    public void destroy() throws Exception {
        if(this.rchb != null) {
            this.rchb.close();
        }

        if(this.es != null) {
            this.es.shutdown();
        }

    }

    private synchronized void executeHeartBeat() {
        if(!this.enableHeartBeat) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("HA redis manager is disabled heart beat.");
            }

        } else if(this.redisClientStatusChecker == null) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("redisClientStatusChecker is null so should disable heart beat.");
            }

        } else {
            if(this.rchb == null) {
                this.rchb = new HaRedisCacheManager.RedisClientHeartBeat(this);
                if(this.es == null) {
                    this.es = Executors.newFixedThreadPool(1);
                }

                this.es.execute(this.rchb);
            } else if(!this.rchb.isRuning()) {
                if(this.es == null) {
                    this.es = Executors.newFixedThreadPool(1);
                }

                this.es.execute(this.rchb);
            }

        }
    }

    private static class RedisClientHeartBeat implements Runnable {
        private static final Logger LOGGER = Logger.getLogger(HaRedisCacheManager.RedisClientHeartBeat.class);
        private boolean runing;
        private HaRedisCacheManager redisCacheManager;
        private boolean close = false;

        public RedisClientHeartBeat(HaRedisCacheManager redisCacheManager) {
            this.redisCacheManager = redisCacheManager;
        }

        public synchronized boolean isRuning() {
            return this.runing;
        }

        public synchronized void close() {
            this.close = true;
        }

        public void run() {
            this.runing = true;

            while(!this.redisCacheManager.getFailedClients().isEmpty() && !this.close && this.redisCacheManager.redisClientStatusChecker != null) {
                ArrayList clients = new ArrayList(this.redisCacheManager.getFailedClients());
                Iterator e = clients.iterator();

                while(e.hasNext()) {
                    RedisClient redisClient = (RedisClient)e.next();
                    if(this.redisCacheManager.redisClientStatusChecker.checkStatus(redisClient)) {
                        this.redisCacheManager.onOk(redisClient);
                        if(LOGGER.isInfoEnabled()) {
                            LOGGER.info("Redis Client[" + redisClient.getRedisServer() + "] heartbeat recover success!");
                        }
                    }
                }

                try {
                    Thread.sleep(this.redisCacheManager.getValidInterval());
                } catch (Exception var4) {
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug(var4.getMessage(), var4);
                    }
                }
            }

            this.runing = false;
        }
    }
}

