package com.nutcracker.wedo.common.framework.conf;

import com.nutcracker.wedo.common.framework.conf.interfaces.ChangedConfigItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by huh on 2017/3/31.
 */
public abstract class AbstractConfiguationReloadable implements ConfiguationReloadable {
    private static final Logger LOGGER = Logger.getLogger(AbstractConfiguationReloadable.class);
    private Map<String, String> mapping;

    public AbstractConfiguationReloadable() {
    }

    protected Map<String, String> getMapping() {
        return this.mapping;
    }

    public void changed(List<ChangedConfigItem> changedConfigItemList) {
        List filteredList = getPropertyChanged(this.mapping, changedConfigItemList);
        if(CollectionUtils.isNotEmpty(filteredList)) {
            this.propertyChanagedCallback(filteredList);
        }

    }

    public static List<ChangedConfigItem> getPropertyChanged(Map<String, String> propertyMapping, List<ChangedConfigItem> changedConfigItemList) {
        if(MapUtils.isEmpty(propertyMapping)) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("ConfiguationReloadable call back but found property mapping is empty ingore this changed call back action");
            }

            return null;
        } else {
            ArrayList filteredList = new ArrayList();
            Iterator i$ = changedConfigItemList.iterator();

            while(i$.hasNext()) {
                ChangedConfigItem changedConfigItem = (ChangedConfigItem)i$.next();
                String configItemName = changedConfigItem.getKey();
                String propName = (String)propertyMapping.get(configItemName);
                if(propName != null) {
                    ChangedConfigItem clonedConfigItem = cloneConfigItem(changedConfigItem);
                    clonedConfigItem.setKey(propName);
                    filteredList.add(clonedConfigItem);
                }
            }

            return filteredList;
        }
    }

    public abstract void propertyChanagedCallback(List<ChangedConfigItem> var1);

    protected static ChangedConfigItem cloneConfigItem(ChangedConfigItem configItem) {
        ChangedConfigItem ret = new ChangedConfigItem();
        ret.setKey(configItem.getKey());
        ret.setNewValue(configItem.getNewValue());
        ret.setOldValue(configItem.getOldValue());
        return ret;
    }

    public void setPropertyMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }
}

