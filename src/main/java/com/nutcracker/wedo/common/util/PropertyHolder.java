package com.nutcracker.wedo.common.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by huh on 2017/4/17.
 */
public class PropertyHolder extends PropertyPlaceholderConfigurer {

    private static Map<String, String> proprertiesMap;

    protected void processProperties(ConfigurableListableBeanFactory beanFactory,
                                     Properties props) throws BeansException {
        super.processProperties(beanFactory, props);
        //load properties to ctxPropertiesMap
        proprertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            proprertiesMap.put(keyStr, value);
        }
    }

    //static method for accessing context properties
    public static String getContextProperty(String name) {
        return proprertiesMap.get(name);
    }
}

