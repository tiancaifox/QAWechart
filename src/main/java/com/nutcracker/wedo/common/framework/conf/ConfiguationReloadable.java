package com.nutcracker.wedo.common.framework.conf;

import com.nutcracker.wedo.common.framework.conf.interfaces.ConfigItemChangedCallable;

import java.util.Map;

/**
 * Created by huh on 2017/3/31.
 */
public interface ConfiguationReloadable extends ConfigItemChangedCallable {
    void setPropertyMapping(Map<String, String> var1);
}
