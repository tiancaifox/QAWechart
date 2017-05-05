package com.nutcracker.wedo.common.framework.conf;

import com.nutcracker.wedo.common.framework.conf.interfaces.ChangedConfigItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by huh on 2017/3/31.
 */
public class PropertyUtils {
    private static final Logger LOGGER = Logger.getLogger(PropertyUtils.class);

    public PropertyUtils() {
    }

    public static void setPropertiesByReflection(Map<String, String> properties, final Class<?> cls, final Object target) {
        if(MapUtils.isEmpty(properties)) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("setPropertiesByReflection found a blank properties map");
            }

        } else {
            HashMap copied = new HashMap(properties);
            final HashMap methodMapping = new HashMap();
            Iterator iterator = copied.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry next = (Map.Entry)iterator.next();
                String setMethod = "set" + StringUtils.capitalize((String)next.getKey());
                methodMapping.put(setMethod, next.getValue());
            }

            ReflectionUtils.doWithMethods(cls, new ReflectionUtils.MethodCallback() {
                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                    if(!methodMapping.isEmpty()) {
                        Class[] parameterTypes = method.getParameterTypes();
                        if(parameterTypes != null && parameterTypes.length == 1) {
                            String methodname = method.getName();
                            String configItem = (String)methodMapping.get(methodname);
                            if(configItem != null) {
                                Class paramType = parameterTypes[0];
                                Object value = null;

                                try {
                                    value = this.wrapValue(configItem, paramType);
                                    method.invoke(target, new Object[]{value});
                                    if(PropertyUtils.LOGGER.isInfoEnabled()) {
                                        PropertyUtils.LOGGER.info("Property changed " + cls.getSimpleName() + " call " + methodname + " method to set value " + value);
                                    }
                                } catch (Exception var8) {
                                    PropertyUtils.LOGGER.error("set property by method " + methodname + " and value " + value + " failed:" + var8.getMessage(), var8);
                                }
                            }

                        }
                    }
                }

                private Object wrapValue(String configItem, Class<?> paramType) {
                    return paramType.equals(String.class)?configItem:(!paramType.equals(Integer.TYPE) && !paramType.equals(Integer.class)?(!paramType.equals(Boolean.TYPE) && !paramType.equals(Boolean.class)?(!paramType.equals(Short.TYPE) && !paramType.equals(Short.class)?(!paramType.equals(Long.TYPE) && !paramType.equals(Long.class)?(!paramType.equals(Byte.TYPE) && !paramType.equals(Byte.TYPE)?(!paramType.equals(Float.TYPE) && !paramType.equals(Float.class)?(!paramType.equals(Double.TYPE) && !paramType.equals(Double.class)?configItem:Double.valueOf(configItem)):Float.valueOf(configItem)):Byte.valueOf(configItem)):Long.valueOf(configItem)):Short.valueOf(configItem)):Boolean.valueOf(configItem)):Integer.valueOf(configItem));
                }
            });
        }
    }

    public static void setPropertiesByReflection(List<ChangedConfigItem> list, Class<?> cls, Object target) {
        if(CollectionUtils.isEmpty(list)) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("setPropertiesByReflection found a blank ChangedConfigItem list");
            }

        } else {
            HashMap kvMap = new HashMap();
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
                ChangedConfigItem changedConfigItem = (ChangedConfigItem)i$.next();
                String property = changedConfigItem.getKey();
                String value = changedConfigItem.getNewValue();
                kvMap.put(property, value);
            }

            setPropertiesByReflection((Map)kvMap, cls, target);
        }
    }
}

