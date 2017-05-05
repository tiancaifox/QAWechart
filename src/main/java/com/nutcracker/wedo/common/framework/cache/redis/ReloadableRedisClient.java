package com.nutcracker.wedo.common.framework.cache.redis;

import com.nutcracker.wedo.common.framework.beans.context.AsyncCloseAction;
import com.nutcracker.wedo.common.framework.beans.context.AsyncCloseHelper;
import com.nutcracker.wedo.common.framework.conf.AbstractConfiguationReloadable;
import com.nutcracker.wedo.common.framework.conf.ConfiguationReloadable;
import com.nutcracker.wedo.common.framework.conf.PropertyUtils;
import com.nutcracker.wedo.common.framework.conf.interfaces.ChangedConfigItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * Created by huh on 2017/3/31.
 */
public class ReloadableRedisClient extends RedisClient implements ConfiguationReloadable, AsyncCloseAction {
    private static final Logger LOGGER = Logger.getLogger(ReloadableRedisClient.class);
    private JedisPool oldJedisPool;
    private Map<String, String> mapping;

    public ReloadableRedisClient() {
    }

    protected void setOldJedisPool(JedisPool oldJedisPool) {
        this.oldJedisPool = oldJedisPool;
    }

    public void changed(List<ChangedConfigItem> changedConfigItemList) {
        List filteredList = AbstractConfiguationReloadable.getPropertyChanged(this.mapping, changedConfigItemList);
        if(CollectionUtils.isNotEmpty(filteredList)) {
            try {
                PropertyUtils.setPropertiesByReflection(filteredList, RedisClient.class, this);
                ReloadableRedisClient e = new ReloadableRedisClient();
                e.setOldJedisPool(this.getJedisPool());
                e.setRealPool(this.getRealPool());
                AsyncCloseHelper.addTask(e);
                this.init();
            } catch (Exception var4) {
                LOGGER.error(var4.getMessage(), var4);
            }
        }

    }

    public void setPropertyMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    public boolean canAsyncClose() {
        return this.oldJedisPool == null?true:(this.getRealPool() == null?true:this.getRealPool().getNumActive() == 0);
    }

    public void asyncClose() {
        if(this.oldJedisPool != null) {
            this.oldJedisPool.destroy();
        }
    }
}

